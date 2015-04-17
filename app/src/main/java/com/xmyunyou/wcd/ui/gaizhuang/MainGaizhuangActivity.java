package com.xmyunyou.wcd.ui.gaizhuang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.City;
import com.xmyunyou.wcd.model.Services;
import com.xmyunyou.wcd.ui.CityList.CityListActivity;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.user.UserCenterActivity;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.CityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/17.
 */
public class MainGaizhuangActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ImageView mCursorImageView;
    private RadioButton mServerRadioButton;
    private RadioButton mStoreRadioButton;
    private RadioButton mProductonRadioButton;
    private ViewPager mViewPager;
    private LinearLayout mArrowcityLinearLayout;
    private TextView mCityTextView;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private int mOffset;
    private int mCurrentIndex;
    private ServerFragment mServerFragment;
    private StoreFragment mStoreFragment;
    private ProductionFragment mProductionFragment;
    private BaseActivity mActivity;
    public static final int DELAY_TIME = 2500;
    private long mDelayTime;

    //百度定位
    public LocationClient mLocationClient = null;
    private String Province;
    private String City;

    private int PID;
    private int CID;
    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_gaizhuang);
        showProgressDialog();
        mActivity = this;
        mCityTextView = (TextView) findViewById(R.id.gaizhuang_city);
        mCursorImageView = (ImageView) findViewById(R.id.gaizhuang_cursor);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
                Province = bdLocation.getProvince();
                City = bdLocation.getCity();
                PID = mActivity.mCityManager.getProvinceId(Province.substring(0, Province.length() - 1));
                CID = mActivity.mCityManager.getCityId(City.substring(0, City.length() - 1));
                buildcomponent();
            }
        });
        setLocationOption();
        mLocationClient.start();
        init();
    }


    //设置相关参数
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }


    public void buildcomponent() {
        dismisProgressDialog();
        mCityTextView.setText(City);
        mBundle = new Bundle();
        mBundle.putInt(ServerFragment.PARAMS_PRIVINCEID, PID);
        mBundle.putInt(ServerFragment.PARAMS_CITYID, CID);
        mBundle.putInt(StoreFragment.PARAMS_PRIVINCEID, PID);
        mBundle.putInt(StoreFragment.PARAMS_CITYID, CID);
        mBundle.putString(StoreFragment.PARAMS_CITYNAME, City);
        mBundle.putInt(ProductionFragment.PARAMS_PRIVINCEID, PID);
        mBundle.putInt(ProductionFragment.PARAMS_CITYID, CID);
        mViewPager = (ViewPager) findViewById(R.id.gaizhuang_pager);
        mViewPager.setOnPageChangeListener(this);

        mFragmentList = new ArrayList<Fragment>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mServerFragment = new ServerFragment();
        mServerFragment.setArguments(mBundle);
        mFragmentList.add(mServerFragment);

        mStoreFragment = new StoreFragment();
        mStoreFragment.setArguments(mBundle);
        mFragmentList.add(mStoreFragment);

        mProductionFragment = new ProductionFragment();
        mProductionFragment.setArguments(mBundle);
        mFragmentList.add(mProductionFragment);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }

    private void init() {
        mCursorImageView = (ImageView) findViewById(R.id.gaizhuang_cursor);
        mServerRadioButton = (RadioButton) findViewById(R.id.gaizhuang_server);
        mStoreRadioButton = (RadioButton) findViewById(R.id.gaizhuang_store);
        mProductonRadioButton = (RadioButton) findViewById(R.id.gaizhuang_production);
        mArrowcityLinearLayout = (LinearLayout) findViewById(R.id.gaizhuang_arrowcity);
        mArrowcityLinearLayout.setOnClickListener(this);
        mServerRadioButton.setOnClickListener(this);
        mStoreRadioButton.setOnClickListener(this);
        mProductonRadioButton.setOnClickListener(this);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 3;
        mCursorImageView.getLayoutParams().width = mOffset;

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gaizhuang_server:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.gaizhuang_store:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.gaizhuang_production:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.gaizhuang_arrowcity:
                Intent intent = new Intent(MainGaizhuangActivity.this, CityListActivity.class);
                intent.putExtra("CityName", City);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1){
            if(resultCode == 2){
                City = intent.getStringExtra("City");
                PID = intent.getIntExtra("PrivinceID", 0);
                CID = intent.getIntExtra("CityID", 0);
                mCityTextView.setText(City);
                mServerFragment.setParam(CID, PID);
                mStoreFragment.setParam(CID,PID,City);
                mProductionFragment.setParam(CID,PID);
            }else if(resultCode ==3){
                City = intent.getStringExtra("City");
                PID = intent.getIntExtra("PrivinceID", 0);
                CID = intent.getIntExtra("CityID", 0);
                mCityTextView.setText(City);
                mServerFragment.setParam(CID, PID);
                mStoreFragment.setParam(CID,PID,City);
                mProductionFragment.setParam(CID,PID);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation animation = new TranslateAnimation(mOffset
                * mCurrentIndex, mOffset * position, 0, 0);
        mCurrentIndex = position;
        animation.setFillAfter(true);
        animation.setDuration(100);
        mCursorImageView.startAnimation(animation);
        if (position == 0) {
            mServerRadioButton.performClick();
        } else if (position == 1) {
            StoreFragment sf = (StoreFragment) mFragmentList.get(position);
            sf.requestStopTop(true);
            mStoreRadioButton.performClick();
        } else if (position == 2) {
            ProductionFragment pf = (ProductionFragment) mFragmentList.get(position);
            pf.requestProductActicle(true);
            mProductonRadioButton.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
