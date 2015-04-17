package com.xmyunyou.wcd.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Banner;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.Regrade;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.model.json.MobileIndex;
import com.xmyunyou.wcd.ui.main.CareFragment;
import com.xmyunyou.wcd.ui.main.CareFragment_;
import com.xmyunyou.wcd.ui.main.CategoryDialog;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.main.GirlFragment;
import com.xmyunyou.wcd.ui.main.GirlFragment_;
import com.xmyunyou.wcd.ui.main.MessageFragment;
import com.xmyunyou.wcd.ui.main.TuringFragment;
import com.xmyunyou.wcd.ui.main.TuringFragment_;
import com.xmyunyou.wcd.ui.main.UpgradeDialog;
import com.xmyunyou.wcd.ui.main.VideoFragment;
import com.xmyunyou.wcd.ui.main.VideoFragment_;
import com.xmyunyou.wcd.ui.search.SearchActivity;
import com.xmyunyou.wcd.ui.user.LoginActivity;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.ui.user.UserCenterActivity;
import com.xmyunyou.wcd.ui.view.FixedSpeedScroller;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class MainTabActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final int DELAY_TIME = 2500;
    public static final String PARAMS_BOUTIQUE = "PARAMS_BOUTIQUE";
    private MobileIndex mMobileIndex;

    @ViewById(R.id.main_cursor)  ImageView mCursorImageView;
    @ViewById(R.id.main_turing)  RadioButton mTuringRadioButton;
    @ViewById(R.id.main_care) RadioButton mCareRadioButton;
    @ViewById(R.id.main_video) RadioButton mVideoRadioButton;
    @ViewById(R.id.main_girl) RadioButton mGirlRadioButton;
    @ViewById(R.id.main_message) RadioButton mMessageRadioButton;

    @ViewById(R.id.main_pager) ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private int mOffset;
    private int mCurrentIndex;

    private FragmentViewPagerAdapter mAdapter;

    //保存退出设置
    private long mDelayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        mMobileIndex =(MobileIndex)getIntent().getSerializableExtra(PARAMS_BOUTIQUE);
        //初始化类型数据
        if(WanApp.mCategoryList.isEmpty()){
            new CategoryDialog(this).show();
        }

        //版本更新
        checkRegrade();
        buildComponent();
    }

    private void buildComponent(){
        setViewPagerScrollSpeed();
        mViewPager.setOnPageChangeListener(this);

        mFragmentList = new ArrayList<Fragment>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        TuringFragment_ tf = new TuringFragment_();
        tf.setMobileIndex(mMobileIndex);
        mFragmentList.add(tf);

        CareFragment_ cf = new CareFragment_();
        mFragmentList.add(cf);

        VideoFragment_ vf = new VideoFragment_();
        mFragmentList.add(vf);

        GirlFragment_ gf = new GirlFragment_();
        mFragmentList.add(gf);

        MessageFragment mf = new MessageFragment();
        mFragmentList.add(mf);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 5;
        mCursorImageView.getLayoutParams().width = mOffset;
    }

    @Click({R.id.main_turing, R.id.main_care, R.id.main_video, R.id.main_girl,R.id.main_message}) void mainTabClick (View v) {
        switch (v.getId()) {
            case R.id.main_turing:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.main_care:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.main_video:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.main_girl:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.main_message:
                mViewPager.setCurrentItem(4);
                break;
        }
    }




    //检测版本更新
    public  void checkRegrade(){
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
                final Regrade r = (Regrade) result;
                int romteCode = Integer.valueOf(r.getVersionCode());
                if(romteCode > versionCode){
                    //发现新版本
                    new UpgradeDialog(mActivity, r.getUpdateInfo(), r.getNewVerDownload()).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }

    /**
     * 请求幻灯片方法
     *
     * @param listener
     */
    public void requestBanner( RequestListener listener){

        mActivity.requestGet(Constants.HOST_ALL_URL, null, MobileIndex.class, listener);
    }

    /*
    *
    * 请求首页改装的数据
    *
    * */

    public void requestArticleList(int page , Map<String,String> args,RequestListener listenter){
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", "" + page);
        params.put("size", Constants.PAGE_SIZE);
        if(args!=null){
            params.putAll(args);
        }
        requestGet(Constants.ARTICLE_LIST, params, ArticleList.class,listenter);
    }

    /*//用户点击事件
    @Click(R.id.main_user) void mainUser(){
        Intent intent = new Intent(mActivity, UserCenterActivity.class);
        startActivity(intent);
    }*/
    //搜索点击事件
    @Click(R.id.main_search) void mainSearch(){
        Intent searchIntent = new Intent(mActivity, SearchActivity.class);
        startActivity(searchIntent);
    }

    //滑动完成
    @Override
    public void onPageScrolled(int i, float v, int i2) {

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
            mTuringRadioButton.performClick();
        }else if(position == 1){
            CareFragment_ cf = (CareFragment_) mFragmentList.get(position);
            cf.requestArticle(true);
            mCareRadioButton.performClick();
        }else if(position == 2){
            VideoFragment_ vf = (VideoFragment_) mFragmentList.get(position);
            vf.requestArticle(true);
            mVideoRadioButton.performClick();
        }else if(position == 3){
            GirlFragment_ gf = (GirlFragment_) mFragmentList.get(position);
            gf.requestArticle(true);
            mGirlRadioButton.performClick();
        }else if(position == 4){
            MessageFragment mf = (MessageFragment) mFragmentList.get(position);
            mf.requestArticle(true);
            mMessageRadioButton.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private void setViewPagerScrollSpeed(){
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( mViewPager.getContext( ) );
            mScroller.set( mViewPager, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }

    @Override
    public void onBackPressed() {
        if(DELAY_TIME + mDelayTime < System.currentTimeMillis()){
            showToast("再按一次退出" + getString(R.string.app_name));
            mDelayTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }


}
