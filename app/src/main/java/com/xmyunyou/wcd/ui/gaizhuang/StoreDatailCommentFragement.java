package com.xmyunyou.wcd.ui.gaizhuang;

import android.app.ActionBar;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Gaizhuang_stop_comment;
import com.xmyunyou.wcd.model.json.Gaizhuang_shop_commentlist;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.ShopCommentAdapter;
import com.xmyunyou.wcd.ui.gaizhuang.adapter.storeDatailCommentAdapter;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/4/8.
 */
public class StoreDatailCommentFragement extends Fragment {
    public static final String PARAMS_SHOPID = "PARAMS_SHOPID";
    private String shopid;
    private GaizhuangStopDetailActivity mActivity;
    private LoadMoreListView mListView;
    private List<Gaizhuang_stop_comment> mCommentList;
    private storeDatailCommentAdapter mAdapter;
    private int mPage = 1;
    private boolean isClear = false;
    private TextView mCommentNumTextView;
    private LinearLayout mSubmitCommentLinearLayout;

    private PopupWindow mPopupWindow;
    private RatingBar mRattingBar;
    private RadioButton mGoodRadioButton;
    private RadioButton mBadRadioButton;
    private EditText mContentEditText;
    private Button mSubmitButton;

    private int StarNum;
    //1表示好评，2表示差评
    private int PingJia;
    //输入内容
    private String content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (GaizhuangStopDetailActivity) getActivity();
        shopid = getArguments().getString(PARAMS_SHOPID);

        mCommentList = new ArrayList<Gaizhuang_stop_comment>();
        mAdapter = new storeDatailCommentAdapter(mActivity, mCommentList);

        requestShopComment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_store_datailcomment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (LoadMoreListView) getView().findViewById(R.id.gaizhuang_store_ditail_comment);
        mCommentNumTextView = (TextView) getView().findViewById(R.id.comment_num);
        mSubmitCommentLinearLayout = (LinearLayout) getView().findViewById(R.id.submit_comment);
        mSubmitCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindowComment(v);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isClear = false;
                mPage++;
                requestShopComment();
            }
        });
    }

    private void showPopWindowComment(View v) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater.inflate(R.layout.popwindow_comment, null);
        mRattingBar = (RatingBar) view.findViewById(R.id.RatingBar);
        mGoodRadioButton = (RadioButton) view.findViewById(R.id.store_good);
        mBadRadioButton = (RadioButton) view.findViewById(R.id.store_bad);
        mContentEditText = (EditText) view.findViewById(R.id.submit_content);
        mSubmitButton = (Button) view.findViewById(R.id.submit_button);
        mRattingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                StarNum = (int) mRattingBar.getRating();
            }
        });
        mGoodRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PingJia = 1;
                mGoodRadioButton.setBackgroundResource(R.drawable.store_detail_zan_selected);
                mBadRadioButton.setBackgroundResource(R.drawable.store_detail_bad);
            }
        });
        mBadRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PingJia = 2;
                mBadRadioButton.setBackgroundResource(R.drawable.store_detail_bad_selected);
                mGoodRadioButton.setBackgroundResource(R.drawable.store_detail_zan);
            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = mActivity.getEditTextStr(mContentEditText);
                if(StarNum ==0){
                    mActivity.showToast("请选择星级数");
                }else if(PingJia ==0){
                    mActivity.showToast("请为商家店铺做出评价");
                }else if(TextUtils.isEmpty(content) ){
                    mActivity.showToast("请输入内容");
                }else {
                    submitShopComment();
                }
            }
        });
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    //提交评论内容
    public void submitShopComment() {
        String name = DataUtils.getLoginUser(mActivity).getName();
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content", content);
        params.put("shopid", shopid + "");
        params.put("UserName", name);
        params.put("DeviceName", Globals.getDeviceName());
        params.put("From", "Android");
        params.put("PingJia", PingJia + "");
        params.put("Star", StarNum + "");
        params.put("DeviceID", Globals.getDeviceID(mActivity));
        mActivity.requestPost(Constants.GAIZHUANG_STOP_COMMENT, params, boolean.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                boolean b = (boolean) result;
                if (b) {
                    mActivity.showToast("提交成功");
                } else {
                    mActivity.showToast("提交失败");
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }


    //请求评论
    private void requestShopComment() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", mPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        params.put("shopid", shopid + "");
        mActivity.requestGet(Constants.GAIZHUANG_SHOP_COMMENT, params, Gaizhuang_shop_commentlist.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Gaizhuang_shop_commentlist c = (Gaizhuang_shop_commentlist) result;
                if (isClear) {
                    mCommentList.clear();
                }
                mCommentList.addAll(c.getList());
                mAdapter.notifyDataSetChanged();
                mCommentNumTextView.setText(c.getTotalCount() + "评");
                mListView.onLoadMoreComplete(mCommentList.size() == c.getTotalCount());
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }


}
