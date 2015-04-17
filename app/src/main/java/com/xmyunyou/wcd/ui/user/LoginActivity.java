package com.xmyunyou.wcd.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

import org.androidannotations.annotations.EActivity;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

@EActivity
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mLoginButton;
    private EditText mNameEditText;
    private EditText mPwdEditText;
    private TextView mForgetPwdTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((TextView) findViewById(R.id.common_title)).setText("登录");
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mNameEditText = (EditText) findViewById(R.id.login_name);
        mPwdEditText = (EditText) findViewById(R.id.login_pwd);
        mForgetPwdTextView = (TextView) findViewById(R.id.btn_find_pwd);
        mForgetPwdTextView.setVisibility(View.VISIBLE);
        findViewById(R.id.login_register).setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mForgetPwdTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.login_register:
                Intent regIntent = new Intent(mActivity, RegisterActivity.class);
                startActivityForResult(regIntent, 2);
                break;
            case R.id.btn_find_pwd:
                Intent intent = new Intent(mActivity, ForgetPwdActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

    }

    /*
    * 用户登录
    * */
    private void login() {
        String name = mNameEditText.getEditableText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("请输入用户名");
            return;
        }
        String pwd = mPwdEditText.getEditableText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        final String md5Pwd = Globals.Md5Encode(Globals.Md5Encode(pwd).toUpperCase()).toUpperCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("password", md5Pwd);
        params.put("PushUserID", DataUtils.getString(mActivity, DataUtils.PushUserID));
        params.put("PushChannelID", DataUtils.getString(mActivity, DataUtils.PushChannelID));
        params.put("DeviceID", Globals.getDeviceID(mActivity));
        showProgressDialog();

        requestPost(Constants.USER_LOGIN, params, User.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                dismisProgressDialog();
                User u = (User) result;
                if (u != null && u.getID() > 0) {
                    u.setPassword(md5Pwd);
                    DataUtils.cacheUser(mActivity, u);
                    showToast("登录成功");
                    finish();
                } else {
                    showToast("登录失败");
                }


            }

            @Override
            public void onFailure(String errorMsg) {
                dismisProgressDialog();
                showToast(errorMsg);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            finish();
        }else if(requestCode == 1 && resultCode == RESULT_OK){

        }
    }
}
