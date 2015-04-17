package com.xmyunyou.wcd.ui.car;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.ui.view.VideoEnabledWebChromeClient;
import com.xmyunyou.wcd.ui.view.VideoEnabledWebView;
import com.xmyunyou.wcd.utils.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_article_video_detail)
public class VideoDetailActivity extends BaseActivity {

    public static final String PARAMS_NEW_URL = "NEWS_Url";
    public static final String PARAMS_TITLE = "NEWS_TITLE";

    @Extra(PARAMS_NEW_URL)
    String mVideoUrl;
    @Extra(PARAMS_TITLE)
    String mTitle;

    @ViewById(R.id.news_detail_webview_video)
    VideoEnabledWebView mVideoWebView;
    /*@ViewById(R.id.common_title)
    TextView mTitleTextView;*/

    @ViewById(R.id.news_video_play_rl)
    View mView;
    @ViewById(R.id.news_video_rl)
    ViewGroup mViewGroup;

    //播放视频

    private VideoEnabledWebChromeClient mWebChromeClient;

    @AfterViews
    void buildComponent() {
        //mTitleTextView.setText(mTitle);
        mVideoWebView.getSettings().setJavaScriptEnabled(true);
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null);
        mWebChromeClient = new VideoEnabledWebChromeClient(mView, mViewGroup, loadingView, mVideoWebView) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...

            }
        };
        mWebChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:

                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all

                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all

                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }
        });
        mVideoWebView.setWebChromeClient(mWebChromeClient);
        mVideoWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mVideoWebView.loadUrl(mVideoUrl);
    }

    @Override
    protected void onPause() {
        mVideoWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
