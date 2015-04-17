package com.xmyunyou.wcd.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Banner;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.model.json.MobileIndex;
import com.xmyunyou.wcd.ui.MainTabActivity;
import com.xmyunyou.wcd.ui.adapter.ImageViewPagerAdapter;
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

@EFragment(R.layout.fragment_turing)
public class TuringFragment extends Fragment implements ViewPager.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener {

    private MainTabActivity mActivity;

    @ViewById(R.id.turing_img_pager)
    AutoScrollViewPager mAutoScrollViewPager;
    @ViewById(R.id.turing_img_text)
    TextView mTitleTextView;
    @ViewById(R.id.turing_img_index)
    LinearLayout mIndexLinearLayout;
    @ViewById(R.id.turing_list)
    LoadMoreView mListView;
    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.turing_scrollview)
    MyScrollView mScrollView;

    private List<ImageView> mViewList;
    private ImageViewPagerAdapter mImageAdaprer;

    private List<Article> mBannerList;
    private List<Article> mArticleList;
    private TuringAdapter mTuringAdapter;

    private int mIndex;

    //当前页
    private int mPage = 1;
    //是否加载中
    private MobileIndex mMobileIndex;
    private boolean isLoadData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainTabActivity) getActivity();
        //顶部幻灯片
        mBannerList = new ArrayList<Article>();
        mViewList = new ArrayList<ImageView>();
        mImageAdaprer = new ImageViewPagerAdapter(mViewList);
        //改装列表
        mArticleList = new ArrayList<Article>();
        mTuringAdapter = new TuringAdapter(mActivity, mArticleList);
    }

    public void setMobileIndex(MobileIndex mi) {
        mMobileIndex = mi;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAutoScrollViewPager.setOnPageChangeListener(this);
        mAutoScrollViewPager.setAdapter(mImageAdaprer);
        mAutoScrollViewPager.setInterval(2500);
        mAutoScrollViewPager.startAutoScroll();

        mListView.setAdapter(mTuringAdapter);
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                Article a = mTuringAdapter.getItem(position);
                mActivity.startNewsDetail(a);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(R.color.bg_title);
        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if (!isLoadData) {
                    mPage++;
                    isLoadData = true;
                    requestArticle();
                }
            }
        });
        if (mMobileIndex != null) {
            refreshData(mMobileIndex);
        } else {
            requestFirstArticle();
        }
    }

    private void buildComponent() {
        //请求幻灯片
        mActivity.requestBanner(new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MobileIndex HuanDengs = (MobileIndex) result;
                mBannerList.clear();
                mBannerList.addAll(HuanDengs.getHuanDengs());
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
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        requestFirstArticle();
    }

    //请求首页数据
    private void requestFirstArticle() {
        mActivity.requestGet(Constants.HOST_ALL_URL, null, MobileIndex.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MobileIndex Gaizhuangs = (MobileIndex) result;
                refreshData(Gaizhuangs);
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.dismisProgressDialog();
                mActivity.showToast(errorMsg);
            }
        });
    }

    private void refreshData(MobileIndex mi) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mArticleList.clear();
        }
        mArticleList.addAll(mi.getGaizhuangs());
        mTuringAdapter.notifyDataSetChanged();
        mListView.onLoadComplete(false);
        mSwipeRefreshLayout.setRefreshing(false);
        buildComponent();
    }


    //请求分页文章
    private void requestArticle() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("CategoryParentID", "1");
        mActivity.requestArticleList(mPage, params, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList ls = (ArticleList) result;
                mArticleList.addAll(ls.getList());
                mTuringAdapter.notifyDataSetChanged();
                boolean nextPage = (mArticleList.size() - Integer.valueOf(Constants.PAGE_SIZE)) == ls.getTotalCount();
                mListView.onLoadComplete(nextPage);
                isLoadData = false;
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
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
//                mActivity.startNewsDetail(a);

                int categoryID = 0;
                final int size = WanApp.mCategoryList.size();
                for (int index = 0; index < size; index++) {
                    Category c = WanApp.mCategoryList.get(index);
                    if (c.getID() == a.getCategoryID()) {
                        categoryID = c.getParentID();
                        break;
                    }
                }

                if (categoryID == 4) {
                    mActivity.startNewsDetail(a, 4);
                } else if(categoryID == 3){
                    mActivity.startPicDetail(a);
                }else{
                    mActivity.startNewsDetail(a);
                }
            }
        });
        mActivity.loadImg(a.getImageUrlHD(), image);
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
        mTitleTextView.setText(mBannerList.get(position).getTitle());
        View view = mIndexLinearLayout.getChildAt(position);
        if (view != null) {
            view.setSelected(true);
        }

        view = mIndexLinearLayout.getChildAt(mIndex);
        if (view != null) {
            view.setSelected(false);
        }
        mIndex = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
