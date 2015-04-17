package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by 95 on 2015/3/5.
 */
public class HackyViewPager extends ViewPager {
    private static final String TAG = "HackyViewPager";

    private boolean scrollble = true;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {

            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //不理会
            Log.e(TAG, "hacky viewpager error1");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            //不理会
            Log.e(TAG, "hacky viewpager error2");
            return false;
        }

    }
}
