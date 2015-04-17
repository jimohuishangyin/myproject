package com.xmyunyou.wcd.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95 on 2015/2/10.
 */
public class ForgetPwdActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ((TextView) findViewById(R.id.common_title)).setText("找回密码");

        final EditText nameEditText = (EditText) findViewById(R.id.forget_name);
        final EditText emailEditText = (EditText) findViewById(R.id.forget_email);


        findViewById(R.id.btn_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getEditableText().toString();
                String email = emailEditText.getEditableText().toString();

                if(TextUtils.isEmpty(name)){
                    mActivity.showToast("请输入您的用户名");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mActivity.showToast("请输入您的邮箱");
                    return;
                }

                if(!Globals.isEmail(email)){
                    mActivity.showToast("您输入的邮箱格式误");
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("email", email);

                showProgressDialog();
                requestPost(Constants.SENDPASSWORD, params, Integer.class, new RequestListener() {
                    @Override
                    public void onSuccess(Object result) {
                        dismisProgressDialog();
                        Integer i = (Integer) result;
                        if(i == 1){
                            new TipDialog(mActivity, "密码重设成功，请到邮箱收取您的新密码").setButtonOk("确定", android.R.color.white, R.color.bg_title)
                                    .setButtonCancel("取消", R.color.dialog_text, R.color.bg_dialog)
                                    .setOnSuccessListener(new TipDialog.OnSuccessListener() {
                                        @Override
                                        public void onSuccess(TipDialog dialog) {
                                            dialog.dismiss();
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }).show();
                        }else{
                            String msg = "";
                            if(i == -1){
                                msg = "未设置密保邮箱，请联系QQ：2874453389 找回密码";
                            }else if(i == -2){
                                msg = "密保邮箱错误，请仔细想想，请重试";
                            }else if(i == -3){
                                msg = "重设密码失败，请重试";
                            }else{
                                msg = "发生未知错误，请稍后重试";
                            }

                            new TipDialog(mActivity, msg).setButtonOk("确定", android.R.color.white, R.color.bg_title)
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
            }
        });
    }
}
