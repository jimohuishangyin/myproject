package com.xmyunyou.wcd.ui.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.UserDetail;
import com.xmyunyou.wcd.ui.circle.DiscussFragment_;
import com.xmyunyou.wcd.ui.circle.SignDailog;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.user.info.IntegralFragment;
import com.xmyunyou.wcd.ui.user.info.IntegralFragment_;
import com.xmyunyou.wcd.ui.user.info.SignFragment;
import com.xmyunyou.wcd.ui.user.info.SignFragment_;
import com.xmyunyou.wcd.ui.user.info.TopicFragment;
import com.xmyunyou.wcd.ui.user.info.TopicFragment_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.BitmapUtils;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_user_index)
public class UserIndexActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    public static final String PARAMS_USER = "USERS";

    @Extra(PARAMS_USER)
    User mUser;

    //用户信息
    @ViewById(R.id.user_index_name)
    TextView mNameTextView;
    @ViewById(R.id.user_index_logo)
    ImageView mLogoImageView;
    @ViewById(R.id.user_index_medal)
    LinearLayout mMedalLinearLayout;
    @ViewById(R.id.user_index_sign_text)
    TextView mSignNumTextView;

    @ViewById(R.id.user_index_cursor)
    ImageView mCursorImageView;
    @ViewById(R.id.user_index_topic)
    RadioButton mDiscussRadioButton;
    @ViewById(R.id.user_index_integral)
    RadioButton mIntegralRadioButton;
    @ViewById(R.id.user_index_sign)
    RadioButton mSignRadioButton;
    @ViewById(R.id.user_index_pager)
    ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private int mOffset;
    private int mCurrentIndex;



    @AfterViews void buildComponent(){
        setViewPagerScrollSpeed(mViewPager);

        mFragmentList = new ArrayList<>();

        /* type 1帖子，3积分，4签到 */

        TopicFragment tf = TopicFragment_.builder().build();
        mFragmentList.add(tf);

        IntegralFragment intf = IntegralFragment_.builder().build();
        mFragmentList.add(intf);

        SignFragment sf = SignFragment_.builder().build();
        mFragmentList.add(sf);

        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);

        //计算底部线条滚动距离
        mOffset = mScreenWidth / 3;
        mCursorImageView.getLayoutParams().width = mOffset;
        //设置用户信息
        setUserInfo();
    }

    private void setUserInfo(){
        mNameTextView.setText(mUser.getName());
        mSignNumTextView.setText("签到次数：" + mUser.getCheckInNums());
        ImageLoader.getInstance().loadImage(mUser.getAvatarUrl(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mLogoImageView.setImageBitmap(BitmapUtils.toRoundBitmap(bitmap));
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        //设置勋章
        if(!TextUtils.isEmpty(mUser.getMedals())){
            String[] medals = mUser.getMedals().split("\\,");
            for (int i = 0; i < medals.length; i++) {
                String str = medals[i];
                ImageView iv = createMedalImageView(str);
                mMedalLinearLayout.addView(iv);
            }
        }
    }

    public void requestUserDetail(int page, int type, RequestListener listener){
        String userid = RsaHelper.encryptDataFromStr(mUser.getID() + "", DataUtils.getString(mActivity, DataUtils.RSA_DATA));
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("type", type + "");
        params.put("UserID", userid);
        params.put("size", Constants.PAGE_SIZE);
        requestPost(Constants.USER_DETAIL, params, UserDetail.class, listener);
    }

    public ImageView createMedalImageView(String id){
        int width = dip2px(30);
        int height = dip2px(60);
        ImageView iv = new ImageView(mActivity);
        iv.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        iv.setPadding(5, 0, 5, 0);
        loadImg(Constants.getMedalBigPic(id), width, height, iv);
        return iv;
    }

    @Click({R.id.user_index_topic, R.id.user_index_integral, R.id.user_index_sign}) void mainTabClick (View v) {
        switch (v.getId()) {
            case R.id.user_index_topic:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.user_index_integral:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.user_index_sign:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    //计算viewpage高度
    public void setViewPageHeight(int totalHeight){
        Globals.log("dddddddddddsetViewPageHeight:" + totalHeight);
        mViewPager.getLayoutParams().height = totalHeight;

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
            TopicFragment_ tf = (TopicFragment_) mFragmentList.get(position);
            tf.loadMoreData();
            mDiscussRadioButton.performClick();
        }else if(position == 1){
            IntegralFragment_ cf = (IntegralFragment_) mFragmentList.get(position);
            cf.loadMoreData(true);
            mIntegralRadioButton.performClick();
        }else if(position == 2){
            SignFragment_ cf = (SignFragment_) mFragmentList.get(position);
            cf.loadMoreData(true);
            mSignRadioButton.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
