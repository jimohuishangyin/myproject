package com.xmyunyou.wcd.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Opinion;
import com.xmyunyou.wcd.model.json.OpinionList;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.RefreshListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/1/15.
 */
public class OpinionFeedbacksActivity extends BaseActivity {

    private LoadMoreListView mListView;
    private OpinionAdapter mOpinionAdater;
    private TextView mTitleTextView;
    private List<Opinion> mOpinionList;
    private int mCurrentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_opinionfeedback);
        findViewById(R.id.feedback).setVisibility(View.VISIBLE);
        mListView = (LoadMoreListView) findViewById(R.id.opinion_list);
        mTitleTextView = (TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("意见反馈");
        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SettingsOpinionActivity.class));
            }
        });
        mOpinionList = new ArrayList<Opinion>();
        mOpinionAdater = new OpinionAdapter(this, mOpinionList);
        mListView.setAdapter(mOpinionAdater);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mCurrentPage++;
                requestOpinion();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(mActivity, FeedBackDetailActivity.class);
                intent.putExtra(FeedBackDetailActivity.PARAMS_INFO_FEED_BACK, (java.io.Serializable) mOpinionAdater.getItem(position));
                startActivity(intent);
            }
        });
        requestOpinion();
    }

    private void requestOpinion() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Type", "0");
        params.put("page", mCurrentPage + "");
        params.put("size", Constants.PAGE_SIZE);
        params.put("Reply", "1");
        requestGet(Constants.OPINION_LIST, params, OpinionList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                OpinionList ol = (OpinionList) result;
                mOpinionList.addAll(ol.getList());
                mOpinionAdater.notifyDataSetChanged();
                mListView.onLoadMoreComplete(mOpinionList.size() == ol.getTotalCount());
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });
    }


}
