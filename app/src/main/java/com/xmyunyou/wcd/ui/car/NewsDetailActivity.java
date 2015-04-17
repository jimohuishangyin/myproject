package com.xmyunyou.wcd.ui.car;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.MyFavValue;
import com.xmyunyou.wcd.ui.user.LoginActivity;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.ui.view.ProgressWebView;
import com.xmyunyou.wcd.ui.view.VideoEnabledWebChromeClient;
import com.xmyunyou.wcd.ui.view.VideoEnabledWebView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EActivity(R.layout.activity_news_detail)
public class NewsDetailActivity extends BaseActivity implements View.OnTouchListener {

    public static final String PARAMS_NEWS_ID = "NEWS_ID";
    public static final String PARAMS_NEWS_TYPE = "NEWS_TYPE";

    @Extra(PARAMS_NEWS_ID)
    int mNewsID;
    @Extra(PARAMS_NEWS_TYPE)
    int mNewsType;

    @ViewById(R.id.news_detail_title)
    TextView mTitleTextView;
    @ViewById(R.id.news_detail_date)
    TextView mDateTextView;
    @ViewById(R.id.news_detail_webview)
    WebView mWebView;
    @ViewById(R.id.comment_et)
    EditText mContentEditText;
    @ViewById(R.id.btn_fav)
    ImageView mFavImageView;
    @ViewById(R.id.btn_share)
    ImageView mShareImageView;

    @ViewById(R.id.news_video_ll)
    LinearLayout mLinearLayout;
    @ViewById(R.id.news_video_pic)
    ImageView mPicImageView;
    @ViewById(R.id.news_detail_content)
    TextView mContentTextView;

    private Article mArticle;

    private boolean isFav = false;
    private MyFavValue mFavValue;

    //缓存收藏
    private List<MyFavValue> mFavList;
    private String mHtmlContent;
    private ImageView mCommentImageView;
    private TextView mTitleNewTextView;
    private ScrollView mScroller;
    //评论数
    private TextView mcommentNumTextView;

    //滑动距离
    private float mEndX;
    private float mTempX;

    @AfterViews
    void buildComponent() {
        showProgressDialog();
        mFavImageView.setVisibility(View.VISIBLE);
        mShareImageView.setVisibility(View.VISIBLE);
        mCommentImageView = (ImageView) findViewById(R.id.btn_comment_news);
        mTitleNewTextView = (TextView) findViewById(R.id.common_title);
        mScroller = (ScrollView) findViewById(R.id.good_detail_rl);
        mScroller.setOnTouchListener(this);
        mcommentNumTextView = (TextView) findViewById(R.id.comment_num);
        mWebView.setOnTouchListener(this);
        if (mNewsType == 4) {
            mTitleNewTextView.setText("视频详情");
        } else {
            mTitleNewTextView.setText("文章详情");
        }
        mCommentImageView.setVisibility(View.VISIBLE);
        mCommentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommentActivity_.class);
                intent.putExtra(NewsDetailActivity.PARAMS_NEWS_ID, mNewsID);
                startActivity(intent);
            }
        });
        if (mNewsType == 4) {
            mLinearLayout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        } else {
            mLinearLayout.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);

            mWebView.setWebChromeClient(new WebChromeClient());
            WebSettings settings = mWebView.getSettings();
            settings.setAppCacheEnabled(true);
            //解决个别网站打不开
            settings.setDomStorageEnabled(true);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
        }
        try {
            InputStream is = getAssets().open("news_detail.html");
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

        //设置是否收藏
        mFavList = new ArrayList<>();
        if (isLogin()) {
            List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
            if (values != null) {
                mFavList.addAll(values);
                int userid = DataUtils.getLoginUser(mActivity).getID();
                for (MyFavValue v : values) {
                    isFav = (v.getNewsID() == mNewsID && userid == v.getUserID());
                    if (isFav) {
                        mFavValue = v;
                        mFavImageView.setSelected(true);
                        break;
                    }
                }
            }
        }
        requestNewsDetail();
    }

    private void requestNewsDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("Id", mNewsID + "");

        requestGet(Constants.ARTICLE_DETAIL, params, Article.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                dismisProgressDialog();
                mArticle = (Article) result;
                initData();
            }

            @Override
            public void onFailure(String errorMsg) {
                dismisProgressDialog();
                showToast(errorMsg);
            }
        });
    }

    private void initData() {
        mTitleTextView.setText(mArticle.getTitle());
        mDateTextView.setText(Globals.convertDate(mArticle.getUpdateDate()));
        mcommentNumTextView.setText(mArticle.getCommentNums() + "");
        mcommentNumTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if (mArticle.getCommentNums() > 0) {
            mcommentNumTextView.setVisibility(View.VISIBLE);
        } else {
            mcommentNumTextView.setVisibility(View.GONE);
        }
        //设置是否是视频
        if (mNewsType == 4) {
            loadImg(mArticle.getImageUrlHD(), mPicImageView);
            mContentTextView.setText(mArticle.getDescription());
        } else {
            mTitleTextView.setVisibility(View.GONE);
            mDateTextView.setVisibility(View.GONE);
            String htmlContent = mHtmlContent.replace("[title]", mArticle.getTitle()).replace("[date]", Globals.convertDateHHMM(mArticle.getCreateDate())).replace("[body]", mArticle.getContent());
            mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        }
    }

    //分享
    @Click(R.id.btn_share)
    void share() {
        String url = getDetailUrl(mArticle.getCategoryID(), mNewsID);
        shareContent(mArticle.getTitle(), mArticle.getTitle(), url, mArticle.getImageUrl());
    }

    @Click(R.id.news_video_play)
    void playVideo() {
        //http://www.wanchezhijia.com/sdsp/122.html?phoneType=2&rnd=
        String playVideo = getDetailUrl(mArticle.getCategoryID(), mNewsID) + "?phoneType=2&rnd=" + UUID.randomUUID();
        Intent intent = new Intent(mActivity, VideoDetailActivity_.class);
        intent.putExtra(VideoDetailActivity.PARAMS_TITLE, mArticle.getTitle());
        intent.putExtra(VideoDetailActivity.PARAMS_NEW_URL, playVideo);
        startActivity(intent);
    }

    //收藏
    @Click(R.id.btn_fav)
    void fav() {
        if (isLogin()) {
            List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
            if (values != null) {
                for (MyFavValue fv : values) {
                    isFav = (fv.getNewsID() == mNewsID && DataUtils.getLoginUser(mActivity).getID() == fv.getUserID());
                    if (isFav) {
                        mFavValue = fv;
                        mFavImageView.setSelected(true);
                        break;
                    }
                }
            }

            if (isFav) {
                //删除收藏
                deleteFav(mFavValue.getFavID());
            } else {
                //创建收藏
                String uid = RsaHelper.encryptDataFromStr(DataUtils.getLoginUser(mActivity).getID() + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));

                createFav(uid, mNewsID);
            }
        } else {
            startActivity(new Intent(mActivity, LoginActivity_.class));
        }
    }

    //评论
    @Click(R.id.btn_comment)
    void submitComment() {
        if (!isLogin()) {
            startActivity(new Intent(mActivity, LoginActivity_.class));
        } else {
            String content = getEditTextStr(mContentEditText);
            if (TextUtils.isEmpty(content)) {
                showToast("请输入内容");
                return;
            }

            showProgressDialog();

            final String name = DataUtils.getLoginUser(mActivity).getName();
            Map<String, String> params = new HashMap<>();
            params.put("Content", content);
            params.put("NewsID", mNewsID + "");
            params.put("UserName", name);
            params.put("DeviceID", Globals.getDeviceID(mActivity));
            params.put("DeviceName", Globals.getDeviceName());
            params.put("From", "Android");

            requestPost(Constants.CREATE_COMMENT, params, Boolean.class, new RequestListener() {
                @Override
                public void onSuccess(Object result) {
                    dismisProgressDialog();
                    Boolean b = (Boolean) result;
                    if (b) {
                        mContentEditText.setText("");
                        showToast("发表成功");
                        Intent intent = new Intent(mActivity, CommentActivity_.class);
                        intent.putExtra(NewsDetailActivity.PARAMS_NEWS_ID, mNewsID);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(String errorMsg) {
                    dismisProgressDialog();
                    showToast(errorMsg);
                }
            });
        }
    }


    /**a
     * 创建收藏
     *
     * @param uid
     * @param NewsID
     */
    private void createFav(final String uid, final int NewsID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserID", uid + "");
        params.put("toid", NewsID + "");
        params.put("type",1+"");
        mActivity.requestPost(Constants.FAV, params, Integer.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Integer b = (Integer) result;
                if (b > 0) {
                    MyFavValue m = new MyFavValue();
                    m.setUserID(DataUtils.getLoginUser(mActivity).getID());
                    m.setNewsID(NewsID);
                    m.setFavID(b);
                    mFavList.add(m);
                    mFavValue = m;
                    String jsonStr = new Gson().toJson(mFavList);
                    log("json:" + jsonStr);
                    DataUtils.putString(mActivity, DataUtils.COLLECTION, jsonStr);

                    mFavImageView.setSelected(true);
                    mActivity.showToast("收藏成功");
                } else {
                    mActivity.showToast("收藏失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

    /**
     * 删除收藏
     *
     * @param favID
     */
    private void deleteFav(int favID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("IDs", favID + "");
        params.put("Password",DataUtils.getLoginUser(mActivity).getPassword());
        params.put("userID",DataUtils.getLoginUser(mActivity).getID()+"");
        mActivity.requestGet(Constants.DEL_FAV, params, Boolean.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Boolean b = (Boolean) result;
                if (b) {
                    for (MyFavValue v : mFavList) {
                        if (v.getFavID() == mFavValue.getFavID()) {
                            boolean is = mFavList.remove(v);
                            break;
                        }
                    }
                    isFav = false;
                    String jsonStr = new Gson().toJson(mFavList);
                    log("json:" + jsonStr);
                    DataUtils.putString(mActivity, DataUtils.COLLECTION, jsonStr);
                    mFavImageView.setSelected(false);
                    mActivity.showToast("删除成功");
                } else {
                    mActivity.showToast("删除失败，等会再试试吧");
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTempX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = event.getRawX();
                if (mEndX - mTempX > 350) {
                    finish();
                }
                break;
        }
        return false;
    }


}
