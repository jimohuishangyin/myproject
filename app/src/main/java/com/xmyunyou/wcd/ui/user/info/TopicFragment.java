package com.xmyunyou.wcd.ui.user.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.json.UserDetail;
import com.xmyunyou.wcd.ui.circle.DiscussDetailActivity_;
import com.xmyunyou.wcd.ui.user.UserIndexActivity;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_topic)
public class TopicFragment extends Fragment {

    private UserIndexActivity mActivity;

    @ViewById(R.id.user_detail_list)
    LoadMoreView mListView;

    private int mListviewHeigeht;

    private List<Topic> mTopicList;
    private TopicLogAdatper mLogAdapter;

    private int mPage = 1;

    private boolean mNextPage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (UserIndexActivity) getActivity();

        mTopicList = new ArrayList<>();
        mLogAdapter = new TopicLogAdatper(mActivity, mTopicList);

        requestData();
    }

    @AfterViews void buildComponent(){
        mListView.setAdapter(mLogAdapter);
        mListView.setOnItemLoadClickListener(new LoadMoreView.OnItemLoadClickListener() {
            @Override
            public void setOnItemLoadClick(int position, Object obj, View item) {
                DiscussDetailActivity_.intent(mActivity).mDiscussID(mLogAdapter.getItem(position).getID()).start();
            }
        });
    }

    public void loadMoreData(){
        mListviewHeigeht = Globals.getTotalHeightofListView(mListView.getListView(), mNextPage);
        if(mListviewHeigeht < 200){
            mActivity.setViewPageHeight(250);
        }else{
            mActivity.setViewPageHeight(mListviewHeigeht);
        }
    }

    private void requestData(){
        mActivity.requestUserDetail(mPage, 1, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                UserDetail ud = (UserDetail) result;
                mTopicList.addAll(ud.getTopics());
                mLogAdapter.notifyDataSetChanged();
                mNextPage = (mTopicList.size() != mPage * Constants.PAGE_SIZE_);
                mListView.onLoadComplete(mNextPage);

                mListviewHeigeht = Globals.getTotalHeightofListView(mListView.getListView(), mNextPage);
                if(mListviewHeigeht < 200){
                    mActivity.setViewPageHeight(250);
                }else{
                    mActivity.setViewPageHeight(mListviewHeigeht);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });

    }

}
