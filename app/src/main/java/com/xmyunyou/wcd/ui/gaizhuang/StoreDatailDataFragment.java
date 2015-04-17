package com.xmyunyou.wcd.ui.gaizhuang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.utils.CityManager;
import com.xmyunyou.wcd.utils.Globals;

/**
 * Created by 95 on 2015/3/24.
 */
public class StoreDatailDataFragment extends Fragment {

    public static final String PARAMS_mStopArticle = "mStopArticle";
    private GaizhuangStopDetailActivity mActivity;
    private ImageView mPicImageView;
    private TextView mNameTextView;
    private TextView mQQTextView;
    private TextView mWeixinTextView;
    private TextView mTelTextView;
    private TextView mAddressTextView;
    private TextView mCategoryTextView;
    private TextView mWangzhiTextView;
    private TextView mDistrictTextView;
    private TextView mDescriptionTextView;
    private StopArticle mStopArticle;
    private int Category;
    private int ProvinceID;
    private int CityID;
    private int DistrictID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (GaizhuangStopDetailActivity) getActivity();
        mStopArticle = (StopArticle) getArguments().getSerializable(PARAMS_mStopArticle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_store_dataildata, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPicImageView = (ImageView)getView().findViewById(R.id.gaizhuang_store_detail_pic);
        mActivity.loadImg(mStopArticle.getImageUrl(),mPicImageView);
        mNameTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_name);
        mNameTextView.setText(mStopArticle.getName());
        mWangzhiTextView = (TextView)getView().findViewById(R.id.gaizhuang_store_detail_wangzhi);
        mWangzhiTextView.setText("http://www.wanchezhijia.com/store/"+mStopArticle.getID()+".html");
        mQQTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_qq);
        mQQTextView.setText("Q Q:"+mStopArticle.getQQ());
        mWeixinTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_weixin);
        mWeixinTextView.setText("微信："+mStopArticle.getWeixin());
        mTelTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_tel);
        mTelTextView.setText("电话："+mStopArticle.getTel());
        mDistrictTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_district);
        mAddressTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_address);
        mAddressTextView.setText("地址："+mStopArticle.getAddress());
        mCategoryTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_category);
        Category = mStopArticle.getCategoryID();
        if (Category == 4) {
            mCategoryTextView.setText("类别：改装店");
        } else if (Category == 5) {
            mCategoryTextView.setText("类别：保养美容维修店");
        } else if (Category == 13) {
            mCategoryTextView.setText("类别：配件店");
        }
        mDescriptionTextView = (TextView) getView().findViewById(R.id.gaizhuang_store_detail_Description);
        mDescriptionTextView.setText("描述:"+mStopArticle.getDescription());
        ProvinceID = mStopArticle.getProvinceID();
        CityID = mStopArticle.getCityID();
        DistrictID = mStopArticle.getDistrictID();
        String pname = mActivity.mCityManager.getCityName(CityManager.PRROVINCE, " ProvinceID", ProvinceID);
        String cname = mActivity.mCityManager.getCityName(CityManager.CITY,"CityID",CityID);
        String dname = mActivity.mCityManager.getCityName(CityManager.DISTRICT,"DistrictID",DistrictID);
        mDistrictTextView.setText("地区："+pname+"|"+cname+"|"+dname);


    }
}
