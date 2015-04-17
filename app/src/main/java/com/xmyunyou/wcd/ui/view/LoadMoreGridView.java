package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;


import com.xmyunyou.wcd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/1/28.
 */
public class LoadMoreGridView extends LinearLayout {

    public static final int COLUMNS_MARGIN = 5;
    public static final int COLUMNS_NUM = 2;

    private Context mContext;

    private View mFooterView;
    private Animation mAnimation;
    private ImageView mAnimImageView;


    //单元格之间间距
    private int mGridViewHorizontalVertical = COLUMNS_MARGIN;
    //显示列数
    private int mNumColumns = COLUMNS_NUM;

    //gridView
    private ShowGridView mGridView;

    private List<ShowGridView> mGridViewList;

    public LoadMoreGridView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mContext = context;
        setOrientation(VERTICAL);
        mGridViewList = new ArrayList<ShowGridView>();
        if(attrs != null){
            //获取自定义属性
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.LoadMoreGridView);
            int numColumns = ta.getInt(R.styleable.LoadMoreGridView_numColumns, 1);
            int itemMargin = ta.getInt(R.styleable.LoadMoreGridView_item_margin, 5);

            mGridViewHorizontalVertical = itemMargin;
            mNumColumns = numColumns;
        }

        createContentView();

        //加载部分
        createFootView();
    }

    public ShowGridView getGridView(){
        return mGridView;
    }

    private void createContentView(){
        mGridView = new ShowGridView(mContext);
        mGridView.setNumColumns(mNumColumns);
        mGridView.setPadding(10, 0, 10, 0);
        mGridView.setVerticalSpacing(mGridViewHorizontalVertical);
        mGridView.setHorizontalSpacing(mGridViewHorizontalVertical);
        mGridView.setSelector(android.R.color.transparent);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(adapterView, view, i, l);
                }
            }
        });
        addView(mGridView);
    }

    private void createFootView(){
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, null);
        //设置加载动画
//        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
//        mAnimImageView = (ImageView) mFooterView.findViewById(R.id.loading_tips);
//        mAnimImageView.startAnimation(mAnimation);
        ImageView mImageView = (ImageView) mFooterView.findViewById(R.id.frame_image);
        AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(R.drawable.progress_large_loading);
        mImageView.setBackgroundDrawable(anim);
        anim.start();
        addView(mFooterView);
    }

    public void setAdapter(ListAdapter adapter){
        mGridView.setAdapter(adapter);
    }

    public ListAdapter getAdapter(){
        return mGridView.getAdapter();
    }

    public void onLoadComplete(boolean hasNext){
        if(hasNext){
            removeView(mFooterView);
        } else {
            if(getChildCount() < 2){
                addView(mFooterView);
            }
//            mAnimImageView.startAnimation(mAnimation);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

}
