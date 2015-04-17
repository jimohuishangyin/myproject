package com.xmyunyou.wcd.ui.user;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.BaseActivity;

/**
 * Created by 95 on 2015/1/14.
 */
public class SettingsAboutActivity extends BaseActivity {
    private TextView mVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_about);
        ((TextView) findViewById(R.id.common_title)).setText("关于");
        mVersionTextView = (TextView) findViewById(R.id.tv_version);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersionTextView.setText("版本号：" + info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
