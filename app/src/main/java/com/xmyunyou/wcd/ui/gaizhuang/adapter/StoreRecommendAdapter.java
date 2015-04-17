package com.xmyunyou.wcd.ui.gaizhuang.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/3/20.
 */
public class StoreRecommendAdapter extends BaseAdapter {

    private List<StopArticle> mList;
    private BaseActivity mActivity;

    public StoreRecommendAdapter(BaseActivity activity, List<StopArticle> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public StopArticle getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreRecommendHolder  holder =null;
        if(convertView == null){
            holder = new StoreRecommendHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adpter_storerecommend,null);
            holder.mImageView = (ImageView)convertView.findViewById(R.id.gaizhuang_storerecommend_img);
            holder.mTextView = (TextView)convertView.findViewById(R.id.gaizhuang_storerecommend_text);
            convertView.setTag(holder);
        }else {
            holder = (StoreRecommendHolder) convertView.getTag();
        }
        StopArticle a = getItem(position);
        mActivity.loadImg(a.getImageUrl(),holder.mImageView);
        holder.mTextView.setText(a.getName());
        return convertView;
    }

    class StoreRecommendHolder{
        private ImageView mImageView;
        private TextView mTextView;

    }
}
