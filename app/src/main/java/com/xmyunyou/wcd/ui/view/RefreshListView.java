package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.xmyunyou.wcd.R;


/***
 * @author huangsm
 * @date 2013-6-5
 * @email huangsanm@gmail.com
 * @desc 可刷新listview
 */
public class RefreshListView extends ListView implements OnScrollListener {
   // private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoadingMore = false;
    private boolean mAutoLoadingMore = true;
    private int mVisibleItem;
    private RelativeLayout mFooterView;
    private ImageView mLoadingImageView;
    private Animation mAnimation;

    public RefreshListView(Context context) {
        super(context);

        initComponent(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initComponent(context);
    }

    public void initComponent(Context context) {
        mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.refresh_footer, this, false);

        /*//设置加载动画
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        mLoadingImageView = (ImageView) mFooterView.findViewById(R.id.loading_tips);
        mLoadingImageView.startAnimation(mAnimation);*/

        addFooterView(mFooterView);
        setOnScrollListener(this);
    }


    /*public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }*/

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mIsLoadingMore = firstVisibleItem + visibleItemCount >= totalItemCount;
    }

    public int getFirstVisibleIndex(){
        return mVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
            mVisibleItem = getFirstVisiblePosition();
            if(mIsLoadingMore && mAutoLoadingMore){
                //onLoadMore();
                if(mOnRefreshListener != null){
                    mOnRefreshListener.onPullUpRefresh();
                }
            }
        }
    }

   /* public void onLoadMore() {
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }
*/
    public void onRefreshComplete(boolean hasNextPage) {
        if(hasNextPage){
            mAutoLoadingMore = false;
            removeFooterView(mFooterView);
            //mLoadingImageView.startAnimation(mAnimation);
        }else{
            mAutoLoadingMore = true;
            if(getFooterViewsCount() == 0){
                addFooterView(mFooterView);
            }
        }


    }

   /* public interface OnLoadMoreListener {
        public void onLoadMore();
    }*/

    private onRefreshListener mOnRefreshListener;

	public void setOnRefreshListener(onRefreshListener refreshListener) {
		mOnRefreshListener = refreshListener;
	}

	public interface onRefreshListener {

		void onPullUpRefresh();

		//void onPullDownRefresh();

	}
}
