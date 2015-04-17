package com.xmyunyou.wcd.ui.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanmee on 2014/12/16.
 */
public class TipDialog extends Dialog implements View.OnClickListener {

    private String mTitle;
    private int mID;


    private TextView mTitleTextView;

    private BaseActivity mActivity;

    private Button mOkButton;
    private Button mCancelButton;

    public TipDialog(BaseActivity activity, String title, int sid) {
        super(activity, R.style.dialog_style);
        mActivity = activity;
        mTitle = title;
        mID = sid;

        setContentView(R.layout.dialog_tips);

        mTitleTextView = (TextView) findViewById(R.id.dialog_title);
        mTitleTextView.setText("您确定移除收藏夹中的" + title + "吗？");
        mOkButton = (Button) findViewById(R.id.btn_ok);
        mOkButton.setOnClickListener(this);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(this);

    }

    public TipDialog(BaseActivity activity, String title) {
        super(activity, R.style.dialog_style);
        mActivity = activity;
        mTitle = title;

        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_tips);

        mTitleTextView = (TextView) findViewById(R.id.dialog_title);
        mTitleTextView.setText(title);
        mOkButton = (Button) findViewById(R.id.btn_ok);
        mOkButton.setOnClickListener(this);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(this);

    }

    public TipDialog setButtonOk(String text, int textColor, int bgColor){
        mOkButton.setText(text);
        mOkButton.setTextColor(mActivity.getResources().getColor(textColor));
        mOkButton.setBackgroundColor(mActivity.getResources().getColor(bgColor));
        return this;
    }

    public TipDialog setButtonCancel(String text, int textColor, int bgColor){
        mCancelButton.setText(text);
        mCancelButton.setTextColor(mActivity.getResources().getColor(textColor));
        mCancelButton.setBackgroundColor(mActivity.getResources().getColor(bgColor));
        return this;
    }

    public TipDialog setButtonOk(String text){
        mOkButton.setText(text);
        return this;
    }

    public TipDialog setButtonCancel(String text){
        mCancelButton.setText(text);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (mID > 0) {
                    deleteFav();
                } else {
                    if (mListener != null) {
                        mListener.onSuccess(TipDialog.this);
                    }
                }
                break;
            case R.id.btn_cancel:
                if(mOnCancelListener != null){
                    mOnCancelListener.onCancel(this);
                }
                break;
        }
        dismiss();
    }

   private void deleteFav() {
        mActivity.showProgressDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put("IDs", mID + "");
        params.put("Password", DataUtils.getString(mActivity, DataUtils.USER_PWD));
        params.put("userid", DataUtils.getLoginUser(mActivity).getID() + "");
        mActivity.requestPost(Constants.DEL_FAV, params, Boolean.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                mActivity.dismisProgressDialog();
                Boolean b = (Boolean) result;
                if (b) {
                    mActivity.dismisProgressDialog();
                    mActivity.showToast("删除成功");
                    if (mListener != null) {
                        mListener.onSuccess(TipDialog.this);
                    }
                } else {
                    mActivity.showToast("删除失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.dismisProgressDialog();
                mActivity.showToast(errorMsg);
            }
        });
    }

    public interface OnSuccessListener {

        void onSuccess(TipDialog dialog);

    }

    private OnSuccessListener mListener;

    public TipDialog setOnSuccessListener(OnSuccessListener listener) {
        mListener = listener;
        return this;
    }

    private OnCancelListener mOnCancelListener;
}
