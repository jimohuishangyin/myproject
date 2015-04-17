package com.xmyunyou.wcd.ui.gaizhuang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Gaizhuang_stop_next;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.StoreAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/3/26.
 */
public class GaizhuangServerActivity extends BaseActivity{

    public static final String PARAMS_ID = "PARAMS_ID";
    public static final String PARAMS_TITLE = "PARAMS_TITLE";

    public static final String PARAMS_PRIVINCEID = "PARAMS_PRIVINCEID";
    public static final String PARAMS_CITYID = "PARAMS_CITYID";
    private MyScrollView mScrollView;
    private LoadMoreView mListView;
    private List<StopArticle> mStopList;
    private StoreAdapter mStoreAdapter;
    private TextView mTitleTextView;

    private int ServersID;
    private int mPage=1 ;

    private int PrivinceID;
    private int CityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaizhuangserver);
        ServersID = getIntent().getIntExtra(PARAMS_ID,0);
        PrivinceID = getIntent().getIntExtra(PARAMS_PRIVINCEID,0);
        CityID = getIntent().getIntExtra(PARAMS_CITYID,0);
        init();
    }
    private void init(){
        mTitleTextView  = (TextView) findViewById(R.id.common_title);
        mTitleTextView.setText(getIntent().getStringExtra(PARAMS_TITLE));
        mScrollView = (MyScrollView) findViewById(R.id.gaizhuang_server_srcollview);
        mListView = (LoadMoreView) findViewById(R.id.gaizhuang_server_list);
        mStopList = new ArrayList<StopArticle>();
        mStoreAdapter = new StoreAdapter(mActivity, mStopList);
        mListView.setAdapter(mStoreAdapter);
        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                mPage++;
                requestServers();
            }
        });
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                StopArticle a = mStoreAdapter.getItem(position);
                mActivity.startGaizhuangStopDetail(a);
            }
        });
    requestServers();
    }
    //请求数据
    private void requestServers() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ProvinceID", PrivinceID+"");
        params.put("CityID", CityID+"");
        params.put("ShopServiceID",ServersID+"");
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
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });

    }

}
