package com.xmyunyou.wcd.ui.circle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Reply;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by sanmee on 2015/4/9.
 */
public class SquareAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<Topic> mList;
    private LayoutInflater mInflater;
    public SquareAdapter(BaseActivity activity, List<Topic> list){
        mActivity = activity;
        mList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Topic getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.adapter_square, null);
        ImageView pic = (ImageView) convertView.findViewById(R.id.adapter_pic);
        TextView title = (TextView) convertView.findViewById(R.id.adapter_label);

        mActivity.loadImg(mList.get(position).getImageUrl(), mActivity.dip2px(180), mActivity.dip2px(120), pic);
        title.setText(mList.get(position).getTitle());
        return convertView;
    }

}
