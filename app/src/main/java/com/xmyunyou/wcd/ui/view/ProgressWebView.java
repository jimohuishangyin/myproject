package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/***
 * @author huangsm
 * @date 2014年10月31日
 * @email huangsanm@gmail.com
 * @desc 
 */
public class ProgressWebView extends WebView {
	
	private ProgressBar mProgressBar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        //mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.main_banner_index_selected));
        addView(mProgressBar);

        WebSettings ws = getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setDatabaseEnabled(true);

        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        //解决个别网站打不开
        ws.setDomStorageEnabled(true);
        
        setWebChromeClient(new WebChromeClient());

    }
    
    public class WebChromeClient extends android.webkit.WebChromeClient {
        
    	@Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
            	mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                	mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
