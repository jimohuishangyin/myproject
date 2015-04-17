package com.xmyunyou.wcd.ui.circle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.FileInfo;
import com.xmyunyou.wcd.model.MyFavValue;
import com.xmyunyou.wcd.model.Reply;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.ReplyList;
import com.xmyunyou.wcd.ui.circle.face.FaceRelativeLayout;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;
import com.xmyunyou.wcd.utils.net.UploadImageManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_discuss_detail)
public class DiscussDetailActivity extends BaseActivity {

    public static final int REQUEST_CODE_IMPORT = 1001;

    public static final String PARAMS_DISCUSS_TID = "DISCUSS_ID";

    public static final String UPLOAD_PIC_TAG = "【图片】";



    @Extra(PARAMS_DISCUSS_TID)
    int mDiscussID;

    @ViewById(R.id.btn_share)
    ImageView mShareImageView;
    @ViewById(R.id.btn_fav)
    ImageView mFavImageView;
    @ViewById(R.id.discuss_detail_logo)
    ImageView mLogoImageView;
    @ViewById(R.id.discuss_detail_name)
    TextView mNameTextView;
    @ViewById(R.id.discuss_detail_comment)
    TextView mCommentTextView;
    @ViewById(R.id.discuss_detail_date)
    TextView mDateTextView;
    @ViewById(R.id.discuss_detail_content)
    WebView mWebView;
    @ViewById(R.id.discuss_detail_medal)
    LinearLayout mMedalLinearLayout;
    @ViewById(R.id.topic_reply_list)
    LoadMoreView mListView;
    @ViewById(R.id.discuss_detail_et)
    EditText mContentEditText;
    @ViewById(R.id.reply_face_ll)
    FaceRelativeLayout mFaceRelativeLayout;
    @ViewById(R.id.discuss_scroll)
    MyScrollView mScrollView;

    //回复列表
    private List<Reply> mReplyList;
    private ReplyAdapter mReplyAdapter;

    private String mPath;

    //缓存收藏
    private List<MyFavValue> mFavList;
    private boolean isFav = false;
    private MyFavValue mFavValue;


    @SystemService
    InputMethodManager mInputMethodManager;

    private String mReplyStr;
    private int mReplyToID;
    //读取html
    private String mHtmlContent;

    private Topic mTopic;

    private String mContent;

    private int mCurrentPage = 1;
    private boolean hasPage = false;
    private boolean isLoading = true;

    private String mUserID;
    private String mImageUrl;

    @AfterInject void loadData(){
        try {
            InputStream is = getAssets().open("news_detail.html");
            //获取文件的字节数
            int lenght = is.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            is.read(buffer);
            mHtmlContent = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //帖子详情
        requestDiscussDetail();
        //回复列表
        requestReplyList();
        /*//设置是否收藏
        mFavList = new ArrayList<>();
        if (isLogin()) {
            List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
            if (values != null) {
                mFavList.addAll(values);
                int userid = DataUtils.getLoginUser(mActivity).getID();
                for (MyFavValue v : values) {
                    isFav = (v.getNewsID() == mDiscussID && userid == v.getUserID());
                    if (isFav) {
                        mFavValue = v;
                        mFavImageView.setSelected(true);
                        break;
                    }
                }
            }
        }*/

    }

    @AfterViews void buildComponent(){
        mReplyList = new ArrayList<>();
        mReplyAdapter = new ReplyAdapter(mActivity, mReplyList);
        mListView.setAdapter(mReplyAdapter);

        mShareImageView.setVisibility(View.VISIBLE);
        mFavImageView.setVisibility(View.VISIBLE);
        mFaceRelativeLayout.setEditTextComponent(mContentEditText);
        mContentEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mFaceRelativeLayout.isShown()) {
                    mFaceRelativeLayout.toggleFaceView();
                }
                return false;
            }
        });

        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if(!hasPage && isLoading){
                    isLoading = false;
                    mCurrentPage ++;
                    requestReplyList();
                }
            }
        });
    }

    private void requestDiscussDetail(){
        Map<String, String> params = new HashMap<>();
        params.put("TopicID", mDiscussID + "");

        requestPost(Constants.TOPIC_DETAIL, params, Topic.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                mTopic = (Topic) result;
                bindValue();
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

    private void bindValue(){
        //设置用户
        User user = mTopic.getTopicUser();
        mActivity.loadImg(user.getAvatarUrl(), mLogoImageView);
        mNameTextView.setText(user.getName());
        //设置勋章
        mActivity.createMedal(user.getMedals(), mMedalLinearLayout);

        //日期
        mDateTextView.setText(Globals.convertDate(mTopic.getCreateDate()));
        mCommentTextView.setText(mTopic.getReplyCount() + "");
        String htmlContent = mHtmlContent.replace("[title]", mTopic.getTitle()).replace("[date]", Globals.convertDateHHMM(mTopic.getCreateDate())).replace("[body]", mTopic.getContent());
        mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
    }

    @Click({R.id.discuss_detail_face, R.id.btn_comment, R.id.discuss_detail_import}) void onClick(View v){
        switch (v.getId()){
            case R.id.discuss_detail_face:
                mInputMethodManager.hideSoftInputFromWindow(
                        mContentEditText.getWindowToken(), 0);
                mFaceRelativeLayout.toggleFaceView();
                break;
            case R.id.btn_comment:
                if(isLogin()) {
                    mContent = getEditTextStr(mContentEditText);
                    if(TextUtils.isEmpty(mContent)){
                        showToast("请输入评论内容");
                        return;
                    }

                    showProgressDialog();

                    final User user = DataUtils.getLoginUser(mActivity);
                    mUserID = RsaHelper.encryptDataFromStr(user.getID() + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
                    if(mContent.indexOf(UPLOAD_PIC_TAG) >= 0 && !TextUtils.isEmpty(mPath)){
                        mContent = mContent.replaceAll(UPLOAD_PIC_TAG, "");
                        //上传文件
                        uploadFile(mPath);
                    }else{
                        //直接发贴
                        sendReply();
                    }
                } else {
                    LoginActivity_.intent(mActivity).start();
                }
                break;
            case R.id.discuss_detail_import:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, REQUEST_CODE_IMPORT);
                break;


        }
    }

    //收藏
    @Click(R.id.btn_fav)
    void fav() {
        if (isLogin()) {
            List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
            if (values != null) {
                for (MyFavValue fv : values) {
                    isFav = (fv.getNewsID() == mDiscussID && DataUtils.getLoginUser(mActivity).getID() == fv.getUserID());
                    if (isFav) {
                        mFavValue = fv;
                        mFavImageView.setSelected(true);
                        break;
                    }
                }
            }
            if (isFav) {
                //删除收藏
                deleteFav(mFavValue.getFavID());
            } else {
                //创建收藏
                int id  = DataUtils.getLoginUser(mActivity).getID();
                String userid = RsaHelper.encryptDataFromStr(id + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
                createFav(userid, mDiscussID);
            }
        } else {
            startActivity(new Intent(mActivity, LoginActivity_.class));
        }
    }
    /**a
     * 创建收藏
     *
     * @param uid
     * @param NewsID
     */
    private void createFav(final String uid, final int NewsID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserID", uid);
        params.put("toid", NewsID + "");
        params.put("type",2+"");
        mActivity.requestPost(Constants.FAV, params, Integer.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Integer b = (Integer) result;
                if (b > 0) {
                    MyFavValue m = new MyFavValue();
                    m.setUserID(DataUtils.getLoginUser(mActivity).getID());
                    m.setNewsID(NewsID);
                    m.setFavID(b);
                    mFavList.add(m);
                    mFavValue = m;
                    String jsonStr = new Gson().toJson(mFavList);
                    log("json:" + jsonStr);
                    DataUtils.putString(mActivity, DataUtils.COLLECTION, jsonStr);

                    mFavImageView.setSelected(true);
                    mActivity.showToast("收藏成功");
                } else {
                    mActivity.showToast("收藏失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    /**
     * 删除收藏
     *
     * @param favID
     */
    private void deleteFav(int favID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("IDs", favID + "");
        params.put("Password",DataUtils.getLoginUser(mActivity).getPassword());
        params.put("userID",DataUtils.getLoginUser(mActivity).getID()+"");
        mActivity.requestGet(Constants.DEL_FAV, params, Boolean.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Boolean b = (Boolean) result;
                if (b) {
                    for (MyFavValue v : mFavList) {
                        if (v.getFavID() == mFavValue.getFavID()) {
                            boolean is = mFavList.remove(v);
                            break;
                        }
                    }
                    isFav = false;
                    String jsonStr = new Gson().toJson(mFavList);
                    log("json:" + jsonStr);
                    DataUtils.putString(mActivity, DataUtils.COLLECTION, jsonStr);
                    mFavImageView.setSelected(false);
                    mActivity.showToast("删除成功");
                } else {
                    mActivity.showToast("删除失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    private void uploadFile(final String path) {
        //showToast("正在发送中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileInfo info = new FileInfo();
                info.setName("filename");
                info.setFile(new File(path));

                Map<String, String> params = new HashMap<String, String>();
                params.put("SiteCode", "T");
                params.put("UserID", mUserID);
                params.put("action", "bbspic");
                mImageUrl = UploadImageManager.httpPost(Constants.UPLOAD_PIC, params,
                        info);
                if (!TextUtils.isEmpty(mImageUrl)) {
                    // 上传成功
                    Message msg = mHandler.obtainMessage(1);
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage(2);
                    msg.obj = path;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sendReply();
                    break;
                case 2:
                    final String path = (String) msg.obj;
                    // 失败时提示
                    TipDialog tipDialog = new TipDialog(mActivity, "上传失败，再试试？").setButtonOk("再试试").setButtonCancel("取消").setOnSuccessListener(new TipDialog.OnSuccessListener() {
                        @Override
                        public void onSuccess(TipDialog dialog) {
                            uploadFile(path);
                        }
                    });
                    tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // 取消直接发帖
                            showToast("正在发送中...");
                            sendReply();
                        }
                    });
                    tipDialog.show();
                    break;
            }
        }
    };

    public void replyToUser(Reply r){
        mReplyToID = r.getID();
        mReplyStr = "回复:" + r.getReplyUser().getName() + "  ";
        mContentEditText.setText(mReplyStr);
        mContentEditText.setSelection(mReplyStr.length());
        mInputMethodManager.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 请求评论
     */
    private void requestReplyList(){
        Map<String, String> params = new HashMap<>();
        params.put("TopicID", mDiscussID + "");
        params.put("size", Constants.PAGE_SIZE);
        params.put("page", mCurrentPage + "");

        requestPost(Constants.REPLY_LIST, params, ReplyList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ReplyList rl = (ReplyList) result;
                mReplyList.addAll(rl.getList());
                mReplyAdapter.notifyDataSetChanged();
                hasPage = mReplyList.size() == rl.getTotalCount();
                mListView.onLoadComplete(hasPage);
                isLoading = true;
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });
    }

    private void sendReply(){
        final User user = DataUtils.getLoginUser(mActivity);
        String userid = RsaHelper.encryptDataFromStr(user.getID() + "", DataUtils.getString(mActivity, DataUtils.RSA_DATA));
        Map<String, String> params = new HashMap<>();
        params.put("Content", mContent);
        params.put("TopicID", mDiscussID + "");
        params.put("ToReplyID", mReplyToID + "");
        params.put("Pictures", mImageUrl);
        params.put("DeviceID", Globals.getDeviceID(mActivity));
        params.put("DeviceName", Globals.getDeviceName());
        params.put("UserID", userid);

        requestPost(Constants.SEND_REPLY, params, int.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                dismisProgressDialog();
                int finish = (int) result;
                if(finish > 0){
                    // 发表后手动加到评论后面
                    Reply r = new Reply();
                    r.setContent(mContent);
                    ArrayList<String> lt = new ArrayList<String>();
                    lt.add(mImageUrl);
                    r.setPictures(lt);
                    r.setCreateDate(System.currentTimeMillis() + "");

                    User info = new User();
                    info.setAvatarUrl(user.getAvatarUrl());
                    info.setName(user.getName());
                    info.setMedals(user.getMedals());
                    r.setReplyUser(info);
                    mReplyList.add(r);
                    mReplyAdapter.notifyDataSetChanged();

                    mContentEditText.setText("");
                    showToast("发表成功");

                    mFaceRelativeLayout.setVisibility(View.GONE);
                    //mFaceRelativeLayout.toggleFaceView();
                } else if (finish == -2) {
                    showToast("回复失败，您的设备已被禁止回复");
                } else if (finish == -3) {
                    showToast("回复失败，您所在网络的IP已被禁止回复");
                } else if (finish == -4) {
                    showToast("您回复的速度过快，5秒后再来");
                } else if (finish == -100) {
                    showToast("数据库异常");
                } else if (finish == -200) {
                    showToast("未知异常");
                } else {
                    showToast("发表失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });

    }

    /**
     * 导入图片
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(REQUEST_CODE_IMPORT)
    void importPic(int resultCode, Intent data) {
        if (data != null) {
            mPath = Globals.importImage(this, data);
            final String content = getEditTextStr(mContentEditText);
            mContentEditText.setText(content + UPLOAD_PIC_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFaceRelativeLayout.getVisibility() == View.VISIBLE) {
            mFaceRelativeLayout.toggleFaceView();
        } else {
            //setParams();
            super.onBackPressed();
        }
    }
}
