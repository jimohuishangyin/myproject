package com.xmyunyou.wcd.ui.search;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.ui.main.turing.TuringAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
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
 * Created by 95 on 2015/1/27.
 */
public class CareSearchFragment extends SearchBaseFragment {

    private LoadMoreView mListView;
    private List<Article> mArticleList;
    private TuringAdapter mTuringAdapter;
    private int mPage = 1;
    private int mID;
    private SearchCarActivity mActivity;
    private boolean isLoadData = false;
    private int mHeight;
    private int statusBarHeight;
    private int mListviewHeigeht;
    private int mTitleHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SearchCarActivity) getActivity();
        mID = getArguments().getInt("model", 0);
        mArticleList = new ArrayList<Article>();
        mTuringAdapter = new TuringAdapter(mActivity, mArticleList);
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        mHeight = display.getHeight();
        mTitleHeight=mActivity.dip2px(100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_turn, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (LoadMoreView) getView().findViewById(R.id.search_turing_list);
        mListView.setAdapter(mTuringAdapter);
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                Article a = mTuringAdapter.getItem(position);
                mActivity.startNewsDetail(a);
            }
        });
    }


    //请求文章
    private void requestArticle() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("CategoryID", "2");
        params.put("CarModelID", "" + mID);
        params.put("page", "" + mPage);
        params.put("size", Constants.PAGE_SIZE);
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        mActivity.requestGet(Constants.SEARCH_CATALOGUE, params, ArticleList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArticleList al = (ArticleList) result;
                mArticleList.addAll(al.getList());
                mTuringAdapter.notifyDataSetChanged();
                isLoadData = mArticleList.size() == al.getTotalCount();
                mListView.onLoadComplete(isLoadData);
                mListviewHeigeht = Globals.getTotalHeightofListView(mListView.getListView(), isLoadData);
                //设置viewpager宽度
                final int rectHeight = mHeight -mTitleHeight-statusBarHeight;
                if (rectHeight > mListviewHeigeht) {
                    mActivity.setViewpageHeight(rectHeight);
                } else {
                    mActivity.setViewpageHeight(mListviewHeigeht);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    @Override
    public void pullData(boolean load) {
        if(load && !mArticleList.isEmpty()){
            if ((mHeight -mTitleHeight-statusBarHeight) > Globals.getTotalHeightofListView(mListView.getListView(), isLoadData)) {
                mActivity.setViewpageHeight((mHeight -mTitleHeight-statusBarHeight));
            } else {
                mActivity.setViewpageHeight(Globals.getTotalHeightofListView(mListView.getListView(), isLoadData));
            }
            return;
        }
        requestArticle();
    }

    @Override
    public void loadMoreData() {
        if (!isLoadData) {
            mPage++;
            isLoadData = true;
            requestArticle();
        }
    }
}
