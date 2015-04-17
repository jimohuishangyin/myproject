package com.xmyunyou.wcd.ui.gaizhuang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95 on 2015/3/24.
 */
public class StoreDatailInfoFragment extends Fragment {

    public static final String PARAMS_ID = "PARAMS_ID";
    private GaizhuangStopDetailActivity mActivity;
    private WebView mWebView;
    private String mHtmlContent;
    private String ID ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (GaizhuangStopDetailActivity) getActivity();
        ID = getArguments().getString(PARAMS_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gaizhuang_store_datailinfo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView = (WebView) getView().findViewById(R.id.gaizhuang_store_detail_webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings settings = mWebView.getSettings();
        settings.setAppCacheEnabled(true);
        //解决个别网站打不开
        settings.setDomStorageEnabled(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
//        settings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        try {
            InputStream is = mActivity.getAssets().open("news_detail.html");
            //获取文件的字节数
            int lenght = is.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            is.read(buffer);
            mHtmlContent = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


    requestDetail();
    }


    public void requestDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",ID );
        mActivity.requestGet(Constants.GAIZHUANG_STOP_DETAILS, params, StopArticle.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                StopArticle mStopArticle = (StopArticle) result;
                String htmlContent = mHtmlContent.replace("[title]", mStopArticle.getName()).replace("[date]", Globals.convertDateHHMM(mStopArticle.getCreateDate())).replace("[body]", mStopArticle.getContent());
                mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
            }

            @Override
            public void onFailure(String errorMsg) {
            }
        });

    }

}
