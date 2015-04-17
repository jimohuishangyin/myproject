package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by 95 on 2015/3/12.
 */
public class TopScrollView extends MyScrollView {

    View mTopView;
    View mFlowView;



    public TopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if(mTopView != null && mFlowView != null) {
            if(t >= mTopView.getHeight()) {
                mFlowView.setVisibility(View.VISIBLE);
            } else {
                mFlowView.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 监听浮动view的滚动状态
     * @param topView 顶部区域view，即当ScrollView滑动的高度要大于等于哪个view的时候隐藏floatview
     * @param flowView 浮动view，即要哪个view停留在顶部
     */
    public void listenerFlowViewScrollState(View topView, View flowView) {
        mTopView = topView;
        mFlowView = flowView;
    }
}
