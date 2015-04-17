package com.xmyunyou.wcd.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by sanmee on 2014/12/11.
 */
public class ImageViewPagerAdapter extends PagerAdapter {

    private List<ImageView> mView ;
    public ImageViewPagerAdapter(List<ImageView> views) {
        mView = views;
    }

    @Override
    public int getCount() {
        return mView.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1 ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mView.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mView.get(position);
        container.addView(view);
        return view;
    }


}
