package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author huangsm
 * @date 2014年7月2日
 * @email huangsanm@gmail.com
 * @desc
 */
public class MyScrollView extends ScrollView {

    private OnScrollToBottomListener mListener;

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       // init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public MyScrollView(Context context) {
        super(context);
       // init();
    }

    /*private void init(){
        mGestureDetector = new GestureDetector(new YScrollDetector());
        canScroll = true;
    }*/

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(getScrollY() + getHeight() >= computeVerticalScrollRange()){
            if(mListener != null){
                mListener.onScrollBottomListener();
            }
        }
    }

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
        mListener = listener;
    }

    public interface OnScrollToBottomListener {
        // 当前滑动的距离
        public void onScrollBottomListener();
    }

    /*private boolean canScroll;

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP)
            canScroll = true;
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(canScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }
    }*/
}
