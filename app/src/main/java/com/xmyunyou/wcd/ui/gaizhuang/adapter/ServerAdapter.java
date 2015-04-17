package com.xmyunyou.wcd.ui.gaizhuang.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Services;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/3/18.
 */
public class ServerAdapter extends BaseAdapter {

    private List<Services> mList;
    private BaseActivity mActivity;

    public ServerAdapter(BaseActivity activity, List<Services> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Services getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServerHolder holder =null;
        if(convertView == null){
            holder = new ServerHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_server,null);
            holder.mImageView = (ImageView)convertView.findViewById(R.id.gaizhuang_server_img);
            holder.mTextView = (TextView)convertView.findViewById(R.id.gaizhuang_server_title);
            convertView.setTag(holder);
        }else {
            holder = (ServerHolder) convertView.getTag();
        }
       Services a =  getItem(position);
        mActivity.loadImg(a.getImageUrl(),holder.mImageView);
        holder.mTextView.setText(a.getName());
        return convertView;

    }

    class ServerHolder{
        ImageView  mImageView;
        TextView mTextView;


    }
}
