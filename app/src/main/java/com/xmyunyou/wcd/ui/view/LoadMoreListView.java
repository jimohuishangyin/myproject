package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;

/**
 * Created by huangsm on 2014/9/9 0009.
 * Email:huangsanm@foxmail.com
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoadingMore = false;
    private boolean mAutoLoadingMore = true;

    private RelativeLayout mFooterView;

    private Context mContext;
    private View mEmptyView;
    public LoadMoreListView(Context context) {
        super(context);

        initComponent(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initComponent(context);
    }

    public void initComponent(Context context) {
        mContext = context;
        mEmptyView = createEmptyView();
        mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.refresh_footer, this, false);
        ImageView mImageView = (ImageView) mFooterView.findViewById(R.id.frame_image);
        AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(R.drawable.progress_large_loading);
        mImageView.setBackgroundDrawable(anim);
        anim.start();
        addFooterView(mFooterView);
        setOnScrollListener(this);
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mIsLoadingMore = firstVisibleItem + visibleItemCount >= totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
            if(mIsLoadingMore && mAutoLoadingMore){
                onLoadMore();
            }
        }
    }

    public void onLoadMore() {
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void onLoadMoreComplete(boolean hasNextPage) {
        if(hasNextPage){
            mAutoLoadingMore = false;
            removeFooterView(mFooterView);
        }else{
            mAutoLoadingMore = true;
            if(getFooterViewsCount() == 0){
                addFooterView(mFooterView);
            }
        }

        if(getAdapter().isEmpty()){
            ViewGroup parentView = (ViewGroup) getParent();
            parentView.addView(mEmptyView, 0); // 你需要在这儿设置正确的位置，以达到你需要的效果。可能还需要正确的LayoutParamater参数
            setEmptyView(mEmptyView);
        }
    }

    private View createEmptyView(){
        TextView view = new TextView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 0);
        view.setLayoutParams(lp);
        view.setText("暂时没有数据");
        view.setGravity(Gravity.CENTER);
        view.setTextSize(16);
        view.setTextColor(mContext.getResources().getColor(R.color.bg_title));
        return view;
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
