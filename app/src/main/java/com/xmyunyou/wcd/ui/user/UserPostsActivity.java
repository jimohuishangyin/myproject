package com.xmyunyou.wcd.ui.user;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.TopicList;
import com.xmyunyou.wcd.ui.circle.CircleAdapter;
import com.xmyunyou.wcd.ui.circle.DiscussDetailActivity_;
import com.xmyunyou.wcd.ui.view.RefreshListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/4/13.
 */
public class UserPostsActivity extends BaseActivity {

    private BaseActivity mActivity;
    private TextView mTitleTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RefreshListView mListView;

    private List<Topic> mTopicList;
    private CircleAdapter mMyPostsAdapter;

    private int mCurrentPage = 1;
    private boolean isClear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        mActivity = this;
        buildcompotent();
        requestPosts();
    }

    private void buildcompotent(){
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("我的帖子");
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        mListView = (RefreshListView) findViewById(R.id.posts_list);
        mTopicList = new ArrayList<>();
        mMyPostsAdapter= new CircleAdapter(mActivity, mTopicList);
        mListView.setAdapter(mMyPostsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscussDetailActivity_.intent(mActivity).mDiscussID(mMyPostsAdapter.getItem(position).getID()).start();

            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mListView.setOnRefreshListener(new RefreshListView.onRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                isClear = false;
                mCurrentPage ++;
                requestPosts();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClear = true;
                mCurrentPage = 1;
                requestPosts();
            }
        });
    }

    private void requestPosts(){
        User u = DataUtils.getLoginUser(mActivity);
        int uid = u.getID();
        String userid = RsaHelper.encryptDataFromStr(uid + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid",userid);
        params.put("page", mCurrentPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        requestPost(Constants.MY_POSTS_LIST, params, TopicList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                TopicList topicList = (TopicList) result;
                if(isClear){
                    mTopicList.clear();
                }
                mTopicList.addAll(topicList.getList());
                mMyPostsAdapter.notifyDataSetChanged();
                boolean hasNext = mTopicList.size() == topicList.getTotalCount();
                mListView.onRefreshComplete(hasNext);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });

    }
}
