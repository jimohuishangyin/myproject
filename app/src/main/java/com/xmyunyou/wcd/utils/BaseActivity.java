package com.xmyunyou.wcd.utils;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.ProductArticle;
import com.xmyunyou.wcd.model.SearchHot;
import com.xmyunyou.wcd.model.Services;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.ui.car.NewsDetailActivity;
import com.xmyunyou.wcd.ui.car.NewsDetailActivity_;
import com.xmyunyou.wcd.ui.car.NewsListActivity;
import com.xmyunyou.wcd.ui.car.NewsListActivity_;
import com.xmyunyou.wcd.model.Regrade;
import com.xmyunyou.wcd.ui.car.PicDetailActivity;
import com.xmyunyou.wcd.ui.car.PicDetailActivity_;
import com.xmyunyou.wcd.ui.car.VideoDetailActivity;
import com.xmyunyou.wcd.ui.car.VideoDetailActivity_;
import com.xmyunyou.wcd.ui.dialog.ProgressDialog;
import com.xmyunyou.wcd.ui.gaizhuang.GaizhuangServerActivity;
import com.xmyunyou.wcd.ui.gaizhuang.GaizhuangStopDetailActivity;
import com.xmyunyou.wcd.ui.search.SearchCarActivity;
import com.xmyunyou.wcd.utils.net.GsonArrayRequest;
import com.xmyunyou.wcd.utils.net.GsonObjectRequest;
import com.xmyunyou.wcd.utils.net.RequestListener;
import com.xmyunyou.wcd.utils.net.VolleyManager;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends FragmentActivity {

    public static final String BAIDU_PUSH = "Aclr3qWkBzph7FWUUeqBVTnQ";

    public BaseActivity mActivity;
    private ProgressDialog mProgressDialog;

    protected int mScreenWidth;
    protected int mScreenHeight;

    //百度推送
    private FrontiaSocialShare mSocialShare;
    private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();

    private DisplayImageOptions mOptions;

    public CityManager mCityManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        //初始化百度推送
        setupBaiduPush();

        showProgress();

        startAnimated();

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_icon)
                .showImageForEmptyUri(R.drawable.default_icon)
                .showImageOnFail(R.drawable.default_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        mCityManager = new CityManager();
        mCityManager.readDataBase(mActivity);

    }

    private void setupBaiduPush() {
        Frontia.init(getApplicationContext(), BAIDU_PUSH);
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, BAIDU_PUSH);
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                mActivity, R.layout.notification_type, R.id.imageView,
                R.id.title, R.id.text);
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(R.drawable.logo);
        cBuilder.setLayoutDrawable(R.drawable.logo);
        PushManager.setNotificationBuilder(this, 3, cBuilder);

        mSocialShare = Frontia.getSocialShare();
        mSocialShare.setContext(mActivity);
        //1174299571
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.SINAWEIBO.toString(), "2569044769");
        //1103475638
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.QZONE.toString(), "1103594409");
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.QQFRIEND.toString(), "1103594409");
        mSocialShare.setClientName(FrontiaAuthorization.MediaType.QQFRIEND.toString(), "玩车之家");
        //wx11885eea5e1c465d
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.WEIXIN.toString(), "wx2494826d02d44455");
    }

    public void shareContent(String title, String content, String url, String imageUrl) {
        try {
            mImageContent.setTitle(title);
            mImageContent.setContent(content);
            mImageContent.setLinkUrl(url);
            mImageContent.setImageUri(Uri.parse(imageUrl));
            mSocialShare.show(getWindow().getDecorView(), mImageContent, FrontiaSocialShare.FrontiaTheme.LIGHT, new FrontiaSocialShareListener() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int errCode, String errMsg) {
                    log(errCode + ":" + errMsg);
                }

                @Override
                public void onCancel() {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //启动动画
    private void startAnimated() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showProgress() {
        mProgressDialog = new ProgressDialog(mActivity);
        // mProgressDialog.setMessage("请稍等..");
        //mAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.loading_rotate);
        mProgressDialog.setCanceledOnTouchOutside(false);
        //mProgressDialog.show();
        //mProgressDialog.setContentView(R.layout.dialog_progress_layout);
    }

    public void showProgressDialog() {
        if (mProgressDialog != null) {
//            mProgressDialog.getImageView().startAnimation(mAnimation);
            mProgressDialog.show();
        }
    }

    public void dismisProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isLogin() {
        return DataUtils.getLoginUser(mActivity) != null;
    }

    /*public void setTitle(String title) {
        ((TextView) findViewById(R.id.common_title)).setText(title);
    }*/

    public void log(Object args) {
        Globals.log(args);
    }


    public void requestGetMobileIndex(String url, Map<String, String> params, final Class cla, final RequestListener listener) {
        VolleyManager.getInstance(mActivity).addRequest(new GsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String responseBody = response.toString();
                    listener.onSuccess(responseBody);

                    //DataUtils.putString(mActivity, DataUtils.CACHE_DATA, responseBody);
                    //发送广播
                    /*Intent intent = new Intent(MainTabActivity_.CACHE_BROAD_CAST);
                    sendBroadcast(intent);*/
                    /*log(responseBody);
                    Object obj = null;
                    Gson gson = new Gson();
                    if (cla != null) {
                        obj = gson.fromJson(responseBody, cla);
                    }*/
                } catch (Exception ex) {
                    listener.onFailure(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }, errorListener(listener)), "Android");
    }

    private void request(final String url, Map<String, String> params, int method, final Class cla, final Type type, final RequestListener listener) {
        log(url);
        Request request = null;
        if (cla != null) {
            request = new GsonObjectRequest(method, url, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String responseBody = response.toString();
                        log(responseBody);
                        if (Constants.RSA.equals(url)) {
                            listener.onSuccess(responseBody);
                        } else {
                            Object obj = null;
                            Gson gson = new Gson();
                            if (cla != null) {
                                obj = gson.fromJson(responseBody, cla);
                            }
                            listener.onSuccess(obj);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, errorListener(listener));
        }

        if (type != null) {
            request = new GsonArrayRequest(method, url, params, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        String responseBody = response.toString();
                        log(responseBody);
                        Object obj = null;
                        Gson gson = new Gson();
                        if (type != null) {
                            obj = gson.fromJson(responseBody, type);
                        }
                        listener.onSuccess(obj);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, errorListener(listener));
        }

        VolleyManager.getInstance(mActivity).addRequest(request, "Android");
    }

    protected Response.ErrorListener errorListener(final RequestListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismisProgressDialog();
                //listener.onFailure(error.getMessage());
            }
        };
    }


    public void requestPost(String url, Map<String, String> params, Class cla, RequestListener listener) {
        request(url, params, Request.Method.POST, cla, null, listener);
    }

    public void requestGet(String url, Map<String, String> params, Class cla, RequestListener listener) {
        request(addParams(url, params), params, Request.Method.GET, cla, null, listener);
    }

    public void requestPost(String url, Map<String, String> params, Type type, RequestListener listener) {
        request(url, params, Request.Method.POST, null, type, listener);
    }

    public void requestGet(String url, Map<String, String> params, Type type, RequestListener listener) {
        request(addParams(url, params), params, Request.Method.GET, null, type, listener);
    }

    private String addParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuffer p = new StringBuffer(url);
        p.append(url.contains("?") ? "&" : "?");
        for (String key : params.keySet()) {
            p.append(key).append("=").append(params.get(key)).append("&");
        }
        return p.substring(0, p.length() - 1);
    }

    /**
     * 文字样式处理
     *
     * @param text
     * @param start
     * @param end
     * @param color
     * @return
     */
    public SpannableString setSpannableText(String text, int start, int end, int color) {
        int textColor = getResources().getColor(color);
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(textColor), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ss;
    }

    public String getEditTextStr(EditText editText) {
        return editText.getEditableText().toString();
    }

    //创建勋章
    public void createMedal(String medal, LinearLayout ll){
        if(!TextUtils.isEmpty(medal)){
            ll.removeAllViews();
            int width = dip2px(17);
            int height = dip2px(29);
            String[] medals = medal.split("\\,");
            for (int i = 0; i < medals.length; i++) {
                if(!TextUtils.isEmpty(medals[i])){
                    ImageView iv = new ImageView(mActivity);
                    iv.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    iv.setPadding(2, 0, 2, 0);
                    mActivity.loadImg(Constants.getMedalPic(medals[i]), width, height, iv);
                    ll.addView(iv);
                }
            }
        }
    }


    public void setViewPagerScrollSpeed(ViewPager pager){
       /* try {
           *//* Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( pager.getContext( ) );
            mScroller.set( pager, scroller);*//*
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }*/
    }

    public void startGaizhuangServer(Services a,int PrivinceID,int CityID){
        Intent intent = new Intent(mActivity, GaizhuangServerActivity.class);
        intent.putExtra(GaizhuangServerActivity.PARAMS_ID,a.getID());
        intent.putExtra(GaizhuangServerActivity.PARAMS_TITLE,a.getName());
        intent.putExtra(GaizhuangServerActivity.PARAMS_PRIVINCEID,PrivinceID);
        intent.putExtra(GaizhuangServerActivity.PARAMS_CITYID,CityID);
        startActivity(intent);

        log("dddddddddd:"+intent.toUri(0));
    }

    //跳转

    public void startGaizhuangStopDetail(StopArticle a){
        Intent intent = new Intent(mActivity,GaizhuangStopDetailActivity.class);
        intent.putExtra(GaizhuangStopDetailActivity.PARAMS_STOP_ID,a.getID());
        startActivity(intent);

        log("dddddddddd:"+intent.toUri(0));
    }

    //跳转
    public void startNewsDetail(ProductArticle a) {
        Intent intent = new Intent(mActivity, NewsDetailActivity_.class);
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_ID, a.getID());
        startActivity(intent);

        log("dddddddd:" + intent.toUri(0));
    }

    //跳转
    public void startNewsDetail(ProductArticle a, int type) {
        Intent intent = new Intent(mActivity, NewsDetailActivity_.class);
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_ID, a.getID());
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_TYPE, type);
        startActivity(intent);

        log("dddddddd:" + intent.toUri(0));
    }

    //打开美女图
    public void startPicDetail(ProductArticle a){
        Intent intent = new Intent(mActivity, PicDetailActivity_.class);
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_ID, a.getID());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_TITLE, a.getTitle());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_PIC, a.getImageUrl());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_NUM,a.getCommentNums());
        intent.putExtra(PicDetailActivity_.PARAMS_CATEGORY_ID, a.getCategoryID());
        startActivity(intent);
    }


    //跳转
    public void startNewsDetail(Article a) {
        Intent intent = new Intent(mActivity, NewsDetailActivity_.class);
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_ID, a.getID());
        startActivity(intent);

        log("dddddddd:" + intent.toUri(0));
    }

    //跳转
    public void startNewsDetail(Article a, int type) {
        Intent intent = new Intent(mActivity, NewsDetailActivity_.class);
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_ID, a.getID());
        intent.putExtra(NewsDetailActivity_.PARAMS_NEWS_TYPE, type);
        startActivity(intent);

        log("dddddddd:" + intent.toUri(0));
    }
    //跳转
    public void startSearchHot(SearchHot sh){
        Intent intent = new Intent(mActivity, SearchCarActivity.class);
        intent.putExtra("model",sh.getID());
        startActivity(intent);
    }

    //打开美女图
    public void startPicDetail(Article a){
        Intent intent = new Intent(mActivity, PicDetailActivity_.class);
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_ID, a.getID());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_TITLE, a.getTitle());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_PIC, a.getImageUrl());
        intent.putExtra(PicDetailActivity.PARAMS_NEWS_NUM,a.getCommentNums());
        intent.putExtra(PicDetailActivity_.PARAMS_CATEGORY_ID, a.getCategoryID());
        startActivity(intent);
    }


    //拼接分享地址ID
    public String getDetailUrl(int categoryID, int newsID){
        String catalog = "";
        if(WanApp.mCategoryList != null){
            final int size = WanApp.mCategoryList.size();
            for (int i = 0; i < size; i ++){
                Category c = WanApp.mCategoryList.get(i);
                if(categoryID == c.getID()){
                    catalog = c.getUrl();
                    break;
                }
            }
        }

        String shareUrl = "";
        if(TextUtils.isEmpty(catalog)){
            shareUrl = "http://www.wanchezhijia.com";
        }else{
            //http://www.wanchezhijia.com/qindu/317.html
            shareUrl = "http://www.wanchezhijia.com/" + catalog + "/" + newsID + ".html";
        }
        return shareUrl;
    }

    /*//跳转
    public void startNewsVideoDetail(Article a) {
        Intent intent = new Intent(mActivity, VideoDetailActivity_.class);
        intent.putExtra(VideoDetailActivity_.PARAMS_NEWS_ID, a.getID());
        startActivity(intent);

        log("dddddddd:" + intent.toUri(0));
    }*/

    //检测是否有新版本
    //版本更新
    public void updateVersion(RequestListener listener) {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String ReqClientVersion = null;

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            ReqClientVersion = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String DeviceName = android.os.Build.MODEL;
        String DeviceManufacturer = android.os.Build.MANUFACTURER;
        String DeviceHardwareVersion = android.os.Build.FINGERPRINT;
        String DeviceFirmwareVersion = android.os.Build.VERSION.RELEASE;
        String DeviceUniqueID = tm.getDeviceId();
        String Phone = tm.getLine1Number();
        String AnonymousUserId = tm.getSubscriberId();
        String ReqClient = "wanchezhijia";
        Map<String, String> params = new HashMap<String, String>();
        params.put("Phone", Phone);
        params.put("DeviceManufacturer", DeviceManufacturer);
        params.put("DeviceName", DeviceName);
        params.put("DeviceFirmwareVersion", DeviceFirmwareVersion);
        params.put("DeviceHardwareVersion", DeviceHardwareVersion);
        params.put("PowerSource", "");
        params.put("IsKeyboardDeployed", "");
        params.put("IsKeyboardPresent", "");
        params.put("AnonymousUserId", AnonymousUserId);
        params.put("DeviceUniqueID", DeviceUniqueID);
        params.put("AppUri", "");
        params.put("ReqClient", ReqClient);
        params.put("ReqClientVersion", ReqClientVersion);
        params.put("PushUserID", DataUtils.getString(mActivity, DataUtils.PushUserID));
        params.put("PushChannelID", DataUtils.getString(mActivity, DataUtils.PushChannelID));
        params.put("PhoneType", "2");

        requestPost(Constants.UPDATE_VERSION, params, Regrade.class, listener);
    }

    public void loadImg(String url, int width, int height, ImageView imageView) {
        ImageSize size = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage(url, imageView, size, mOptions);
        //Picasso.with(mActivity).load(url).placeholder(R.drawable.default_icon).resize(width, height).into(view);
    }

    public void loadImg(String url, ImageView view) {
        ImageLoader.getInstance().displayImage(url, view, mOptions);
        //Picasso.with(mActivity).load(url).placeholder(R.drawable.default_icon).into(view);
    }

    private float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    public int dip2px(float dipValue) {
        return (int) (dipValue * getDensity() + 0.5f);
    }

    public int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(mActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(mActivity);
    }
}
