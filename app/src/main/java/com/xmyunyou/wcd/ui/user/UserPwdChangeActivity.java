package com.xmyunyou.wcd.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95 on 2015/1/15.
 */
public class UserPwdChangeActivity extends BaseActivity implements View.OnClickListener {

    private EditText mInitPwdEditText;
    private EditText mResetPwdEditText;
    private EditText mSurePwdEditText;
    private Button mChangeButton;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pwdchange);
        mInitPwdEditText = (EditText) findViewById(R.id.init_pwd);
        mResetPwdEditText = (EditText) findViewById(R.id.reset_pwd);
        mSurePwdEditText = (EditText) findViewById(R.id.sure_pwd);
        mChangeButton = (Button) findViewById(R.id.change_pwd);
        mChangeButton.setOnClickListener(this);
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("修改密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_pwd:
                String initpwd = mInitPwdEditText.getEditableText().toString();
                String resetpwd = mResetPwdEditText.getEditableText().toString();
                String surepwd = mSurePwdEditText.getEditableText().toString();
                if (TextUtils.isEmpty(initpwd)) {
                    showToast("请输入您现在正在使用的密码");
                    return;
                }
                if (TextUtils.isEmpty(resetpwd) || !resetpwd.equals(surepwd)) {
                    showToast("您两次输入的密码不一样！");
                    return;
                }
                mActivity.showProgressDialog();
                final String md5Pwd = Globals.Md5Encode(Globals.Md5Encode(initpwd).toUpperCase()).toUpperCase();
                final String md5NewPwd = Globals.Md5Encode(Globals.Md5Encode(resetpwd).toUpperCase()).toUpperCase();
                int uid = DataUtils.getLoginUser(mActivity).getID();
                String userid = RsaHelper.encryptDataFromStr(uid + "", DataUtils.getString(mActivity, DataUtils.RSA_DATA));
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", userid);
                params.put("OldPassword", md5Pwd);
                params.put("NewPassword", md5NewPwd);
                requestPost(Constants.RESETPWD, params, Integer.class, new RequestListener() {
                    @Override
                    public void onSuccess(Object result) {
                        dismisProgressDialog();
                        int i = (int) result;
                        if (i > 0) {
                            new TipDialog(mActivity, "密码修改成功，请用新密码重新登录").setButtonOk("确定", android.R.color.white, R.color.bg_title)
                                    .setButtonCancel("取消", R.color.dialog_text, R.color.bg_dialog)
                                    .setOnSuccessListener(new TipDialog.OnSuccessListener() {
                                        @Override
                                        public void onSuccess(TipDialog dialog) {
                                            dialog.dismiss();
                                            startActivity(new Intent(mActivity, LoginActivity_.class));
                                            DataUtils.clearCache(mActivity);
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }).show();
                        } else if (i == -1) {
                            new TipDialog(mActivity, "您输入的原密码有误").setButtonOk("确定", android.R.color.white, R.color.bg_title)
                                    .setButtonCancel("取消", R.color.dialog_text, R.color.bg_dialog)
                                    .setOnSuccessListener(new TipDialog.OnSuccessListener() {
                                        @Override
                                        public void onSuccess(TipDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            //showToast("");
                            new TipDialog(mActivity, "发生未知错误，请稍后再试").setButtonOk("确定", android.R.color.white, R.color.bg_title)
                                    .setButtonCancel("取消", R.color.dialog_text, R.color.bg_dialog)
                                    .setOnSuccessListener(new TipDialog.OnSuccessListener() {
                                        @Override
                                        public void onSuccess(TipDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        dismisProgressDialog();
                        showToast(errorMsg);
                    }
                });
                break;
        }
    }
}
