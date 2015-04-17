package com.xmyunyou.wcd.ui.user;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.BrandCar;
import com.xmyunyou.wcd.model.BrandCarLine;
import com.xmyunyou.wcd.model.BrandCarLineType;
import com.xmyunyou.wcd.model.JiFenLog;
import com.xmyunyou.wcd.model.MyCar;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.dialog.BrandDialog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/4/13.
 */
public class UserMycarActivity extends BaseActivity {
    private BaseActivity mActivity;
    private TextView mTitleTextView;
    private ImageView mMyCarPicImageView;
    private Button mBrandButton;
    private Button mCarLineButton;
    private Button mCarTypeButton;
    private TextView mSaveMycarTextView;
    private int myCarID;

    private BrandCar mBrandCar;
    private BrandCarLine mCarLine;
    private BrandCarLineType mBrandCarLineType;

    private int mBrandID;
    private int mBrandCarID;

    private String mBrandName;
    private String mBrandCarName;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mycar);
        mActivity = this;
        User u = DataUtils.getLoginUser(mActivity);
        myCarID = u.getCarID();
        userid = RsaHelper.encryptDataFromStr(u.getID() + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
        requestMycar();
        init();
    }

    public void init(){
        mMyCarPicImageView = (ImageView)findViewById(R.id.mycar_pic);
        mBrandButton = (Button) findViewById(R.id.brand_btn);
        mCarLineButton = (Button) findViewById(R.id.carline_btn);
        mCarTypeButton = (Button) findViewById(R.id.cartype);
        mSaveMycarTextView = (TextView) findViewById(R.id.save_mycar);
        mTitleTextView =(TextView) findViewById(R.id.common_title);
        mTitleTextView.setText("我的车型");
        buildcomponent();
    }

    public void buildcomponent(){


        mBrandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDialog mBrandDialog = new BrandDialog(mActivity, mBrandID);
                mBrandDialog.setmOnBrandItemClick(new BrandDialog.OnBrandItemClickListener() {
                    @Override
                    public void OnBrandItemClick(Object car) {
                        mBrandCar = (BrandCar) car;
                        mBrandID = mBrandCar.getID();
                        mBrandName =mBrandCar.getName();
                        mBrandButton.setText(mBrandName);

                    }
                });
                mBrandDialog.show();
            }
        });

        mCarLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    BrandDialog mBrandDialog = new BrandDialog(mActivity, mBrandCarID, mBrandID);
                    mBrandDialog.setmOnBrandItemClick(new BrandDialog.OnBrandItemClickListener() {
                        @Override
                        public void OnBrandItemClick(Object car) {
                            mCarLine = (BrandCarLine) car;
                            mBrandCarID = mCarLine.getID();
                            mBrandCarName =mCarLine.getName();
                            mCarLineButton.setText(mBrandCarName);
                        }
                    });
                    mBrandDialog.show();
//                }
            }
        });

        mCarTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDialog mBrandDialog = new BrandDialog(mActivity, mBrandCarID, true);
                mBrandDialog.setmOnBrandItemClick(new BrandDialog.OnBrandItemClickListener() {
                    @Override
                    public void OnBrandItemClick(Object car) {
                        mBrandCarLineType = (BrandCarLineType) car;
                        mCarTypeButton.setText(mBrandCarLineType.getName());
                        myCarID = mBrandCarLineType.getID();
                    }
                });
                mBrandDialog.show();
            }
        });

        mSaveMycarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils.putInt(mActivity, DataUtils.USER_CAR_ID, myCarID);
                requstMycarID();
                requestMycar();
            }
        });

    }
    //提交我的车型ID
    private void requstMycarID(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid",userid);
        params.put("carID",myCarID+"");
        requestPost(Constants.MY_CAR_ID,params,boolean.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                 boolean b = (boolean) result;
                if (b){
                    showToast("修改成功");
                }else{
                    showToast("修改失败");
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }

    //请求我的车型
    private void requestMycar(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", myCarID + "");
        requestGet(Constants.MY_CAR,params,MyCar.class,new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MyCar mc = (MyCar)result;
                mActivity.loadImg(mc.getImageUrl(), mMyCarPicImageView);
                mBrandButton.setText(mc.getCarManufacturer().getName());
                mCarLineButton.setText(mc.getCarModel().getName());
                mCarTypeButton.setText(mc.getName());
                mBrandID = mc.getCarManufacturer().getID();
                mBrandCarID = mc.getCarModel().getID();
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

}
