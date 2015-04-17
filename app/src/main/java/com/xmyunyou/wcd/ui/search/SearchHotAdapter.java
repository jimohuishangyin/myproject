package com.xmyunyou.wcd.ui.search;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.SearchHot;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/2/3.
 */
public class SearchHotAdapter extends BaseAdapter {
    private List<SearchHot> mList;
    private BaseActivity mActivity;

    public SearchHotAdapter(BaseActivity activity, List<SearchHot> list){
        mActivity =activity;
        mList=list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_search_hot, null);
        ImageView pic = (ImageView) convertView.findViewById(R.id.hot_pic);
        TextView title = (TextView) convertView.findViewById(R.id.hot_title);
        ImageView enter = (ImageView) convertView.findViewById(R.id.hot_enter);
        SearchHot s = mList.get(position);
        mActivity.loadImg(s.getImageUrlHD(),pic);
        title.setText(s.getName());
        return convertView;
    }
}
