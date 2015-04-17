package com.xmyunyou.wcd.ui.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.FileInfo;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.circle.face.FaceRelativeLayout;
import com.xmyunyou.wcd.ui.dialog.PhotoDialog;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;
import com.xmyunyou.wcd.utils.net.UploadImageManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_send_topic)
public class SendTopicActivity extends BaseActivity {

    public static final int REQUEST_CODE_IMPORT = 1001;

    @ViewById(R.id.send_board)
    RadioGroup mBoardRadioGroup;
    @ViewById(R.id.send_board_gz)
    RadioButton mTuringRadioButton;
    @ViewById(R.id.send_board_ac)
    RadioButton mShowRadioButton;
    @ViewById(R.id.send_face_ll)
    FaceRelativeLayout mFaceRelativeLayout;
    @ViewById(R.id.send_content)
    EditText mContentEditText;
    @ViewById(R.id.insert_pic_preview)
    ImageView mPreviewImageView;

    @ViewById(R.id.send_title)
    EditText mTitleEditText;

    //默认值是-1
    private String mBoard = "-1";

    private String mPath;

    @SystemService
    InputMethodManager mInputMethodManager;

    private String mTitle;
    private String mContent;
    private String mImageUrl;
    //加密过后的userid
    private String mUserID;

    @AfterViews
    void buildComponent() {
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
        mBoardRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View v = group.findViewById(checkedId);
                mBoard = v.getTag().toString();
            }
        });
    }

    @Click({R.id.btn_send, R.id.insert_face, R.id.insert_pic})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (!isLogin()) {
                    LoginActivity_.intent(mActivity).start();
                    return;
                }
                final User user = DataUtils.getLoginUser(mActivity);
                String ras = DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA);
                Globals.log("dddddddddddddd:" + ras);
                mUserID = RsaHelper.encryptDataFromStr(user.getID() + "", ras);
                mTitle = getEditTextStr(mTitleEditText);
                mContent = getEditTextStr(mContentEditText);
                if (TextUtils.isEmpty(mTitle)) {
                    showToast("请输入标题");
                    return;
                }

                if (TextUtils.isEmpty(mContent)) {
                    showToast("请输入内容");
                    return;
                }

                showProgressDialog();
                if (TextUtils.isEmpty(mPath)) {
                    //发帖
                    sendTopic();
                } else {
                    //先上传图片，再发帖
                    uploadFile(mPath);
                }
                break;
            case R.id.insert_pic:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, REQUEST_CODE_IMPORT);
                break;
            case R.id.insert_face:
                mInputMethodManager.hideSoftInputFromWindow(
                        mContentEditText.getWindowToken(), 0);
                mFaceRelativeLayout.toggleFaceView();
                break;
        }
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
                    sendTopic();
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
                            sendTopic();
                        }
                    });
                    tipDialog.show();
                    break;
            }
        }
    };

    private void sendTopic() {
        Map<String, String> params = new HashMap<>();
        params.put("Content", mContent);
        params.put("GameID", mBoard + "");
        params.put("Title", mTitle);
        params.put("Pictures", mImageUrl);
        params.put("DeviceID", Globals.getDeviceID(mActivity));
        params.put("DeviceName", Globals.getDeviceName());
        params.put("UserID", mUserID);

        requestPost(Constants.CREATE_TOPIC, params, int.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                int finish = (int) result;
                if (finish > 0) {
                    showToast("发送成功");
                    finish();
                    setResult(RESULT_OK);
                } else if (finish == -2) {
                    showToast("发帖失败，您的设备已被禁止发帖");
                } else if (finish == -3) {
                    showToast("发帖失败，您所在网络的IP已被禁止发帖");
                } else if (finish == -4) {
                    showToast("您发帖的速度过快，30秒后再来。");
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
            mPreviewImageView.setImageBitmap(BitmapFactory.decodeFile(mPath));
        }
    }

    @Override
    public void onBackPressed() {
        if (mFaceRelativeLayout.getVisibility() == View.VISIBLE) {
            mFaceRelativeLayout.toggleFaceView();
        } else {
            super.onBackPressed();
        }
    }

}
