package com.xmyunyou.wcd.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.FileInfo;
import com.xmyunyou.wcd.model.MyFav;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.MyFavList;
import com.xmyunyou.wcd.ui.adapter.ArticleAdapter;
import com.xmyunyou.wcd.ui.dialog.PhotoDialog;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.BitmapUtils;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;
import com.xmyunyou.wcd.utils.net.UploadImageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/1/12.
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_LOGIN = 1001;
    public static final int REQUEST_CODE_SETTINGS = REQUEST_CODE_LOGIN + 1;
    public static final int DELAY_TIME = 2500;
    private long mDelayTime;
    private ImageView mLogoImageView;
    private TextView mNameTextView;
    private ImageView mBackImageView;
    private ImageView mSettingImageView;
    private LoadMoreListView mListView;
    private TextView mTitleTextView;
    private TextView mLabelTextView;

    private List<Article> mNewsList;


    private ArticleAdapter mFavAdapter;

    private LinearLayout mMyFavLinearLayout;
    private LinearLayout mMyPostsLinearLayout;
    private LinearLayout mMyJfLinearLayout;
    private LinearLayout mMyCarLinearLayout;
    //勋章
    private LinearLayout mMedalLinearLayout;
    private TextView mJifenTextView;
    private int mCurrentPage = 1;

    private PhotoDialog mPhotoDialog;

    //加密过后的userid
    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mLogoImageView = (ImageView) findViewById(R.id.user_logo);
        mNameTextView = (TextView) findViewById(R.id.user_name);
        mBackImageView = (ImageView) findViewById(R.id.btn_back);
        mBackImageView.setVisibility(View.GONE);
        mSettingImageView = (ImageView) findViewById(R.id.user_settings);
        mMyFavLinearLayout = (LinearLayout) findViewById(R.id.myfav);
        mMyPostsLinearLayout = (LinearLayout) findViewById(R.id.myposts);
        mMyJfLinearLayout = (LinearLayout) findViewById(R.id.myjf);
        mMyCarLinearLayout = (LinearLayout) findViewById(R.id.mycar);
        mMedalLinearLayout = (LinearLayout) findViewById(R.id.user_medal);
        mJifenTextView = (TextView) findViewById(R.id.user_jf);
        mLabelTextView = (TextView) findViewById(R.id.user_center_medal_label);
        mMyFavLinearLayout.setOnClickListener(this);
        mMyPostsLinearLayout.setOnClickListener(this);
        mMyJfLinearLayout.setOnClickListener(this);
        mMyCarLinearLayout.setOnClickListener(this);
        mNameTextView.setText(DataUtils.getString(mActivity, DataUtils.USER_NAME));
        findViewById(R.id.user_settings).setVisibility(View.VISIBLE);
        findViewById(R.id.user_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(mActivity, UserSetingsActivity.class);
                startActivityForResult(settingIntent, REQUEST_CODE_SETTINGS);
            }
        });
        mTitleTextView.setText("个人中心");
        setUserLogin();

    }

    private void setUserLogin() {
        if (isLogin()) {
            mLabelTextView.setVisibility(View.VISIBLE);
            mJifenTextView.setVisibility(View.VISIBLE);
            mMedalLinearLayout.setVisibility(View.VISIBLE);
            //加载数据
            User u = DataUtils.getLoginUser(mActivity);
            mNameTextView.setText(u.getName());
            createMedal(u.getMedals(), mMedalLinearLayout);
            mJifenTextView.setText("积分：" + u.getJiFen());
            ImageLoader.getInstance().loadImage(u.getAvatarUrl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mLogoImageView.setImageBitmap(BitmapUtils.toRoundBitmap(bitmap));
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            mLogoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final User user = DataUtils.getLoginUser(mActivity);
                    String ras = DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA);
                    mUserID = RsaHelper.encryptDataFromStr(user.getID() + "", ras);

                    mPhotoDialog = new PhotoDialog(UserCenterActivity.this);
                    mPhotoDialog.show();
                }
            });
        } else {
            mLabelTextView.setVisibility(View.GONE);
            mJifenTextView.setVisibility(View.GONE);
            mMedalLinearLayout.setVisibility(View.GONE);
            mLogoImageView.setOnClickListener(null);
            mNameTextView.setText("未登录，点击登录");
            mLogoImageView.setImageResource(R.drawable.user_big);
            findViewById(R.id.user_login_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, LoginActivity_.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myfav:
                if (isLogin()) {
                    User u = DataUtils.getLoginUser(mActivity);
                    int uid = u.getID();
                    Intent intent = new Intent(mActivity, UserFavactivity.class);
                    intent.putExtra("UserID", uid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity_.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }
                break;
            case R.id.myposts:
                if (isLogin()) {
                    Intent intent = new Intent(mActivity, UserPostsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(mActivity, LoginActivity_.class);
                    startActivityForResult(intent1, REQUEST_CODE_LOGIN);
                }
                break;

            case R.id.myjf:
                if (isLogin()) {
                    Intent intentmyjf = new Intent(mActivity, UserJifenActivity.class);
                    startActivity(intentmyjf);
                } else {
                    Intent intent1 = new Intent(mActivity, LoginActivity_.class);
                    startActivityForResult(intent1, REQUEST_CODE_LOGIN);
                }
                break;

            case R.id.mycar:
                if (isLogin()) {
                    Intent intentmycar = new Intent(mActivity, UserMycarActivity.class);
                    startActivity(intentmycar);
                } else {
                    Intent intent1 = new Intent(mActivity, LoginActivity_.class);
                    startActivityForResult(intent1, REQUEST_CODE_LOGIN);
                }
                break;
        }
    }


    private void request() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserID", DataUtils.getLoginUser(mActivity).getID() + "");
        params.put("page", mCurrentPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        requestGet(Constants.GET_FAV, params, MyFavList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MyFavList gl = (MyFavList) result;
                List<Article> list = new ArrayList<Article>();
                if (gl != null) {
                    final int size = gl.getList().size();
                    for (int index = 0; index < size; index++) {
                        MyFav fav = gl.getList().get(index);
                        Article n = fav.getNews();
                        n.setFavID(fav.getID());
                        list.add(n);
                    }
                }
                mNewsList.addAll(list);
                mFavAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(mNewsList.size() >= gl.getTotalCount());
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            setUserLogin();
        } else if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            setUserLogin();
            request();
        } else if (PhotoDialog.REQUEST_TAKE == requestCode) {
            // 指定相机拍摄照片保存地址
            Uri path = mPhotoDialog.getPicUri();
            if (path != null && new File(mPhotoDialog.getPicPath()).exists()) {
                mPhotoDialog.cropImage(path);
            }
        } else if (PhotoDialog.REQUEST_CROP_PIC == requestCode) {
            // 裁剪返回
            Bitmap bitmap = data.getParcelableExtra("data");
            if (bitmap != null) {
                mLogoImageView.setImageBitmap(BitmapUtils.toRoundBitmap(bitmap));
                String path = mPhotoDialog.getSaveBitmapPath(bitmap);
                uploadFile(path);
            } else {
                showToast("请检查您的sd卡是否可以使用");
            }
        } else if (PhotoDialog.REQUEST_IMPORT == requestCode) {
            if (data != null) {
                mPhotoDialog.cropImage(data.getData());
            }
        } else if (PhotoDialog.REQUEST_CAMERA == requestCode) {
            if (data != null) {
                mPhotoDialog.cropImage(data.getData());
            }
        }
    }

    private void uploadFile(final String path) {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileInfo info = new FileInfo();
                info.setName("filename");
                info.setFile(new File(path));

                Map<String, String> params = new HashMap<String, String>();
                params.put("SiteCode", "avatar");
                params.put("UserID", mUserID);
                params.put("action", "userpic");
                String imageUrl = UploadImageManager.httpPost(Constants.UPLOAD_PIC, params,
                        info);
                if (!TextUtils.isEmpty(imageUrl)) {
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
            dismisProgressDialog();
            switch (msg.what) {
                case 1:
                    showToast("上传成功!");
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
                            showToast("上传成功!");
                        }
                    });
                    tipDialog.show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        setUserLogin();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (DELAY_TIME + mDelayTime < System.currentTimeMillis()) {
            showToast("再按一次退出" + getString(R.string.app_name));
            mDelayTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


}
