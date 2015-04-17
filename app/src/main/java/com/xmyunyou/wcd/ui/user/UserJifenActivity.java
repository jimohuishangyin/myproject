package com.xmyunyou.wcd.ui.user;

import android.os.Bundle;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.JiFenLog;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.model.json.JiFenLogList;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
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
public class UserJifenActivity extends BaseActivity {
    private BaseActivity mActivity;
    private TextView mJifenTextView;
    private MyScrollView myScrollView;
    private LoadMoreView mListView;
    private UserJifenAdapter mAdapter;
    private List<JiFenLog> mList;

    private int mCurrentPage = 1;
    private int load ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_jifen);
        mActivity = this;
        mList = new ArrayList<JiFenLog>();
        mAdapter = new UserJifenAdapter(mActivity,mList);
        buildcompotent();
        requestJifen();
    }

    private void buildcompotent(){
        mJifenTextView = (TextView) findViewById(R.id.user_jifen_TextView);
        User u = DataUtils.getLoginUser(mActivity);
        mJifenTextView.setText(u.getJiFen()+"");
        myScrollView = (MyScrollView) findViewById(R.id.MyscrollView);
        myScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if ((load+1)>=mCurrentPage) {
                    mCurrentPage++;
                    requestJifen();
                }
            }
        });
        mListView = (LoadMoreView) findViewById(R.id.user_jifen_lists);
        mListView.setAdapter(mAdapter);
    }

    private void requestJifen(){
        User u = DataUtils.getLoginUser(mActivity);
        int uid = u.getID();
        String userid = RsaHelper.encryptDataFromStr(uid + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid",userid);
        params.put("page", mCurrentPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        requestPost(Constants.MY_JIFEN_LIST,params, JiFenLogList.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                JiFenLogList jiFenLogList =(JiFenLogList)result;
                mList.addAll(jiFenLogList.getList());
                mAdapter.notifyDataSetChanged();
                boolean hasNext = mList.size() == jiFenLogList.getTotalCount();
                mListView.onLoadComplete(hasNext);
                load = (int)(jiFenLogList.getTotalCount()/Integer.valueOf(Constants.PAGE_SIZE));
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }
}
