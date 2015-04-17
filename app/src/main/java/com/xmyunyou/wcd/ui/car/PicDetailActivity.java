package com.xmyunyou.wcd.ui.car;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.MyFavValue;
import com.xmyunyou.wcd.model.PicDetail;
import com.xmyunyou.wcd.photoview.PhotoViewAttacher;
import com.xmyunyou.wcd.ui.main.FragmentViewPagerAdapter;
import com.xmyunyou.wcd.ui.user.LoginActivity_;
import com.xmyunyou.wcd.ui.view.HackyViewPager;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.FileUtil;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EActivity(R.layout.activity_pic_detail)
public class PicDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    public static final String PARAMS_NEWS_ID = "NEWS_ID";
    public static final String PARAMS_NEWS_TITLE = "NEWS_TITLE";
    public static final String PARAMS_NEWS_PIC = "NEWS_PIC";
    public static final String PARAMS_NEWS_NUM = "NEWS_PIC_NUM";
    public static final String PARAMS_CATEGORY_ID = "PARAMS_CATEGORY_ID";



    @Extra(PARAMS_NEWS_ID)
    int newsID;
    @Extra(PARAMS_NEWS_TITLE)
    String mTitle;
    @Extra(PARAMS_NEWS_PIC)
    String mPicUrl;
    @Extra(PARAMS_NEWS_NUM)
    int num;
    @Extra(PARAMS_CATEGORY_ID)
    int mCategoryID;

    @ViewById(R.id.pic_pager)
    HackyViewPager mViewPager;
    @ViewById(R.id.pic_page)
    TextView mPageTextView;
    @ViewById(R.id.pic_title)
    TextView mTitleTextView;
    @ViewById(R.id.btn_fav)
    ImageView mFavImageView;
    @ViewById(R.id.btn_share)
    ImageView mShareImageView;

    //private List<ImageView> mImageList;
    //private ImageViewPagerAdapter mImageAdaprer;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mFragmentPagerAdapter;

    private int mTotal;
    @ViewById(R.id.btn_comment_news)
    ImageView mCommentImageView;
    @ViewById(R.id.common_title)
    TextView mTitleNewTextView;

    //缓存收藏
    private List<MyFavValue> mFavList;
    private boolean isFav = false;
    private MyFavValue mFavValue;

    //评论
    private ImageView mCommentBtnImageView;
    private EditText mContentEditText;

    //评论数
    private TextView mcommentNumTextView;

    //图片下载
    private ImageButton mDownloadImageButton;
    //图片下载路径
    private String filePath;


    @AfterViews void buildComponent(){
        mFragmentList = new ArrayList<>();
        mFragmentPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mTitleNewTextView.setText("图片详情");
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mDownloadImageButton = (ImageButton) findViewById(R.id.pic_download);
        mDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailFragment fragment = (ImageDetailFragment) mFragmentList.get(mViewPager.getCurrentItem());
                PicDownload(fragment.getImageView());
            }
        });
        mFavImageView.setVisibility(View.VISIBLE);
        mShareImageView.setVisibility(View.VISIBLE);
        mCommentImageView.setVisibility(View.VISIBLE);
        requestNews();

        //设置是否收藏
        mFavList = new ArrayList<>();
        if (isLogin()) {
            List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
            if (values != null) {
                mFavList.addAll(values);
                int userid = DataUtils.getLoginUser(mActivity).getID();
                for (MyFavValue v : values) {
                    isFav = (v.getNewsID() == newsID && userid == v.getUserID());
                    if (isFav) {
                        mFavValue = v;
                        mFavImageView.setSelected(true);
                        break;
                    }
                }
            }
        }
        mcommentNumTextView = (TextView) findViewById(R.id.comment_num);
        mcommentNumTextView.setText(num+"");
        mcommentNumTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if(num>0){
            mcommentNumTextView.setVisibility(View.VISIBLE);
        }else {
            mcommentNumTextView.setVisibility(View.GONE);
        }
        mContentEditText = (EditText) findViewById(R.id.comment_et);
        mCommentBtnImageView = (ImageView) findViewById(R.id.btn_comment);
        mCommentBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    params.put("NewsID", newsID + "");
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
                                intent.putExtra(NewsDetailActivity.PARAMS_NEWS_ID, newsID);
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
        });

    }

    private void requestNews(){

        showProgressDialog();
        Map<String, String> params = new HashMap<>();
        params.put("newsid", newsID + "");
        Type type = new TypeToken<List<PicDetail>>() {}.getType();
        requestGet(Constants.PIC_LIST, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                dismisProgressDialog();
                List<PicDetail> list = (List<PicDetail>) result;
                mTotal = list.size();
                for (int index = 0; index < mTotal; index ++){
                    PicDetail p = list.get(index);

                    ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
                    Bundle params = new Bundle();
                    params.putString("imageurl", p.getImageUrl());
                    params.putString("title", p.getTitle());
                    imageDetailFragment.setArguments(params);

                    mFragmentList.add(imageDetailFragment);
                    mFragmentPagerAdapter.notifyDataSetChanged();
                }
                setSelectedItem(0);
            }

            @Override
            public void onFailure(String errorMsg) {
                dismisProgressDialog();
            }
        });
    }

    private void PicDownload (final ImageView view){
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                try {
                    view.setDrawingCacheEnabled(true);
                    Bitmap bitmap = view.getDrawingCache();
                    String fileName = UUID.randomUUID() + ".png";
                    filePath = FileUtil.getDefaultPath()
                            + File.separator + fileName;
                    File file = new File(filePath);
                    out = new FileOutputStream(file);
                    if (null != bitmap && out != null) {
                        if (bitmap
                                .compress(Bitmap.CompressFormat.PNG, 80, out)) {
                            out.flush();
                            out.close();
                        }
                    }
                    mHandler.sendEmptyMessage(1);
//                    mActivity.showToast("图片保存成功(" + filePath + ")");
                } catch (Exception e) {
                    dismisProgressDialog();
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        try {
                            out.flush();
                        } catch (IOException e) {

                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }).start();


    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismisProgressDialog();
            if(msg.what ==1){
                mActivity.showToast("图片保存成功(" + filePath + ")");
            }else {
                mActivity.showToast("图片保存失败");
            }

        }
    };


    private void setSelectedItem(int position){
        mTitleTextView.setText(((ImageDetailFragment) mFragmentList.get(position)).getTitle());
        mPageTextView.setText((position + 1) + "/" + mTotal);
    }

    @Click({R.id.btn_share, R.id.btn_comment_news, R.id.btn_fav}) void share(View v) {
        switch (v.getId()){
            case R.id.btn_share:
                shareContent(mTitle, mTitle, getDetailUrl(mCategoryID, newsID), mPicUrl);
                break;
            case R.id.btn_fav:
                if (isLogin()) {
                    List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
                    if (values != null) {
                        for (MyFavValue fv : values) {
                            isFav = (fv.getNewsID() == newsID && DataUtils.getLoginUser(mActivity).getID() == fv.getUserID());
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
                        createFav(uid, newsID);
                    }
                } else {
                    startActivity(new Intent(mActivity, LoginActivity_.class));
                }
                break;
            case R.id.btn_comment_news:
                Intent intent = new Intent(mActivity, CommentActivity_.class);
                intent.putExtra(PicDetailActivity.PARAMS_NEWS_ID, newsID);
                startActivity(intent);
                break;
        }
    }

    /**
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
                    m.setNewsID(newsID);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
