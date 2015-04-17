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
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 95 on 2015/1/12.
 */
public class RegisterActivity extends BaseActivity{

    private Button mRegisterButton;
    private EditText mNameEditText;
    private EditText mPwdEditText;
    private EditText mComfirmEditText;
    private EditText mEmailEditText;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ((TextView) findViewById(R.id.common_title)).setText("登录");
        mRegisterButton = (Button)findViewById(R.id.btn_register);
        mNameEditText = (EditText)findViewById(R.id.register_name);
        mPwdEditText = (EditText)findViewById(R.id.register_pwd);
        mComfirmEditText = (EditText)findViewById(R.id.register_confirm_pwd);
        mEmailEditText = (EditText)findViewById(R.id.register_email);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            register();
            }
        });
    }

    private void register(){
        final String name =mNameEditText.getEditableText().toString();
        String pwd = mPwdEditText.getEditableText().toString();
        String cpwd = mComfirmEditText.getEditableText().toString();
        final String email = mEmailEditText.getEditableText().toString();
//        用户名密码不能为空
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
            showToast("用户名或密码不能为空！");
            return;
        }
        if(TextUtils.isEmpty(cpwd)||!pwd.equals(cpwd)){
            showToast("您两次输入的密码不一样");
            return;
        }
        if(TextUtils.isEmpty(email)||!Globals.isEmail(email)){
            showToast("请慎重填写您的密保邮箱");
            return;
        }
        showProgressDialog();
        final String md5Pwd = Globals.Md5Encode(Globals.Md5Encode(pwd).toUpperCase()).toUpperCase();
        Map<String,String>params = new HashMap<String, String>();
        params.put("Name",name);
        params.put("Password",md5Pwd);
        params.put("Email",email);
        params.put("PushUserID", DataUtils.getString(mActivity, DataUtils.PushUserID));
        params.put("PushChannelID", DataUtils.getString(mActivity, DataUtils.PushChannelID));
        params.put("DeviceID", Globals.getDeviceID(mActivity));
        requestPost(Constants.USER_REGISTER,params,User.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {

                dismisProgressDialog();
                User u =(User) result;
                if(u!=null){
                    if(u.getID()>0){
                       u.setPassword(md5Pwd);
                        DataUtils.cacheUser(mActivity,u);
                        setResult(RESULT_OK);
                        finish();
                        showToast("注册成功");
                    }else if(u.getID()==-1){
                        mNameEditText.setText("");
                        showToast("这个名称太火被人抢先一步注册了，换个试试？");
                    }else if(u.getID()==-2){
                        showToast("您设置的密保邮箱已存在");
                    }else if(u.getID() == -5){
                        showToast("您今天已经注册过不能再注册了哦");
                    }else{
                        showToast("注册失败，再试试？");
                    }
                }else {
                    showToast("注册失败，再试试？");
                }



            }

            @Override
            public void onFailure(String errorMsg) {
                dismisProgressDialog();
                showToast("注册失败，请稍后再试");
            }
        });
    }
}
