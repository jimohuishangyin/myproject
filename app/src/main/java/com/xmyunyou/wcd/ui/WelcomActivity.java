package com.xmyunyou.wcd.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.json.ArticleList;
import com.xmyunyou.wcd.model.json.MobileIndex;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_welcom)
public class WelcomActivity extends BaseActivity {

    public static final int DELAY_TIME = 2000;
    private MobileIndex mCacheMobile;
    @AfterViews void buildComponent(){
        requestRsa();
        requestFirstArticle();
        mHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(mActivity,MainActivity.class);
            intent.putExtra(MainActivity.PARAMS_NEWS,mCacheMobile);
            startActivity(intent);
            finish();
        }
    };

    private void requestRsa(){
        requestPost(Constants.RSA, null, String.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                String rsa = (String) result;
                DataUtils.putRsaStr(mActivity, DataUtils.RSA_DATA, rsa);
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

    //更新缓存中的数据
    private void requestFirstArticle(){
        if(Globals.isConnectionNetWork(mActivity)){
            mActivity.requestGet(Constants.HOST_ALL_URL,null, MobileIndex.class ,new RequestListener() {
                @Override
                public void onSuccess(Object result) {
                    mCacheMobile= (MobileIndex)result;
                }

                @Override
                public void onFailure(String errorMsg) {
                    mActivity.dismisProgressDialog();
                    mActivity.showToast(errorMsg);
                }
            });
        }
    }


   /* //请求文章
    private void requestArticle() {
        Map<String, String> params = new HashMap<String, String>();
//        params.put("CategoryParentID", "1");
        params.put("page", "" + 1);
        params.put("size", Constants.PAGE_SIZE);
        if (Globals.isConnectionNetWork(mActivity)) {
            mActivity.requestGet(Constants.HOST_ALL_URL, params, ArticleList.class, new RequestListener() {
                @Override
                public void onSuccess(Object result) {
                    al = (ArticleList) result;
                }

                @Override
                public void onFailure(String errorMsg) {
                    mActivity.dismisProgressDialog();
                    mActivity.showToast(errorMsg);
                }
            });
        }
    }*/

    //同步收藏数据
   /* private void requestFavData(){

    }*/

}
