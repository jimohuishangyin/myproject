package com.xmyunyou.wcd.ui.circle;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.view.TextLabelView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanmee on 2015/4/8.
 */
public class SignDailog extends Dialog implements View.OnClickListener {

    private ImageView mLogoImageView;
    private TextView mNameTextView;
    private LinearLayout mMedalLinearLayout;
    private TextView mNumTextView;
    private TextView mDaysTextView;
    private LinearLayout mSignTextLinearlayout;
    private EditText mContentEditText;

    private BaseActivity mActivity;
    private User mUser;
    public SignDailog(BaseActivity context) {
        super(context, R.style.dialog_style);
        mActivity = context;

        mUser = DataUtils.getLoginUser(mActivity);

        buildComponent();

    }

    private void buildComponent(){
        setContentView(R.layout.dialog_sign);

        mLogoImageView = (ImageView) findViewById(R.id.sign_pic);
        mNameTextView = (TextView) findViewById(R.id.sign_name);
        mMedalLinearLayout = (LinearLayout) findViewById(R.id.sign_medal);
        mSignTextLinearlayout = (LinearLayout) findViewById(R.id.sign_text);
        mNumTextView = (TextView) findViewById(R.id.sign_num);
        mDaysTextView = (TextView) findViewById(R.id.sign_days);
        mContentEditText = (EditText) findViewById(R.id.sign_et);

        findViewById(R.id.sign_submit).setOnClickListener(this);
        findViewById(R.id.sign_cancel).setOnClickListener(this);

        bindData();
    }

    private void bindData(){
        mActivity.loadImg(mUser.getAvatarUrl(), mLogoImageView);
        mNameTextView.setText(mUser.getName());
        //设置勋章
        mActivity.createMedal(mUser.getMedals(), mMedalLinearLayout);
        String signTimes = "签到次数：" + mUser.getCheckInNums();
        mNumTextView.setText(mActivity.setSpannableText(signTimes, signTimes.indexOf("：") + 1, signTimes.length(), R.color.bg_title));

        String daysTimes = "累计签到：" + mUser.getCheckInNums() + "天";
        mDaysTextView.setText(mActivity.setSpannableText(daysTimes, daysTimes.indexOf("：") + 1, daysTimes.length() - 1, R.color.bg_title));

        mSignTextLinearlayout.removeAllViews();
        TextLabelView tagview = new TextLabelView(mActivity);
        tagview.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        tagview.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        tagview.setMargin(10);
        tagview.addTag(mActivity.getString(R.string.sign_text));
        tagview.setOnItemClick(new TextLabelView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                TextView tv = (TextView) view;
                String text = mContentEditText.getEditableText() + " "
                        + tv.getText();
                mContentEditText.setText(text);
                mContentEditText.setSelection(text.length());
            }
        });
        mSignTextLinearlayout.addView(tagview);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_submit:
                submitSignText();
                break;
            case R.id.sign_cancel:
                dismiss();
                break;
        }

    }

    private void submitSignText(){
        mActivity.showProgressDialog();
        Map<String, String> params = new HashMap<>();
        String userid = RsaHelper.encryptDataFromStr(mUser.getID() + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
        params.put("UserID", userid);
        params.put("Content", mContentEditText
                .getEditableText().toString());

        mActivity.requestPost(Constants.CREATE_SIGN, params, Integer.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                mActivity.dismisProgressDialog();
                int signID = (Integer) result;
                if (signID > 0) {
                    mActivity.showToast("签到成功，小手一抖，积分已到手。");
                    DataUtils.putInt(mActivity, "checkinnums", mUser.getCheckInNums() + 1);
                } else {
                    mActivity.showToast("今天你已经签到过了");
                }
                dismiss();
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast("太多人了，等会再试试吧");
            }
        });
    }
}
