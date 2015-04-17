package com.xmyunyou.wcd.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.ui.MainTabActivity;
import com.xmyunyou.wcd.ui.adapter.ImageViewPagerAdapter;
import com.xmyunyou.wcd.ui.car.PicDetailActivity;
import com.xmyunyou.wcd.ui.car.PicDetailActivity_;
import com.xmyunyou.wcd.ui.main.girl.GirlPicAdapter;
import com.xmyunyou.wcd.ui.main.turing.TuringAdapter;
import com.xmyunyou.wcd.ui.view.AutoScrollViewPager;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_girl)
public class GirlFragment extends Fragment implements ViewPager.OnPageChangeListener ,SwipeRefreshLayout.OnRefreshListener{

    private MainTabActivity mActivity;

   /* @ViewById(R.id.girl_img_pager)
    AutoScrollViewPager mAutoScrollViewPager;
    @ViewById(R.id.girl_img_text)
    TextView mTitleTextView;
    @ViewById(R.id.girl_img_index)
    LinearLayout mIndexLinearLayout;*/
    @ViewById(R.id.girl_list)
    LoadMoreView mListView;
    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.girl_scrollview)
    MyScrollView mScrollView;

    /*private List<ImageView> mViewList;
    private ImageViewPagerAdapter mImageAdaprer;

    private List<Article> mBannerList;*/

    private List<Article> mArticleList;
    private GirlPicAdapter mPicAdapter;

    private int mIndex;

    //当前页
    private int mPage = 1;
    private boolean isLoadData = false;
    private boolean isRefresh = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainTabActivity) getActivity();

//        mBannerList = new ArrayList<Article>();

        /*mViewList = new ArrayList<ImageView>();
        mImageAdaprer = new ImageViewPagerAdapter(mViewList);*/

        mArticleList = new ArrayList<Article>();
        mPicAdapter = new GirlPicAdapter(mActivity, mArticleList);
    }

    @AfterViews void init() {
       /* mAutoScrollViewPager.setOnPageChangeListener(this);
        mAutoScrollViewPager.setAdapter(mImageAdaprer);
        mAutoScrollViewPager.setInterval(2500);
        mAutoScrollViewPager.startAutoScroll();*/

        mListView.setAdapter(mPicAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.bg_title);
        mSwipeRefreshLayout.setColorScheme(
                R.color.bg_title);
        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if (!isLoadData) {
                    mPage++;
                    isLoadData = true;
                    isRefresh = false;
                    requestArticle(false);
                }
            }
        });
    }

    /*private void requestBannerList() {
        //请求幻灯片
        mActivity.requestBanner(3, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                mBannerList.clear();
                mBannerList.addAll(al.getList());
                initData();
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    private void initData() {
        final int size = mBannerList.size();
        mViewList.clear();
        mIndexLinearLayout.removeAllViews();
        for (int i = 0; i < size; i++) {
            //创建imageView
            Article b = mBannerList.get(i);
            mViewList.add(createImageView(b));
            mImageAdaprer.notifyDataSetChanged();

            //设置文本
            mTitleTextView.setText(b.getTitle());

            //游标
            mIndexLinearLayout.addView(createIndexImageView(i));
        }
    }*/

    //请求文章
    public void requestArticle(boolean isInit) {
        if (isInit && !mArticleList.isEmpty()) {
            return;
        }

        /*if(mBannerList.isEmpty()){
            requestBannerList();
        }*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("CategoryParentID", "3");
        params.put("page", "" + mPage);
        params.put("size", "10");

        mActivity.requestGet(Constants.ARTICLE_LIST, params, ArticleList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                if (isRefresh) {
                    mArticleList.clear();
                }
                mArticleList.addAll(al.getList());
                mPicAdapter.notifyDataSetChanged();
                mListView.onLoadComplete(mArticleList.size() == al.getTotalCount());
                mSwipeRefreshLayout.setRefreshing(false);
                isLoadData = false;
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        isRefresh = true;
        requestArticle(false);
    }

    private ImageView createImageView(final Article a) {
        ImageView image = new ImageView(mActivity);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image.setLayoutParams(lp);
        image.setImageResource(R.drawable.default_icon);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startPicDetail(a);
            }
        });
        Picasso.with(mActivity).load(a.getImageUrlHD()).placeholder(R.drawable.default_icon).into(image);
        return image;
    }

    /**
     * 创建游标
     *
     * @param index
     * @return
     */
    private ImageView createIndexImageView(int index) {
        ImageView image = new ImageView(mActivity);
        image.setPadding(5, 0, 5, 0);
        image.setImageResource(R.drawable.selector_main_banner);
        if (index == 0) {
            mIndex = index;
            image.setSelected(true);
        } else {
            image.setSelected(false);
        }
        return image;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       /* mTitleTextView.setText(mBannerList.get(position).getTitle());
        View view = mIndexLinearLayout.getChildAt(position);
        if (view != null) {
            view.setSelected(true);
        }

        view = mIndexLinearLayout.getChildAt(mIndex);
        if (view != null) {
            view.setSelected(false);
        }
        mIndex = position;*/

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
