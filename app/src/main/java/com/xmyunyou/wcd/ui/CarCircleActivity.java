package com.xmyunyou.wcd.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.TopicList;
import com.xmyunyou.wcd.ui.circle.DiscussFragment;
import com.xmyunyou.wcd.ui.circle.DiscussFragment_;
import com.xmyunyou.wcd.ui.circle.SendTopicActivity_;
import com.xmyunyou.wcd.ui.circle.SignDailog;
import com.xmyunyou.wcd.ui.circle.SquareFragment_;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 玩车圈
 */
@EActivity(R.layout.activity_car_circle)
public class CarCircleActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    public static final String PARAMS_GAME_ID = "GAME_ID";

    @ViewById(R.id.circle_cursor) ImageView mCursorImageView;
    @ViewById(R.id.circle_discuss) RadioButton mDiscussRadioButton;
    @ViewById(R.id.circle_show) RadioButton mShowRadioButton;
    @ViewById(R.id.circle_square) RadioButton mSquareRadioButton;
    @ViewById(R.id.circle_pager) ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private int mOffset;
    private int mCurrentIndex;


    @AfterViews void buildComponent(){
        setViewPagerScrollSpeed(mViewPager);

        mFragmentList = new ArrayList<>();

        SquareFragment_ sf = new SquareFragment_();
        mFragmentList.add(sf);

        //gameid=-1  改装讨论
        //gameid=-2  改装秀
        //gameid > 0 对应车型
        DiscussFragment show = new DiscussFragment_();
        show.setActivity(this, -2);
        mFragmentList.add(show);

        DiscussFragment discuss =  new DiscussFragment_();;
        discuss.setActivity(this, -1);
        mFragmentList.add(discuss);

        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 3;
        mCursorImageView.getLayoutParams().width = mOffset;
    }

    @Click({R.id.circle_discuss, R.id.circle_show, R.id.circle_square, R.id.circle_sign, R.id.circle_write_topic}) void mainTabClick (View v) {
        switch (v.getId()) {
            case R.id.circle_discuss:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.circle_show:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.circle_square:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.circle_sign:
                if(isLogin()) {
                    new SignDailog(this).show();
                }else{
                    LoginActivity_.intent(mActivity).start();
                }
                break;
            case R.id.circle_write_topic:
                SendTopicActivity_.intent(mActivity).start();
                break;
        }
    }

    /**
     * 帖子
     * @param gameID
     * @param page
     * @param listener
     */
    public void requestBbsData(int gameID, int page, RequestListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("GameID", gameID + "");
        params.put("page", "" + page);
        params.put("size", Constants.PAGE_SIZE);

        requestPost(Constants.TOPIC_LIST, params, TopicList.class, listener);
    }

    /**
     * 车型
     * @param listener
     */
    public void requestCarModel(RequestListener listener){
        Type type = new TypeToken<List<Article>>() {
        }.getType();
        mActivity.requestGet(Constants.SEARCH_LIST, null, type, listener);
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

        if(position == 0){
            mSquareRadioButton.performClick();
        }else if(position == 1){
            DiscussFragment_ cf = (DiscussFragment_) mFragmentList.get(position);
            cf.requestDisscussList(true);
            mShowRadioButton.performClick();
        }else if(position == 2){
            DiscussFragment_ cf = (DiscussFragment_) mFragmentList.get(position);
            cf.requestDisscussList(true);
            mDiscussRadioButton.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
