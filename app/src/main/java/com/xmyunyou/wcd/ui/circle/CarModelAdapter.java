package com.xmyunyou.wcd.ui.circle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by sanmee on 2015/3/30.
 */
public class CarModelAdapter extends BaseAdapter {

    private List<Article> mArticleList;
    private BaseActivity mActivity;
    private LayoutInflater mInflater;
    public CarModelAdapter(List<Article> list, BaseActivity activity){
        mActivity = activity;
        mArticleList = list;
        mInflater = mActivity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mArticleList.size();
    }

    @Override
    public Article getItem(int position) {
        return mArticleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.adapter_car_model, null);
        ImageView pic = (ImageView) convertView.findViewById(R.id.adapter_car_pic);
        TextView tv = (TextView) convertView.findViewById(R.id.adapter_car_name);

        Article a = mArticleList.get(position);
        tv.setText(a.getTitle());

        mActivity.loadImg(a.getImageUrlHD(), 150, 110, pic);
        return convertView;
    }
}
