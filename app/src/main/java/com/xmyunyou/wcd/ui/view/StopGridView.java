package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by 95 on 2015/3/19.
 */
public class StopGridView extends GridView {
    public StopGridView(Context context ,AttributeSet attrs) {
        super(context ,attrs);
    }
    public  StopGridView(Context context ,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
// TODO Auto-generated method stub
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }
}
