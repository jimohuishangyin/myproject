package com.xmyunyou.wcd.ui.search;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.BrandCar;
import com.xmyunyou.wcd.model.BrandCarLine;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.Products;
import com.xmyunyou.wcd.model.SearchHot;
import com.xmyunyou.wcd.ui.adapter.ArticleAdapter;
import com.xmyunyou.wcd.ui.dialog.BrandDialog;

import com.xmyunyou.wcd.ui.view.LoadMoreGridView;
import com.xmyunyou.wcd.ui.view.LoadMoreListView;
import com.xmyunyou.wcd.ui.view.TextLabelView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/1/21.
 */
public class SearchActivity extends BaseActivity {
    private ImageView mBackImageView;
    private EditText mContentEditText;
    private ImageView mSearchImageView;
    private TextView mChooseTextView;
    //搜索
    private LoadMoreListView mListView;
    private List<Article> mArticleList;
    private ArticleAdapter mArticleAdapter;
    private PopupWindow mPopupWindow;

    private BrandCar mBrandCar;
    private BrandCarLine mCarLine;
    Button mBrandButton;
    Button mCarlineButton;
    Button mSearchCarlineButton;
    //搜索车型
//    private ImageView mSearchCarImageView;
//    private TextView mCarLineTextView;
    //车系的id
/* //    int model;
    private ImageView mCursorImageView;
    private int mOffset;
    private int mCurrentIndex;
    private RadioButton mTuringRadioButton;
    private RadioButton mCareRadioButton;
    private RadioButton mGirlRadioButton;
    private RadioButton mVideoRadioButton;
    private RadioButton mMotorzineRadioButton;
    private TurnSearchFragment mTurnSearchFragment;
    private CareSearchFragment mCareSearchFragment;
    private GirlSearchFragment mGirlSearchFragment;
    private VideoSearchFragment mVideoSearchFragment;
    private MotorzineSearchFragment mMotorzineSearchFragment;
    private boolean refresh = false;
    private TextLabelView mTextLabelView;

    private LinearLayout mProductLinearLayout;*/

    private int mBrandID;
    private int mBrandCarID;

    private String mBrandName;
    private String mBrandCarName;


    //热门车型
    private LoadMoreGridView mLoadMoreGridView;
    private SearchHotAdapter mSearchHotAdapter;
    private List<SearchHot> mSearchHot;

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mContentEditText = (EditText) findViewById(R.id.search_content);
        mSearchImageView = (ImageView) findViewById(R.id.search_click);
        mChooseTextView = (TextView) findViewById(R.id.choose_car);
//        mSearchCarImageView = (ImageView) findViewById(R.id.search_car_image);
//        mCarLineTextView = (TextView) findViewById(R.id.carline_tv);
        mListView = (LoadMoreListView) findViewById(R.id.search_list);
        /*mCursorImageView = (ImageView) findViewById(R.id.search_cursor);
        mTuringRadioButton = (RadioButton) findViewById(R.id.search_turing);
        mCareRadioButton = (RadioButton) findViewById(R.id.search_care);
        mGirlRadioButton = (RadioButton) findViewById(R.id.search_girl);
        mVideoRadioButton = (RadioButton) findViewById(R.id.search_video);
        mMotorzineRadioButton = (RadioButton) findViewById(R.id.search_motorzine);
        mProductLinearLayout = (LinearLayout) findViewById(R.id.search_car_choose);
        mTextLabelView = (TextLabelView) findViewById(R.id.search_model_label);*/
        mScrollView = (ScrollView) findViewById(R.id.hot_search);
        /*mTextLabelView.setOnItemClick(new TextLabelView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String id = view.getTag().toString();
                String carLine = mCarLine.getName();
                int carLineID = mCarLine.getID();
                String product = ((TextView) view).getText().toString();
                Intent intent = new Intent(mActivity, SearchProductsActivity.class);
                intent.putExtra("carLine", carLine);
                intent.putExtra("product", product);
                intent.putExtra("id", id);
                intent.putExtra("carLineID", carLineID);
                startActivity(intent);
            }
        });*/
        /*mTuringRadioButton.setOnClickListener(this);
        mCareRadioButton.setOnClickListener(this);
        mGirlRadioButton.setOnClickListener(this);
        mVideoRadioButton.setOnClickListener(this);
        mMotorzineRadioButton.setOnClickListener(this);*/
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                searchKey();
            }
        });
        mChooseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(v);
            }
        });
        mSearchHot = new ArrayList<SearchHot>();
        mSearchHotAdapter = new SearchHotAdapter(mActivity, mSearchHot);
        mLoadMoreGridView = (LoadMoreGridView) findViewById(R.id.search_hot_list);
        mLoadMoreGridView.setAdapter(mSearchHotAdapter);
        mLoadMoreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchHot sh = (SearchHot) mSearchHotAdapter.getItem(position);
                Intent intent = new Intent(mActivity, SearchCarActivity.class);
                intent.putExtra(SearchCarActivity.PARAMS_PIC, sh.getImageUrlHD());
                intent.putExtra(SearchCarActivity.PARAMS_TITLE, sh.getName());
                intent.putExtra(SearchCarActivity.PARAMS_ID, sh.getID());
                startActivity(intent);
            }
        });
        requestSearchHot();
    }

    private void search() {
        mArticleList = new ArrayList<Article>();
        mArticleAdapter = new ArticleAdapter(mActivity, mArticleList);
        mListView.setAdapter(mArticleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mArticleAdapter.isEmpty()) {
                    startNewsDetail(mArticleAdapter.getItem(position));
                }
            }
        });

    }

    //关键字搜索接口
    private void searchKey() {
        String key = mContentEditText.getEditableText().toString();
        if (TextUtils.isEmpty(key)) {
            showToast("请输入关键词");
            mListView.setVisibility(View.GONE);
            return;
        } else {
            mListView.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", Globals.encode(key));
        Type type = new TypeToken<List<Article>>() {
        }.getType();
        mActivity.requestGet(Constants.SEARCH_LIST, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<Article> gl = (List<Article>) result;
                mArticleList.clear();
                mArticleList.addAll(gl);
                mArticleAdapter.notifyDataSetChanged();
                mListView.onLoadMoreComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {
                showToast(errorMsg);
            }
        });

    }

    //热门搜索
    public void requestSearchHot() {
        Type type = new TypeToken<List<SearchHot>>() {
        }.getType();
        requestGet(Constants.SEARCH_LIST_INIT, null, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<SearchHot> list = (List<SearchHot>) result;
                mSearchHot.addAll(list);
                mSearchHotAdapter.notifyDataSetChanged();
                mLoadMoreGridView.onLoadComplete(true);
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

    private void showPopWindow(View v) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_search, null);
        mBrandID = TextUtils.isEmpty(DataUtils.getString(mActivity, DataUtils.CAR)) ? 0 : Integer.valueOf(DataUtils.getString(mActivity, DataUtils.CAR));
        mBrandCarID = TextUtils.isEmpty(DataUtils.getString(mActivity, DataUtils.CAR_LINE)) ? 0 : Integer.valueOf(DataUtils.getString(mActivity, DataUtils.CAR_LINE));
        mBrandName = TextUtils.isEmpty(DataUtils.getString(mActivity, DataUtils.CAR_NAME)) ? "选择品牌" : DataUtils.getString(mActivity, DataUtils.CAR_NAME);
        mBrandCarName = TextUtils.isEmpty(DataUtils.getString(mActivity, DataUtils.CAR_LINE_NAME)) ? "选择车系" : DataUtils.getString(mActivity, DataUtils.CAR_LINE_NAME);
        mBrandButton = (Button) view.findViewById(R.id.brand_btn);
        mCarlineButton = (Button) view.findViewById(R.id.carline_btn);
        mCarlineButton.setText(mBrandCarName);
        mBrandButton.setText(mBrandName);

        if(mBrandID != 0 && !TextUtils.isEmpty(mBrandName)){
            mBrandCar = new BrandCar();
            mBrandCar.setID(mBrandID);
            mBrandCar.setName(mBrandName);
        }

        if (mBrandCarID != 0 && !TextUtils.isEmpty(mBrandCarName)) {
            mCarLine = new BrandCarLine();
            mCarLine.setID(mBrandCarID);
            mCarLine.setName(mBrandCarName);
            mCarLine.setImageUrlHD(DataUtils.getString(this, DataUtils.CAR_PIC));
        }
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
                        DataUtils.putString(mActivity, DataUtils.CAR , mBrandID + "");
                        mBrandButton.setText(mBrandName);
                        DataUtils.putString(mActivity,DataUtils.CAR_NAME, mBrandName);
                        mCarlineButton.setText("选择车系");
                    }
                });
                mBrandDialog.show();
            }
        });


        mCarlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBrandCar == null) {
                    showToast("请先选择车的品牌");
                } else {
                    BrandDialog mBrandDialog = new BrandDialog(mActivity, mBrandCarID, mBrandID);
                    mBrandDialog.setmOnBrandItemClick(new BrandDialog.OnBrandItemClickListener() {
                        @Override
                        public void OnBrandItemClick(Object car) {
                            mCarLine = (BrandCarLine) car;
                            mBrandCarID = mCarLine.getID();
                            mBrandCarName =mCarLine.getName();
                            DataUtils.putString(mActivity, DataUtils.CAR_LINE , mBrandCarID + "");
                            DataUtils.putString(mActivity,DataUtils.CAR_LINE_NAME,mBrandCarName);
                            mCarlineButton.setText(mBrandCarName);
                        }
                    });
                    mBrandDialog.show();
                }
            }
        });

        if (mBrandCar != null) {
            mBrandID = mBrandCar.getID();
            mBrandButton.setText(mBrandName);
        }
        if (mCarLine != null) {
            mCarlineButton.setText(mBrandCarName);
        }
        mSearchCarlineButton = (Button) view.findViewById(R.id.search_carline);
        mSearchCarlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBrandCar == null) {
                    showToast("请选择车的品牌");
                } else if (mCarLine == null) {
                    showToast("请选择车的系列");
                } else {
                    String ImageUrl = mCarLine.getImageUrlHD();
                    String TitleCar = mCarLine.getName();
                    int carLineID = mCarLine.getID();
                    DataUtils.putString(mActivity, DataUtils.CAR_PIC, ImageUrl);
                    Intent intent = new Intent(mActivity, SearchCarActivity.class);
                    intent.putExtra(SearchCarActivity.PARAMS_PIC, ImageUrl);
                    intent.putExtra(SearchCarActivity.PARAMS_TITLE, TitleCar);
                    intent.putExtra(SearchCarActivity.PARAMS_ID, carLineID);
                    startActivity(intent);
                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAsDropDown(v, 0, 14);
    }

    /*//请求配件标题
    private void requestProducts() {
        Map<String, String> params = new HashMap<String, String>();
        Type type = new TypeToken<List<Products>>() {
        }.getType();
        requestGet(Constants.SEARCH_CAR_PRODUCT, params, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<Products> p = (List<Products>) result;
                mTextLabelView.addTag(p);
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }*/

   /* @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.search_turing:
                setTabSelection(0);
                break;

            case R.id.search_care:
                setTabSelection(1);
                break;

            case R.id.search_girl:
                setTabSelection(2);
                break;

            case R.id.search_video:
                setTabSelection(3);
                break;

            case R.id.search_motorzine:
                setTabSelection(4);
                break;

        }
    }*/

   /* //index   每个tab页对应的下标 0 改装实战，1 包养维修 ，2香车美女 ，3玩车视频 ，4 车讯

    private void setTabSelection(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle mBundle = new Bundle();
        mBundle.putInt("model", model);
        mBundle.putBoolean("refresh", refresh);
        hideFragments(transaction);
        switch (index) {
            case 0:
                mTurnSearchFragment = new TurnSearchFragment();
                mTurnSearchFragment.setArguments(mBundle);
                transaction.add(R.id.content_frameLayout, mTurnSearchFragment);
                transaction.show(mTurnSearchFragment);
                break;
            case 1:
                mCareSearchFragment = new CareSearchFragment();
                mCareSearchFragment.setArguments(mBundle);
                transaction.add(R.id.content_frameLayout, mCareSearchFragment);
                transaction.show(mCareSearchFragment);
                break;
            case 2:
                mGirlSearchFragment = new GirlSearchFragment();
                mGirlSearchFragment.setArguments(mBundle);
                transaction.add(R.id.content_frameLayout, mGirlSearchFragment);
                transaction.show(mGirlSearchFragment);
                break;
            case 3:
                mVideoSearchFragment = new VideoSearchFragment();
                mVideoSearchFragment.setArguments(mBundle);
                transaction.add(R.id.content_frameLayout, mVideoSearchFragment);
                transaction.show(mVideoSearchFragment);
                break;
            case 4:
                mMotorzineSearchFragment = new MotorzineSearchFragment();
                mMotorzineSearchFragment.setArguments(mBundle);
                transaction.add(R.id.content_frameLayout, mMotorzineSearchFragment);
                transaction.show(mMotorzineSearchFragment);
        }
        transaction.commit();

    }

    //Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        if (mTurnSearchFragment != null) {
            transaction.hide(mTurnSearchFragment);
        }
        if (mCareSearchFragment != null) {
            transaction.hide(mCareSearchFragment);
        }
        if (mGirlSearchFragment != null) {
            transaction.hide(mGirlSearchFragment);
        }
        if (mVideoSearchFragment != null) {
            transaction.hide(mVideoSearchFragment);
        }
        if (mMotorzineSearchFragment != null) {
            transaction.hide(mMotorzineSearchFragment);
        }
    }*/

}
