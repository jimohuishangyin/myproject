package com.xmyunyou.wcd.ui.gaizhuang;

import android.graphics.Rect;
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
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.ProductAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.SearchBaseFragment;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/3/24.
 */
public class StoreDatailProductFragment extends Fragment {
    public static final String PARAMS_mStopArticle = "mStopArticle";
    private GaizhuangStopDetailActivity mActivity;
    private LoadMoreGridView mListView;
    private MyScrollView mScrollView;
    private List<ProductArticle> mProductActicleList;
    private ProductAdapter mProductAdapter;
    private int mpage = 1;
    private boolean isLoadData = false;
    private int provinceID;
    private int cityID;
    private int shopid;
    private int load =1;
    private TextView mTextView;
    private StopArticle mStopArticle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (GaizhuangStopDetailActivity) getActivity();
        mProductActicleList = new ArrayList<ProductArticle>();
        mProductAdapter = new ProductAdapter(mActivity, mProductActicleList);

        mStopArticle = (StopArticle) getArguments().getSerializable(PARAMS_mStopArticle);
        shopid = mStopArticle.getID();
        provinceID = mStopArticle.getProvinceID();
        cityID = mStopArticle.getCityID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_store_product, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScrollView =(MyScrollView) getView().findViewById(R.id.gaizhuang_store_detail_scrollview);
        mListView = (LoadMoreGridView) getView().findViewById(R.id.gaizhuang_store_detail_production);
        mTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_production_nodata);
        mListView.setAdapter(mProductAdapter);
        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if ((load+1)>=mpage) {
                    mpage++;
                    requestProductActicle(false);
                }
            }
        });

    }

    public  void requestProductActicle(boolean init) {
        if (init && !mProductActicleList.isEmpty()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("CityID", cityID+"");
        params.put("ProvinceID", provinceID+"");
        params.put("shopid", shopid+"");
        params.put("page", mpage + "");
        params.put("size", Constants.PAGE_SIZE);
        mActivity.requestGet(Constants.GAIZHUANG_PRODUCT, params, Gaizhuang_production.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_production p = (Gaizhuang_production) result;
                mProductActicleList.addAll(p.getList());
                mProductAdapter.notifyDataSetChanged();
                isLoadData = mProductActicleList.size() == p.getTotalCount();
                mListView.onLoadComplete(isLoadData);
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
