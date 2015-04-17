package com.xmyunyou.wcd.ui.car;

import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.ui.adapter.ArticleAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_news_list)
public class NewsListActivity extends BaseActivity {

    public static final String PARAMS_NEWS_ID = "news_id";
    public static final String PARAMS_NEWS_TITLE = "news_title";

    @Extra(PARAMS_NEWS_ID) int mCategoryID;
    @Extra(PARAMS_NEWS_TITLE) String mTitle;

    @ViewById(R.id.common_title)
    TextView mTitleTextView;
    @ViewById(R.id.news_list)
    LoadMoreListView mListView;

    private List<Article> mArticleList;
    private ArticleAdapter mArticleAdapter;

    private int mPage = 1;

    @AfterViews void buildComponent(){
        mTitleTextView.setText(mTitle);
        mArticleList = new ArrayList<Article>();
        mArticleAdapter = new ArticleAdapter(mActivity, mArticleList);
        mListView.setAdapter(mArticleAdapter);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage ++;
                requestNewsList();
            }
        });

        requestNewsList();
    }

    private void requestNewsList(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("categoryid", mCategoryID + "");
        params.put("page", mPage + "");
        params.put("size", Constants.PAGE_SIZE);

        requestGet(Constants.ARTICLE_CATEGORY_LIST, params, ArticleList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                mArticleList.addAll(al.getList());
                mArticleAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(mArticleList.size() >= al.getTotalCount());
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }


}
