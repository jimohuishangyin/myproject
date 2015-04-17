package com.xmyunyou.wcd.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.ui.MainTabActivity;
import com.xmyunyou.wcd.ui.main.turing.MessageAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/2/5.
 */


public class MessageFragment extends Fragment implements ViewPager.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener {

    private MainTabActivity mActivity;

    private LoadMoreGridView mListView;

    private List<Article> mArticleList;
    private MessageAdapter mMessageAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private MyScrollView myScrollView;

    private boolean isLoadData = false;
    private boolean isRefresh = false;
    private int mPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainTabActivity) getActivity();
        mArticleList = new ArrayList<Article>();
        mMessageAdapter = new MessageAdapter(mActivity,mArticleList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (LoadMoreGridView) getView().findViewById(R.id.message_list);
        mListView.setAdapter(mMessageAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article a = mMessageAdapter.getItem(position);
                mActivity.startNewsDetail(a);
            }
        });
        myScrollView = (MyScrollView) getView().findViewById(R.id.message_scrollview);
        myScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
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
        mSwipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(
                R.color.bg_title);

        requestArticle(false);
    }


    //请求文章列表
    public void requestArticle(boolean isInit){

        if (isInit && !mArticleList.isEmpty()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("CategoryParentID", "5");
        params.put("page", "" + mPage);
        params.put("size", Constants.PAGE_SIZE);
        mActivity.requestGet(Constants.ARTICLE_LIST, params, ArticleList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                if (isRefresh) {
                    mArticleList.clear();
                }
                mArticleList.addAll(al.getList());
                mMessageAdapter.notifyDataSetChanged();
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRefresh() {
        mPage = 1;
        isRefresh = true;
        requestArticle(false);
    }
}
