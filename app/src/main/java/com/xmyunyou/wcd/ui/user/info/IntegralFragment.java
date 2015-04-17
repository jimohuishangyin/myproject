package com.xmyunyou.wcd.ui.user.info;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Integral;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.json.UserDetail;
import com.xmyunyou.wcd.ui.user.UserIndexActivity;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_topic)
public class IntegralFragment extends Fragment {
    private UserIndexActivity mActivity;

    @ViewById(R.id.user_detail_list)
    LoadMoreView mListView;

    private List<Integral> mIntegralList;
    private IntegralAdapter mIntegralAdapter;

    private int mListviewHeigeht;

    private int mPage = 1;

    private boolean mNextPage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (UserIndexActivity) getActivity();

        mIntegralList = new ArrayList<>();
        mIntegralAdapter = new IntegralAdapter(mActivity, mIntegralList);

    }

    @AfterViews
    void buildComponent(){
        mListView.setAdapter(mIntegralAdapter);
    }

    public void loadMoreData(boolean isload){
        if(isload && !mIntegralList.isEmpty()){
            mListviewHeigeht = Globals.getTotalHeightofListView(mListView.getListView(), mNextPage);
            Globals.log("dddddddddddd：" + mListviewHeigeht);
            if(mListviewHeigeht < 200){
                mActivity.setViewPageHeight(250);
            }else{
                mActivity.setViewPageHeight(mListviewHeigeht);
            }
            return;
        }
        requestData();
    }

    private void requestData(){

        mActivity.requestUserDetail(mPage, 3, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                UserDetail ud = (UserDetail) result;
                mIntegralList.addAll(ud.getJiFenLog());
                mIntegralAdapter.notifyDataSetChanged();
                mNextPage = (mIntegralList.size() != mPage * Constants.PAGE_SIZE_);
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
