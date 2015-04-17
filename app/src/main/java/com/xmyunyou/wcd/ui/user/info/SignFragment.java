package com.xmyunyou.wcd.ui.user.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.CheckIns;
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
public class SignFragment extends Fragment {

    private UserIndexActivity mActivity;

    @ViewById(R.id.user_detail_list)
    LoadMoreView mListView;

    private List<CheckIns> mSignList;
    private SignAdapter mSignAdapter;
    private int mPage = 1;

    private int mListviewHeigeht;

    private boolean mNextPage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (UserIndexActivity) getActivity();

        mSignList = new ArrayList<>();
        mSignAdapter = new SignAdapter(mActivity, mSignList);
    }

    @AfterViews
    void buildComponent(){
        mListView.setAdapter(mSignAdapter);
    }

    public void loadMoreData(boolean isload){
        if(isload && !mSignList.isEmpty()){
            mListviewHeigeht = Globals.getTotalHeightofListView(mListView.getListView(), mNextPage);
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
        mActivity.requestUserDetail(mPage, 4, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                UserDetail ud = (UserDetail) result;
                mSignList.addAll(ud.getMyCheckIns());
                mSignAdapter.notifyDataSetChanged();
                mNextPage = (mSignList.size() != mPage * Constants.PAGE_SIZE_);
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
