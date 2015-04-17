package com.xmyunyou.wcd.ui.gaizhuang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.ui.view.TopScrollView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.SearchBaseFragment;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/3/24.
 */
public class GaizhuangStopDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String PARAMS_STOP_ID = "STOP_ID";

    private TextView mTitleTextView;
    private int stopID;
    private StopArticle mStopArticle;
    private ImageView btnshare;

    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private ImageView mCursorImageView;
    private RadioButton mDataRadioButton;
    private RadioButton mInfoRadioButton;
    private RadioButton mProductRadioButton;
    private RadioButton mCommentRadioButton;

    private StoreDatailDataFragment mStoreDatailDataFragment;
    private StoreDatailInfoFragment mStoreDatailInfoFragment;
    private StoreDatailProductFragment mStoreDatailProductFragment;
    private StoreDatailCommentFragement mStoreDatailCommentFragement;

    private int mOffset;
    private int mCurrentIndex;

    private Bundle mBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaizhuangstop_detail);
        Intent param = getIntent();
        stopID = param.getIntExtra(PARAMS_STOP_ID, 0);
        requestDetail();
        init();
    }

    private void init() {
        btnshare = (ImageView) findViewById(R.id.btn_share);
        btnshare.setVisibility(View.VISIBLE);
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("商家详情");
        mViewPager = (ViewPager) findViewById(R.id.gaizhuang_store_detail_pager);
        mCursorImageView = (ImageView) findViewById(R.id.gaizhuang_store_detail_cursor);
        mDataRadioButton = (RadioButton) findViewById(R.id.gaizhuang_store_detail_data);
        mInfoRadioButton = (RadioButton) findViewById(R.id.gaizhuang_store_detail_info);
        mProductRadioButton = (RadioButton) findViewById(R.id.gaizhuang_store_detail_product);
        mCommentRadioButton = (RadioButton) findViewById(R.id.gaizhuang_store_detail_comment);
        mDataRadioButton.setOnClickListener(this);
        mInfoRadioButton.setOnClickListener(this);
        mProductRadioButton.setOnClickListener(this);
        mCommentRadioButton.setOnClickListener(this);
        btnshare.setOnClickListener(this);
        //计算底部线条滚动距离
        mOffset = mScreenWidth / 4;
        mCursorImageView.getLayoutParams().width = mOffset;
    }

    private void buildComputent() {
        mViewPager.setOnPageChangeListener(this);

        mFragmentList = new ArrayList<Fragment>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mStoreDatailDataFragment = new StoreDatailDataFragment();
        mStoreDatailDataFragment.setArguments(mBundle);
        mFragmentList.add(mStoreDatailDataFragment);

        mStoreDatailInfoFragment = new StoreDatailInfoFragment();
        mStoreDatailInfoFragment.setArguments(mBundle);
        mFragmentList.add(mStoreDatailInfoFragment);

        mStoreDatailProductFragment = new StoreDatailProductFragment();
        mStoreDatailProductFragment.setArguments(mBundle);
        mFragmentList.add(mStoreDatailProductFragment);

        mStoreDatailCommentFragement = new StoreDatailCommentFragement();
        mStoreDatailCommentFragement.setArguments(mBundle);
        mFragmentList.add(mStoreDatailCommentFragement);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);


    }

    public void requestDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", stopID + "");
        mActivity.requestGet(Constants.GAIZHUANG_STOP_DETAILS, params, StopArticle.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                mStopArticle = (StopArticle) result;

                mBundle = new Bundle();
                mBundle.putSerializable(StoreDatailDataFragment.PARAMS_mStopArticle,mStopArticle);
                mBundle.putString(StoreDatailInfoFragment.PARAMS_ID,mStopArticle.getID()+"");
                mBundle.putSerializable(StoreDatailProductFragment.PARAMS_mStopArticle,mStopArticle);
                mBundle.putString(StoreDatailCommentFragement.PARAMS_SHOPID,mStopArticle.getID()+"");
                buildComputent();
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.gaizhuang_store_detail_data:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.gaizhuang_store_detail_info:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.gaizhuang_store_detail_product:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.gaizhuang_store_detail_comment:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.btn_share:
                String url = "http://www.wanchezhijia.com/store/"+ mStopArticle.getID()+".html";
                shareContent(mStopArticle.getName(), mStopArticle.getName(), url, mStopArticle.getImageUrl());
                break;
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
            mDataRadioButton.performClick();
        } else if (position == 1) {
            mInfoRadioButton.performClick();
        } else if (position == 2) {
            StoreDatailProductFragment sdpf = (StoreDatailProductFragment)mFragmentList.get(position);
            sdpf.requestProductActicle(true);
            mProductRadioButton.performClick();
        }else if(position ==3){

            mCommentRadioButton.performClick();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
