package com.xmyunyou.wcd.ui.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.json.TopicList;
import com.xmyunyou.wcd.ui.CarCircleActivity;
import com.xmyunyou.wcd.ui.CarCircleActivity_;
import com.xmyunyou.wcd.ui.view.RefreshListView;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_discuss)
public class DiscussFragment extends Fragment {

    private CarCircleActivity mActivity;

    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.discuss_list)
    RefreshListView mListView;

    private List<Topic> mTopicList;
    private CircleAdapter mCircleAdapter;

    private int mCurrentPage = 1;
    private boolean isClear = false;

   // @FragmentArg(CarCircleActivity.PARAMS_GAME_ID)
    int mGameID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (CarCircleActivity) getActivity();
    }

    public void setActivity(CarCircleActivity activity, int gameID){
        mActivity = activity;
        mGameID = gameID;
        mTopicList = new ArrayList<>();
        mCircleAdapter = new CircleAdapter(mActivity, mTopicList);
    }

    @AfterViews void buildComponent(){
        mListView.setAdapter(mCircleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscussDetailActivity_.intent(mActivity).mDiscussID(mCircleAdapter.getItem(position).getID()).start();
                //DiscussDetailActivity_.intent(mActivity).mTopic(mCircleAdapter.getItem(position)).start();
/*
                Intent intent = new Intent(mActivity, DiscussDetailActivity_.class);
                intent.putExtra(DiscussDetailActivity_.PARAMS_DISCUSS_TID, mCircleAdapter.getItem(position).getID());
                startActivity(intent);*/
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mListView.setOnRefreshListener(new RefreshListView.onRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                isClear = false;
                mCurrentPage ++;
                requestDisscussList (false);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClear = true;
                mCurrentPage = 1;
                requestDisscussList (false);
            }
        });
    }

    /**
     * 改装讨论帖子
     */
    public void requestDisscussList(boolean isInit){
        if(isInit && !mTopicList.isEmpty()){
            return;
        }
        mActivity.requestBbsData(mGameID, mCurrentPage, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                TopicList tl = (TopicList) result;
                if(isClear){
                    mTopicList.clear();
                }
                mTopicList.addAll(tl.getList());
                mCircleAdapter.notifyDataSetChanged();
                boolean hasNext = mTopicList.size() == tl.getTotalCount();
                mListView.onRefreshComplete(hasNext);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }


}
