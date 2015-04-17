package com.xmyunyou.wcd.ui.gaizhuang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Gaizhuang_production;
import com.xmyunyou.wcd.model.ProductArticle;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.ProductAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/3/18.
 */
public class ProductionFragment extends Fragment {

    private MainGaizhuangActivity mActivity;

    private LoadMoreGridView mListView;

    private List<ProductArticle> mProductActicleList;
    private ProductAdapter mProductAdapter;
    private TextView mTextView;
    private MyScrollView myScrollView;

    private int mPage = 1;

    private int load =1;

    public static final String PARAMS_PRIVINCEID = "PARAMS_PRIVINCEID";
    public static final String PARAMS_CITYID = "PARAMS_CITYID";
    private int PrivinceID;
    private int CityID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainGaizhuangActivity) getActivity();
        mProductActicleList = new ArrayList<ProductArticle>();
        mProductAdapter = new ProductAdapter(mActivity, mProductActicleList);
        PrivinceID = getArguments().getInt(PARAMS_PRIVINCEID,0);
        CityID = getArguments().getInt(PARAMS_CITYID,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_production, container, false);
    }

    public void setParam(int cityID, int provinceID){
        CityID = cityID;
        PrivinceID = provinceID;
        //重新加载数据
        mProductActicleList.clear();
        mPage =1;
        requestProductActicle(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (LoadMoreGridView) getView().findViewById(R.id.gaizhuang_production_list);
        mListView.setAdapter(mProductAdapter);
        mTextView = (TextView)getView().findViewById(R.id.gaizhuang_production_nodata);
        myScrollView = (MyScrollView) getView().findViewById(R.id.gaizhuang_production_scrollview);
        myScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
                @Override
                public void onScrollBottomListener() {
                    if ((load+1)>=mPage) {
                        mPage++;
                        requestProductActicle(false);
                    }
                }
            });
    }


    public void requestProductActicle(boolean isInit) {
        if (isInit && !mProductActicleList.isEmpty()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("ProvinceID",PrivinceID+"");
        params.put("CityID", CityID+"" );
        params.put("page", "" +mPage);
        params.put("size", Constants.PAGE_SIZE);
        mActivity.requestGet(Constants.GAIZHUANG_PRODUCT, params, Gaizhuang_production.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_production p = (Gaizhuang_production) result;
                mProductActicleList.addAll(p.getList());
                mProductAdapter.notifyDataSetChanged();
                mListView.onLoadComplete(mProductActicleList.size() == p.getTotalCount());
                if(p.getTotalCount()==0){
                    mTextView.setVisibility(View.VISIBLE);
                }
                load = (int)(p.getTotalCount()/Integer.valueOf(Constants.PAGE_SIZE));
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

}
