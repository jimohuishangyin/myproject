package com.xmyunyou.wcd.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Products;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.ui.view.TextLabelView;
import com.xmyunyou.wcd.ui.view.TopScrollView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.SearchBaseFragment;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/2/6.
 */
public class SearchCarActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String PARAMS_ID = "PARAMS_ID";
    public static final String PARAMS_CAR_ID = "PARAMS_ID";
    public static final String PARAMS_TITLE = "PARAMS_TITLE";
    public static final String PARAMS_PIC = "PARAMS_PIC";


    private ImageView mSearchCarImageView;
    private TextView mTitleCarTextView;
    private ImageView mShareImageView;
    private TextLabelView mTextLabelView;

    private ImageView mCursorImageView;
    private ImageView mCursorImageView1;
    private ViewPager mViewPager;
    //目录
    private RadioButton mTuringRadioButton;
    private RadioButton mCareRadioButton;
    private RadioButton mVideoRadioButton;
    private RadioButton mGirlRadioButton;
    private RadioButton mMessageRadioButton;
    //隐藏目录
    private RadioButton mTuringRadioButton1;
    private RadioButton mCareRadioButton1;
    private RadioButton mVideoRadioButton1;
    private RadioButton mGirlRadioButton1;
    private RadioButton mMessageRadioButton1;

    //内容
    private TurnSearchFragment mTurnSearchFragment;
    private CareSearchFragment mCareSearchFragment;
    private GirlSearchFragment mGirlSearchFragment;
    private VideoSearchFragment mVideoSearchFragment;
    private MotorzineSearchFragment mMotorzineSearchFragment;


    //浮动
    private TopScrollView mScrollView;
    private LinearLayout mImageView;
    private LinearLayout mFlowLayout;

    private List<Fragment> mFragmentList;
    private int mOffset;
    private int mCurrentIndex;
    private String mTitle;
    private String mImageUrl;
    private FragmentViewPagerAdapter mAdapter;

    //车系ID
    int carLineID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcar);


        mScrollView = (TopScrollView) findViewById(R.id.search_car_scroll_view);
        mImageView = (LinearLayout)findViewById(R.id.pic_image_category);
        mFlowLayout = (LinearLayout)findViewById(R.id.search_category);
        mScrollView.listenerFlowViewScrollState(mImageView,mFlowLayout);
        mScrollView.scrollTo(0,0);
        mSearchCarImageView = (ImageView) findViewById(R.id.search_car_image);
        mTitleCarTextView = (TextView) findViewById(R.id.common_title);
        mShareImageView = (ImageView) findViewById(R.id.btn_share);
        mCursorImageView = (ImageView) findViewById(R.id.search_cursor);
        mCursorImageView1 = (ImageView) findViewById(R.id.search_cursor1);
        mViewPager = (ViewPager) findViewById(R.id.search_pager);
        mTuringRadioButton = (RadioButton) findViewById(R.id.search_turing);
        mCareRadioButton = (RadioButton) findViewById(R.id.search_care);
        mVideoRadioButton = (RadioButton) findViewById(R.id.search_video);
        mGirlRadioButton = (RadioButton) findViewById(R.id.search_girl);
        mMessageRadioButton = (RadioButton) findViewById(R.id.search_message);
        mTuringRadioButton1 = (RadioButton) findViewById(R.id.search_turing1);
        mCareRadioButton1 = (RadioButton) findViewById(R.id.search_care1);
        mVideoRadioButton1 = (RadioButton) findViewById(R.id.search_video1);
        mGirlRadioButton1 = (RadioButton) findViewById(R.id.search_girl1);
        mMessageRadioButton1 = (RadioButton) findViewById(R.id.search_message1);
        mTuringRadioButton.setOnClickListener(this);
        mCareRadioButton.setOnClickListener(this);
        mVideoRadioButton.setOnClickListener(this);
        mGirlRadioButton.setOnClickListener(this);
        mMessageRadioButton.setOnClickListener(this);
        mTuringRadioButton1.setOnClickListener(this);
        mCareRadioButton1.setOnClickListener(this);
        mVideoRadioButton1.setOnClickListener(this);
        mGirlRadioButton1.setOnClickListener(this);
        mMessageRadioButton1.setOnClickListener(this);
       mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                final int index = mViewPager.getCurrentItem();
                if(mCurrentIndex == index) {
                    ((SearchBaseFragment) mFragmentList.get(index)).loadMoreData();
                }
            }
        });
        mShareImageView.setVisibility(View.VISIBLE);
        mShareImageView.setOnClickListener(this);

        Intent params = getIntent();
        mTitle = params.getStringExtra(PARAMS_TITLE);
        carLineID = params.getIntExtra(PARAMS_ID, 0);
        mTitleCarTextView.setText(mTitle);
        mImageUrl = params.getStringExtra(PARAMS_PIC);
        mActivity.loadImg(mImageUrl, mSearchCarImageView);


        mTextLabelView = (TextLabelView) findViewById(R.id.search_model_label);
        mTextLabelView.setOnItemClick(new TextLabelView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String id = view.getTag().toString();
                String product = ((TextView) view).getText().toString();
                Intent intent = new Intent(mActivity, SearchProductsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("product", product);
                intent.putExtra("carLine", mTitle);
                intent.putExtra("carLineID", carLineID);
                startActivity(intent);
            }
        });
        buildComponent();
        requestProducts();
    }

    public void buildComponent() {
        mViewPager.setOnPageChangeListener(this);

        Bundle mBundle = new Bundle();
        mBundle.putInt("model", carLineID);
        mFragmentList = new ArrayList<Fragment>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mTurnSearchFragment = new TurnSearchFragment();
        mTurnSearchFragment.setArguments(mBundle);
        mFragmentList.add(mTurnSearchFragment);

        mCareSearchFragment = new CareSearchFragment();
        mCareSearchFragment.setArguments(mBundle);
        mFragmentList.add(mCareSearchFragment);

        mGirlSearchFragment = new GirlSearchFragment();
        mGirlSearchFragment.setArguments(mBundle);
        mFragmentList.add(mGirlSearchFragment);

        mVideoSearchFragment = new VideoSearchFragment();
        mVideoSearchFragment.setArguments(mBundle);
        mFragmentList.add(mVideoSearchFragment);

        mMotorzineSearchFragment = new MotorzineSearchFragment();
        mMotorzineSearchFragment.setArguments(mBundle);
        mFragmentList.add(mMotorzineSearchFragment);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 5;
        mCursorImageView.getLayoutParams().width = mOffset;
        mCursorImageView1.getLayoutParams().width = mOffset;
    }


    //请求配件标题
    private void requestProducts() {
        Map<String, String> params = new HashMap<String, String>();
        Type type = new TypeToken<List<Products>>() {
        }.getType();
        requestGet(Constants.SEARCH_CAR_PRODUCT, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<Products> p = (List<Products>) result;
                mTextLabelView.addTag(p);
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
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
        mCursorImageView1.startAnimation(animation);
        if(position == 0){
            mTuringRadioButton.performClick();
            mTuringRadioButton1.performClick();
        }else if(position == 1){
            mCareRadioButton.performClick();
            mCareRadioButton1.performClick();
        }else if(position == 2){
            mVideoRadioButton.performClick();
            mVideoRadioButton1.performClick();
        }else if(position == 3){
            mGirlRadioButton.performClick();
            mGirlRadioButton1.performClick();
        }else if(position == 4){
            mMessageRadioButton.performClick();
            mMessageRadioButton1.performClick();
        }

        ((SearchBaseFragment) mFragmentList.get(position)).pullData(true);
    }

    public void setViewpageHeight(int height){
        mViewPager.getLayoutParams().height = height;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_turing:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.search_care:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.search_video:
                mViewPager.setCurrentItem(2);
                break;

            case R.id.search_girl:
                mViewPager.setCurrentItem(3);
                break;

            case R.id.search_message:
                mViewPager.setCurrentItem(4);
                break;
            case R.id.search_turing1:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.search_care1:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.search_video1:
                mViewPager.setCurrentItem(2);
                break;

            case R.id.search_girl1:
                mViewPager.setCurrentItem(3);
                break;

            case R.id.search_message1:
                mViewPager.setCurrentItem(4);
                break;
            case R.id.btn_share:
                String url = "http://www.wanchezhijia.com/car/" + carLineID + ".html";
                String title = mTitle + "改装实战_改装/内饰/音响/动力/轮毂/外观/汽车DIY-玩车之家";
                shareContent(title, title, url, mImageUrl);
                break;

        }

    }
}
