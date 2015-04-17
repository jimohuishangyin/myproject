package com.xmyunyou.wcd.ui.user.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.MyFavTopic;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.MyFavTopicList;
import com.xmyunyou.wcd.ui.user.UserFavactivity;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/4/10.
 */
public class FavPostsFragment extends Fragment {

    private UserFavactivity mActivity;
    private MyScrollView mMyScrollView;
    private LoadMoreView mListView;

    private FavPostsAdapter mFavPostsAdapter;
    private List<MyFavTopic> myFavTopicList;

    private int mPage =1;
    private int  uid;
    private String userid;
    private int load ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (UserFavactivity)getActivity();

        User u = DataUtils.getLoginUser(mActivity);
        uid = u.getID();
        userid = RsaHelper.encryptDataFromStr(uid + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));

        myFavTopicList = new ArrayList<MyFavTopic>();
        mFavPostsAdapter = new FavPostsAdapter(mActivity,myFavTopicList);
        requestTopic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userfav_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyScrollView = (MyScrollView)getView().findViewById(R.id.userfav_article_scrollview);
        mListView =(LoadMoreView) getView().findViewById(R.id.userfav_article_list);
        mListView.setAdapter(mFavPostsAdapter);
        mMyScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if ((load+1)>=mPage) {
                    mPage++;
                    requestTopic();
                }
            }
        });
    }

    public void requestTopic(){
        Map<String,String > params = new HashMap<String, String>();
        params.put("type",2+"");
        params.put("UserID", userid);
        params.put("page", mPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        mActivity.requestPost(Constants.GET_FAV,params, MyFavTopicList.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MyFavTopicList m = (MyFavTopicList) result;
                myFavTopicList.addAll(m.getList());
                mFavPostsAdapter.notifyDataSetChanged();
                boolean hasNext = myFavTopicList.size() == m.getTotalCount();
                mListView.onLoadComplete(hasNext);
                load = (int)(m.getTotalCount()/Integer.valueOf(Constants.PAGE_SIZE));
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }



}
