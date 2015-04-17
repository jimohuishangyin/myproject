package com.xmyunyou.wcd.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.user.Fragment.FavArticleFragment;
import com.xmyunyou.wcd.ui.user.Fragment.FavPostsFragment;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/4/9.
 */
public class UserFavactivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private RadioButton mArticleRadioButton;
    private RadioButton mPostsRadioButton;
    private ImageView mCursorImageView;
    private ViewPager mViewPager;
    private TextView mTitleTextView;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;

    private FavArticleFragment mFavArticleFragment;
    private FavPostsFragment mFavPostsFragment;

    private int mOffset;
    private int mCurrentIndex;

    private int uid;
    private Bundle mBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav);
        uid = getIntent().getIntExtra("UserID",0);
        init();
    }

    private void init(){
        mArticleRadioButton = (RadioButton) findViewById(R.id.fav_Article);
        mPostsRadioButton = (RadioButton) findViewById(R.id.fav_posts);
        mCursorImageView = (ImageView) findViewById(R.id.fav_cursor);
        mViewPager = (ViewPager) findViewById(R.id.fav_pager);
        mTitleTextView =(TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("我的收藏");

        mArticleRadioButton.setOnClickListener(this);
        mPostsRadioButton.setOnClickListener(this);

        mBundle = new Bundle();
        mBundle.putInt(FavArticleFragment.PARAM_USERID,uid);

        mViewPager.setOnPageChangeListener(this);
        mFragmentList = new ArrayList<Fragment>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mFavArticleFragment = new FavArticleFragment();
        mFavArticleFragment.setArguments(mBundle);
        mFragmentList.add(mFavArticleFragment);

        mFavPostsFragment = new FavPostsFragment();
        mFragmentList.add(mFavPostsFragment);


        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 2;
        mCursorImageView.getLayoutParams().width = mOffset;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fav_Article:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.fav_posts:
                mViewPager.setCurrentItem(1);
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
            mArticleRadioButton.performClick();
        } else if (position == 1) {
           mPostsRadioButton.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
