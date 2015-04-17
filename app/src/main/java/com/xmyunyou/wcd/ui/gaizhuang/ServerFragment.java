package com.xmyunyou.wcd.ui.gaizhuang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.Gaizhuang_services;
import com.xmyunyou.wcd.model.Services;
import com.xmyunyou.wcd.ui.adapter.ImageViewPagerAdapter;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.ServerAdapter;
import com.xmyunyou.wcd.ui.view.AutoScrollViewPager;
import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.ShowGridView;
import com.xmyunyou.wcd.ui.view.StopGridView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 95 on 2015/3/17.
 */
public class ServerFragment extends Fragment implements ViewPager.OnPageChangeListener {
    public static final String PARAMS_PRIVINCEID = "PARAMS_PRIVINCEID";
    public static final String PARAMS_CITYID = "PARAMS_CITYID";

    private MainGaizhuangActivity mActivity;
    private AutoScrollViewPager mAutoScrollViewPager;
    private TextView mTitleTextView;
    private LinearLayout mIndexLinearLayout;

    private List<ImageView> mViewList;
    private ImageViewPagerAdapter mImageAdaprer;
    private List<Article> mBannerList;
    private int mIndex;

    private List<Services> mServicesList;
    private ServerAdapter mServerAdapter;

    private ShowGridView mListView;

    private int PrivinceID;
    private int CityID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainGaizhuangActivity) getActivity();

        //顶部幻灯片
        mBannerList = new ArrayList<Article>();
        mViewList = new ArrayList<ImageView>();
        mImageAdaprer = new ImageViewPagerAdapter(mViewList);

        mServicesList =new ArrayList<Services>();
        mServerAdapter = new ServerAdapter(mActivity,mServicesList);

        PrivinceID = getArguments().getInt(PARAMS_PRIVINCEID,0);
        CityID = getArguments().getInt(PARAMS_CITYID,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_server, container, false);
    }

    public void setParam(int cityID, int provinceID){
        CityID = cityID;
        PrivinceID = provinceID;
        //重新加载数据
        mServicesList.clear();
        requestBannerList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAutoScrollViewPager = (AutoScrollViewPager) getView().findViewById(R.id.gaizhuang_img_pager);
        mTitleTextView = (TextView) getView().findViewById(R.id.gaizhuang_img_text);
        mIndexLinearLayout = (LinearLayout) getView().findViewById(R.id.gaizhuang_img_index);
        mListView = (ShowGridView)getView().findViewById(R.id.gaizhuang_server_list);
        mListView.setAdapter(mServerAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Services a = mServerAdapter.getItem(position);
                mActivity.startGaizhuangServer(a,PrivinceID,CityID);
            }
        });
        mAutoScrollViewPager.setOnPageChangeListener(this);
        mAutoScrollViewPager.setAdapter(mImageAdaprer);
        mAutoScrollViewPager.setInterval(2500);
        mAutoScrollViewPager.startAutoScroll();
        requestBannerList();

    }

    //请求幻灯片和数据分类的数据
    public void requestBannerList() {
        mActivity.requestGet(Constants.GAIZHUANG_SERVER, null, Gaizhuang_services.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_services HuanDengs = (Gaizhuang_services) result;
                mBannerList.clear();
                mBannerList.addAll(HuanDengs.getHuanDengs());
                initData();
                mServicesList.addAll(HuanDengs.getServices());
                mServerAdapter.notifyDataSetChanged();
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

    private ImageView createImageView(final Article a) {
        ImageView image = new ImageView(mActivity);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image.setLayoutParams(lp);
        image.setImageResource(R.drawable.default_icon);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                } else if (categoryID == 3) {
                    mActivity.startPicDetail(a);
                } else {
                    mActivity.startNewsDetail(a);
                }
            }
        });
        mActivity.loadImg(a.getImageUrlHD(), image);
        return image;
    }

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
