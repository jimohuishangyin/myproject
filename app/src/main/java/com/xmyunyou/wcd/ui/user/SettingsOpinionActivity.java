package com.xmyunyou.wcd.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95 on 2015/1/14.
 */
public class SettingsOpinionActivity extends BaseActivity{
    private EditText mTilteEditText;
    private EditText mContentEditText;
    private Button mSubmitButton;
    private ImageView mBackImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_opinion);
        mTilteEditText = (EditText) findViewById(R.id.et_title);
        mContentEditText = (EditText) findViewById(R.id.et_content);
        mSubmitButton = (Button) findViewById(R.id.btn_submit);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedBack();
            }
        });
    }


    private void submitFeedBack(){
        String title = mTilteEditText.getEditableText().toString();
        String content = mContentEditText.getEditableText().toString();
        if(TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
            showToast("标题或者内容不能为空！");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("Name", title);
        params.put("Content", content);
        params.put("Type","0");
//        showProgressDialog();
        requestPost(Constants.OPINION_SUBMIT,params,Integer.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Integer i = (Integer) result;
                if(i>0){
                    startActivity(new Intent(mActivity,OpinionFeedbacksActivity.class));
                    mTilteEditText.setText("");
                    mContentEditText.setText("");
                    showToast("发布成功，我们已收到您的建议。");
                    finish();
                }else {
                    showToast("提交失败,请重试！！！");
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });


    }
}
