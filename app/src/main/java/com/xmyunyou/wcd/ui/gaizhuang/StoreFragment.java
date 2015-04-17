package com.xmyunyou.wcd.ui.gaizhuang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Gaizhuang_stop_next;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.model.json.Gaizhuang_store;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.StoreAdapter;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.StoreRecommendAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.ui.view.ShowGridView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/3/18.
 */
public class StoreFragment extends Fragment {
    public static final String PARAMS_PRIVINCEID = "PARAMS_PRIVINCEID";
    public static final String PARAMS_CITYID = "PARAMS_CITYID";
    public static final String PARAMS_CITYNAME = "PARAMS_CITYNAME";
    private MainGaizhuangActivity mActivity;
    private TextView mStoreRecommentText;
    private ShowGridView mShowGridView;

    private List<StopArticle> mStopRecommentList;
    private StoreRecommendAdapter mStoreRecommendAdapter;

    private List<StopArticle> mStopList;
    private StoreAdapter mStoreAdapter;
    private LoadMoreView mListView;

    private MyScrollView mMyScrollView;
    private int mPage = 1;
    private int load =1;

    private int PrivinceID;
    private int CityID;
    private String CityName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainGaizhuangActivity) getActivity();
        mStopRecommentList = new ArrayList<StopArticle>();
        mStoreRecommendAdapter = new StoreRecommendAdapter(mActivity, mStopRecommentList);
        mStopList = new ArrayList<StopArticle>();
        mStoreAdapter = new StoreAdapter(mActivity, mStopList);
        PrivinceID = getArguments().getInt(PARAMS_PRIVINCEID,0);
        CityID = getArguments().getInt(PARAMS_CITYID,0);
        CityName =getArguments().getString(PARAMS_CITYNAME)+"地区推荐商家";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_store, container, false);
    }

    public void setParam(int cityID, int provinceID,String cityName){
        CityID = cityID;
        PrivinceID = provinceID;
        CityName = cityName;
        mStoreRecommentText.setText(CityName.substring(0, CityName.length() - 1)+"地区推荐商家");
        //重新加载数据
        mStopRecommentList.clear();
        mStopList.clear();
        mPage=1;
        requestStopTop(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStoreRecommentText = (TextView) getView().findViewById(R.id.gaizhuang_storerecommend);
        mStoreRecommentText.setText(CityName);
        mShowGridView = (ShowGridView) getView().findViewById(R.id.gaizhuang_storerecommend_list);
        mListView = (LoadMoreView) getView().findViewById(R.id.gaizhuang_store_list);
        mMyScrollView = (MyScrollView) getView().findViewById(R.id.gaizhuang_store_scrollview);
        mMyScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if ((load+1)>=mPage) {
                    mPage++;
                    requestStopNext();
                }
            }

        });
        mListView.setAdapter(mStoreAdapter);
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                StopArticle a = mStoreAdapter.getItem(position);
                mActivity.startGaizhuangStopDetail(a);
            }
        });
    }


    //    请求商品顶部数据
    public void requestStopTop(boolean isInit) {
        if (isInit && !mStopList.isEmpty()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("ProvinceID", PrivinceID+"");
        params.put("CityID", CityID+"");
        mActivity.requestGet(Constants.GAIZHUANG_STOP, params, Gaizhuang_store.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_store TopShops = (Gaizhuang_store) result;
                mStopRecommentList.addAll(TopShops.getTopShops());
                mShowGridView.setNumColumns(mStopRecommentList.size());
                mShowGridView.setAdapter(mStoreRecommendAdapter);
                mShowGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StopArticle a = mStoreRecommendAdapter.getItem(position);
                        mActivity.startGaizhuangStopDetail(a);
                    }
                });
                mStoreRecommendAdapter.notifyDataSetChanged();
                mStopList.addAll(TopShops.getShops());
                mStoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    //请求店铺第二页开始的数据
    private void requestStopNext() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ProvinceID", PrivinceID+"");
        params.put("CityID", CityID+"");
        params.put("page", mPage + "");
        params.put("size", Constants.PAGE_SIZE);
        mActivity.requestGet(Constants.GAIZHUANG_STOP_NETX, params, Gaizhuang_stop_next.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_stop_next List = (Gaizhuang_stop_next) result;
                mStopList.addAll(List.getList());
                mStoreAdapter.notifyDataSetChanged();
                boolean nextPage = (mStopList.size()) == List.getTotalCount();
                mListView.onLoadComplete(nextPage);
                load = (int)(List.getTotalCount()/Integer.valueOf(Constants.PAGE_SIZE));
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });

    }
}
