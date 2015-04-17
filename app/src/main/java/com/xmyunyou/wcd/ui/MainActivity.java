package com.xmyunyou.wcd.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.json.MobileIndex;
import com.xmyunyou.wcd.ui.gaizhuang.MainGaizhuangActivity;
import com.xmyunyou.wcd.ui.user.UserCenterActivity;

/**
 * Created by 95 on 2015/3/16.
 */
public class MainActivity extends TabActivity {

    private TabHost mTabHost;
    private RadioGroup mMainRadioGroup;
    private RadioButton mNewsRadioButton, mFriendRadioButton, mGaizhuangRadioButton,mUserRadioButton;
    private MobileIndex mNEWS;
    private final static String TAB_TAG_NEWS = "tab_tag_news";
    private final static String TAB_TAG_FRIEND = "tab_tag_friend";
    private final static String TAB_TAG_GAIZHUANG = "tab_tag_gaizhuang";
    private final static String TAB_TAG_USER = "tab_tag_user";
    //传递初始化数据给MainTabActivity
    public static final String PARAMS_NEWS = "PARAMS_NEWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_tabs);
        mMainRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        Resources res  = mMainRadioGroup.getResources();

        Drawable mNews = res.getDrawable(R.drawable.selector_tab_news);
        mNews.setBounds(0,7,60,67);
        mNewsRadioButton = (RadioButton) findViewById(R.id.main_news);
        mNewsRadioButton.setCompoundDrawables(null,mNews,null,null);

        Drawable mFriend = res.getDrawable(R.drawable.selector_tab_friend);
        mFriend.setBounds(0,7,60,67);
        mFriendRadioButton = (RadioButton) findViewById(R.id.main_friend);
        mFriendRadioButton.setCompoundDrawables(null,mFriend,null,null);

        Drawable mGaizhuang = res.getDrawable(R.drawable.selector_tab_gaizhuang);
        mGaizhuang.setBounds(0,7,60,67);
        mGaizhuangRadioButton = (RadioButton) findViewById(R.id.main_gaizhuang);
        mGaizhuangRadioButton.setCompoundDrawables(null,mGaizhuang,null,null);

        Drawable mUser = res.getDrawable(R.drawable.selector_tab_user);
        mUser.setBounds(0,7,60,67);
        mUserRadioButton = (RadioButton)findViewById(R.id.main_user);
        mUserRadioButton.setCompoundDrawables(null,mUser,null,null);

        mNEWS = (MobileIndex) getIntent().getSerializableExtra(PARAMS_NEWS);
        mTabHost = getTabHost();
        //传递初始化数据给MainTabActivity
        Intent intent = new Intent(this, MainTabActivity_.class);
        intent.putExtra(MainTabActivity_.PARAMS_BOUTIQUE, mNEWS);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TAG_NEWS).setIndicator("0").setContent(intent));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TAG_FRIEND).setIndicator("1").setContent(new Intent(this, CarCircleActivity_.class)));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TAG_GAIZHUANG).setIndicator("2").setContent(new Intent(this, MainGaizhuangActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TAG_USER).setIndicator("3").setContent(new Intent(this, UserCenterActivity.class)));
        CheckListener mCheckRadio = new CheckListener();
        mMainRadioGroup.setOnCheckedChangeListener(mCheckRadio);




    }

    public class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.main_news:
                    mTabHost.setCurrentTab(0);
                    break;
                case R.id.main_friend:
                    mTabHost.setCurrentTab(1);
                    break;
                case R.id.main_gaizhuang:
                    mTabHost.setCurrentTab(2);
                    break;
                case R.id.main_user:
                    mTabHost.setCurrentTab(3);
                    break;
            }
        }
    }



}
