package com.xmyunyou.wcd.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.ui.main.turing.TuringAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
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
 * Created by 95 on 2015/1/28.
 */
public class SearchProductsActivity extends BaseActivity {
    private TextView mCarLineTextView;
    private TextView mProductTextView;
    private ImageView mBackImageView;
    private LoadMoreView mListView;

    private List<Article> mArticleList;
    private TuringAdapter mTuringAdapter;
    private MyScrollView mScrollView;

    //当前页
    private int mPage = 1;
    private boolean isLoadData = false;
    int carLineID;
    String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        mCarLineTextView = (TextView) findViewById(R.id.search_carline);
        mProductTextView = (TextView) findViewById(R.id.search_car_produces);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mListView = (LoadMoreView) findViewById(R.id.search_car_produces_content);
        mScrollView = (MyScrollView) findViewById(R.id.search_scrollview);
        Intent intent = getIntent();
        mCarLineTextView.setText(intent.getExtras().getString("carLine"));
        mProductTextView.setText(intent.getExtras().getString("product"));
        carLineID = intent.getExtras().getInt("carLineID");
        productID = intent.getExtras().getString("id");
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mArticleList = new ArrayList<Article>();
        mTuringAdapter = new TuringAdapter(mActivity, mArticleList);
        mListView.setAdapter(mTuringAdapter);
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                Article a = mTuringAdapter.getItem(position);
                mActivity.startNewsDetail(a);
            }
        });
        mScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                    mPage++;
                    requestProduct();
            }
        });
        requestProduct();
    }

    //请求参数
    private void requestProduct() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("CarModelID", "" + carLineID);
        params.put("GaiZhuangJianID", productID);
        params.put("page", "" + mPage);
        params.put("size", Constants.PAGE_SIZE);

        requestGet(Constants.SEARCH_CAR_PRODUCT_RETURN, params, ArticleList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                mArticleList.addAll(al.getList());
                mTuringAdapter.notifyDataSetChanged();
                mListView.onLoadComplete(mArticleList.size() == al.getTotalCount());

            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

}
