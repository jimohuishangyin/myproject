package com.xmyunyou.wcd.ui.circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.json.SquareList;
import com.xmyunyou.wcd.ui.CarCircleActivity_;
import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.ui.view.ShowGridView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_square)
public class SquareFragment extends Fragment {

    private CarCircleActivity_ mActivity;

    @ViewById(R.id.square_scroll)
    MyScrollView mScrollView;
    @ViewById(R.id.square_grid_view)
    LoadMoreGridView mGridView;

    private List<Topic> mTopicList;
    private SquareAdapter mSquareAdapter;

    private int mScrollY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (CarCircleActivity_) getActivity();

        mTopicList = new ArrayList<>();
        mSquareAdapter = new SquareAdapter(mActivity, mTopicList);

        requestSquareList();

    }

    @AfterViews void buildComponent(){
        mGridView.setAdapter(mSquareAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic t = mSquareAdapter.getItem(position);
                DiscussDetailActivity_.intent(mActivity).mDiscussID(t.getID()).start();
            }
        });
    }

    @Click({R.id.square_integral, R.id.square_sort}) void onClick(View view){
        switch (view.getId()){
            case R.id.square_sort:

                break;
            case R.id.square_integral:
                break;
        }
    }


    private void requestSquareList(){
        mActivity.requestGet(Constants.CIRCLE_BBS_INDEX, null, SquareList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                SquareList sl = (SquareList) result;
                mTopicList.addAll(sl.getCommendTopics());
                mSquareAdapter.notifyDataSetChanged();
                mGridView.onLoadComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });

    }
}
