package com.xmyunyou.wcd.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.BrandCar;
import com.xmyunyou.wcd.model.BrandCarLine;
import com.xmyunyou.wcd.model.BrandCarLineType;
import com.xmyunyou.wcd.model.json.BrandCarLineTypeList;
import com.xmyunyou.wcd.ui.search.BrandCarAdapter;
import com.xmyunyou.wcd.ui.search.BrandCarLineAdapter;
import com.xmyunyou.wcd.ui.search.BrandCarLineTypeAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/1/22.
 */
public class BrandDialog extends Dialog {

    private int mID;
    private int mCarLineTypeID;
    private BaseActivity mActivity;

    //获取所以汽车的品牌
    private LoadMoreListView mListView;
    private List<BrandCar> mBrandCarList;
    private BrandCarAdapter mBrandCarAdapter;
    private TextView mTitleTextView;
    //获取某个品牌汽车系列
    private List<BrandCarLine> mBrandCarLineList;
    private BrandCarLineAdapter mBrandCarLineAdapter;

    //获取单个品牌汽车系列的车型
    private List<BrandCarLineType> mBrandCarLineTypeList;
    private BrandCarLineTypeAdapter mBrandCarLineTypeAdapter;

    private int mBrandID;
    private int mBrandCarID;
    private int mBrandCarTypeID;

    public BrandDialog(BaseActivity activity, int brandID) {
        super(activity, R.style.dialog_style);
        mActivity = activity;
        mBrandID = brandID;
        setContentView(R.layout.dialog_brandcar);
        mListView = (LoadMoreListView) findViewById(R.id.brandcar);
        mTitleTextView = (TextView) findViewById(R.id.dialog_title);
        mTitleTextView.setText("选择品牌");
        returnback();
        brandCar();
        initbrandCar();
    }


    public BrandDialog(BaseActivity activity, int brandCarID, int brandID) {
        super(activity, R.style.dialog_style);
        mActivity = activity;
        mBrandCarID = brandCarID;
        setContentView(R.layout.dialog_brandcar);
        mListView = (LoadMoreListView) findViewById(R.id.brandcar);
        mTitleTextView = (TextView) findViewById(R.id.dialog_title);
        mID = brandID;
        mTitleTextView.setText("选择车系");
        brandCarLine();
        returnback();
        intbrandCarLine();
    }

    public BrandDialog(BaseActivity activity,int CarLineTypeID,boolean b){
        super(activity, R.style.dialog_style);
        mActivity = activity;
        mCarLineTypeID =CarLineTypeID;
        setContentView(R.layout.dialog_brandcar);
        mListView = (LoadMoreListView) findViewById(R.id.brandcar);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOnBrandItemClick != null){
                    mOnBrandItemClick.OnBrandItemClick(mBrandCarLineTypeAdapter.getItem(position));
                    dismiss();
                }
            }
        });
        mTitleTextView = (TextView) findViewById(R.id.dialog_title);
        mTitleTextView.setText("选择车型");
        brandCarLineType();
        intbrandCarLineType();
    }

    private void returnback() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnBrandItemClick != null) {
                    if (mID > 0) {
                        if (!mBrandCarLineAdapter.isEmpty()) {
                            mOnBrandItemClick.OnBrandItemClick(mBrandCarLineAdapter.getItem(position));
                        }
                    } else {
                        if (!mBrandCarAdapter.isEmpty()) {
                            mOnBrandItemClick.OnBrandItemClick(mBrandCarAdapter.getItem(position));
                        }
                    }
                    dismiss();
                }
            }
        });
    }

    private void brandCar() {
        mBrandCarList = new ArrayList<BrandCar>();
        mBrandCarAdapter = new BrandCarAdapter(mActivity, mBrandCarList);
        mBrandCarAdapter.setBrandID(mBrandID);
        mListView.setAdapter(mBrandCarAdapter);
    }

    private void brandCarLine() {
        mBrandCarLineList = new ArrayList<BrandCarLine>();
        mBrandCarLineAdapter = new BrandCarLineAdapter(mActivity, mBrandCarLineList);
        mBrandCarLineAdapter.setBrandCarID(mBrandCarID);
        mListView.setAdapter(mBrandCarLineAdapter);
    }

    private void brandCarLineType(){
        mBrandCarLineTypeList = new ArrayList<BrandCarLineType>();
        mBrandCarLineTypeAdapter = new BrandCarLineTypeAdapter(mActivity,mBrandCarLineTypeList);
        mListView.setAdapter(mBrandCarLineTypeAdapter);
    }

    //获取所以汽车的品牌
    private void initbrandCar() {
        Map<String, String> params = new HashMap<String, String>();
        Type type = new TypeToken<List<BrandCar>>() {
        }.getType();
        mActivity.requestGet(Constants.SEARCH_ALL, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<BrandCar> gl = (List<BrandCar>) result;
                mBrandCarList.clear();
                mBrandCarList.addAll(gl);
                mBrandCarAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {
            }
        });
    }

    private OnBrandItemClickListener mOnBrandItemClick;

    public void setmOnBrandItemClick(OnBrandItemClickListener mOnBrandItemClick) {
        this.mOnBrandItemClick = mOnBrandItemClick;
    }

    public interface OnBrandItemClickListener {
        void OnBrandItemClick(Object car);
    }

    //获取某个品牌汽车系列
    private void intbrandCarLine() {
        Map<String, String> params = new HashMap<String, String>();
        Type type = new TypeToken<List<BrandCarLine>>() {
        }.getType();
        params.put("mID", mID + "");
        mActivity.requestGet(Constants.SEARCH_ALL_LINE, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<BrandCarLine> bcl = (List<BrandCarLine>) result;
                mBrandCarLineList.clear();
                mBrandCarLineList.addAll(bcl);
                mBrandCarLineAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {
            }
        });
    }

    //获取单个品牌汽车系列的车型
    private void intbrandCarLineType() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("CarModelID", mCarLineTypeID + "");
        mActivity.requestGet(Constants.SEARCH_ALL_LINE_TYPE, params, BrandCarLineTypeList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                BrandCarLineTypeList bclt= (BrandCarLineTypeList) result;
                mBrandCarLineTypeList.clear();
                mBrandCarLineTypeList.addAll(bclt.getList());
                mBrandCarLineTypeAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }

}