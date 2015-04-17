package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by sanmee on 2014/12/12.
 */
public class ShowListView extends ListView {

    public ShowListView(Context context, AttributeSet attrs)    {
        super(context, attrs);
    }

    public ShowListView(Context context) {
        super(context);
    }

    public ShowListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}