package com.xmyunyou.wcd.ui.user;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Regrade;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.ui.main.UpgradeDialog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.net.RequestListener;

/**
 * Created by 95 on 2015/1/13.
 */
public class UserSetingsActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_CHANGE = 1001;

    private RelativeLayout mPwdRelativeLayout;
    private RelativeLayout mVersionRelativeLayout;
    private RelativeLayout mOpinionRelativeLayout;
    private RelativeLayout mAboutRelativeLayout;
    private TextView mTitleTextView;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mPwdRelativeLayout = (RelativeLayout) findViewById(R.id.settings_pwd);
        if(isLogin()){
            mPwdRelativeLayout.setVisibility(View.VISIBLE);
        }else {
            mPwdRelativeLayout.setVisibility(View.GONE);
        }
        mVersionRelativeLayout = (RelativeLayout) findViewById(R.id.settings_version);
        mOpinionRelativeLayout = (RelativeLayout) findViewById(R.id.settings_opinion);
        mAboutRelativeLayout = (RelativeLayout) findViewById(R.id.settings_about);
        mCancelButton = (Button) findViewById(R.id.cancel_login);
        mPwdRelativeLayout.setOnClickListener(this);
        mVersionRelativeLayout.setOnClickListener(this);
        mOpinionRelativeLayout.setOnClickListener(this);
        mAboutRelativeLayout.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        mTitleTextView.setText("设置");

        if(isLogin()){
            mCancelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_pwd:
                if(isLogin()) {
                    Intent intent = new Intent(mActivity, UserPwdChangeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CHANGE);
                }else{
                    startActivity(new Intent(mActivity, LoginActivity_.class));
                }
                break;
            case R.id.settings_version:
                checkRegrade();
                break;
            case R.id.settings_opinion:
                startActivity(new Intent(mActivity, OpinionFeedbacksActivity.class));
                break;
            case R.id.settings_about:
                startActivity(new Intent(mActivity, SettingsAboutActivity.class));
                break;
            case R.id.cancel_login:
                new TipDialog(mActivity, "是否确认退出？").setOnSuccessListener(new TipDialog.OnSuccessListener() {
                    @Override
                    public void onSuccess(TipDialog dialog) {
                        DataUtils.clearCache(mActivity);
                        finish();
                    }
                }).show();
                break;

        }
    }

    //检测版本更新
    public void checkRegrade() {
        showProgressDialog();
        int reqClientCode = 0;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            reqClientCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        final int versionCode = reqClientCode;
        updateVersion(new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                dismisProgressDialog();
                final Regrade r = (Regrade) result;
                int romteCode = Integer.valueOf(r.getVersionCode());
                if (romteCode > versionCode) {
                    //发现新版本
                    new UpgradeDialog(mActivity, r.getUpdateInfo(), r.getNewVerDownload()).show();
                } else {
                    showToast("当前已经是最新版本！！！");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                dismisProgressDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHANGE && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
