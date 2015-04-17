package com.xmyunyou.wcd.ui.car;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.ArticleComment;
import com.xmyunyou.wcd.model.json.CommentList;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.RefreshListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends BaseActivity implements View.OnTouchListener {

    @ViewById(R.id.comment_list)
    LoadMoreListView mListView;
    @ViewById(R.id.common_title)
    TextView mTitleTextView;

    private int mCurrentPage = 1;
    private List<ArticleComment> mCommentList;
    private CommentAdapter mAdapter;
    @Extra(NewsDetailActivity.PARAMS_NEWS_ID)
    int mGoodID;

    private boolean isClear = false;
    //评论
    private ImageView mCommentBtnImageView;
    private EditText mContentEditText;


    @AfterViews
    void buildComponent() {
        mListView.setOnTouchListener(this);
        mTitleTextView.setText("用户评论");
        mCommentList = new ArrayList<>();
        mAdapter = new CommentAdapter(mActivity, mCommentList);
        mContentEditText = (EditText) findViewById(R.id.comment_et);
        mCommentBtnImageView = (ImageView) findViewById(R.id.btn_comment);
        mCommentBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    startActivity(new Intent(mActivity, LoginActivity_.class));
                } else {
                    final String content = getEditTextStr(mContentEditText);
                    if (TextUtils.isEmpty(content)) {
                        showToast("请输入内容");
                        return;
                    }

                    showProgressDialog();

                    final String name = DataUtils.getLoginUser(mActivity).getName();
                    Map<String, String> params = new HashMap<>();
                    params.put("Content", content);
                    params.put("NewsID", mGoodID + "");
                    params.put("UserName", name);
                    params.put("DeviceID", Globals.getDeviceID(mActivity));
                    params.put("DeviceName", Globals.getDeviceName());
                    params.put("From", "Android");

                    requestPost(Constants.CREATE_COMMENT, params, Boolean.class, new RequestListener() {
                        @Override
                        public void onSuccess(Object result) {
                            dismisProgressDialog();
                            Boolean b = (Boolean) result;
                            if (b) {
                                ArticleComment a = new ArticleComment();
                                a.setCreateDate(Globals.getCurrentDate());
                                a.setContent(content);
                                a.setUserName(DataUtils.getLoginUser(mActivity).getName());
                                mCommentList.add(a);
                                mAdapter.notifyDataSetChanged();
                                mContentEditText.setText("");
                                showToast("发表成功");
                            }
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            dismisProgressDialog();
                            showToast(errorMsg);
                        }
                    });
                }
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isClear = false;
                mCurrentPage++;
                requestComment();
            }
        });

        requestComment();
    }

    private void requestComment() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", mCurrentPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        params.put("NewsID", mGoodID + "");

        requestGet(Constants.COMMENT, params, CommentList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CommentList cl = (CommentList) result;
                if (isClear) {
                    mCommentList.clear();
                }
                mCommentList.addAll(cl.getList());
                mAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(mCommentList.size() == cl.getTotalCount());
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast("加载出错！！！");
            }
        });
    }


    private float mTempX;
    private float mEndX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTempX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = event.getRawX();
                if (mEndX - mTempX > 350) {
                    finish();
                }
                break;
        }
        return false;
    }

}
