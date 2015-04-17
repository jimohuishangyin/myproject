package com.xmyunyou.wcd.ui.search;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.BrandCarLineType;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/4/14.
 */
public class BrandCarLineTypeAdapter extends BaseAdapter {
    private List<BrandCarLineType> mList;
    private BaseActivity mActivity;
    private int mBrandCarLineTypeID;

    public BrandCarLineTypeAdapter(BaseActivity activity, List<BrandCarLineType> list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BrandCarLineType getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BrandCarLineTypeHolder holder = null;
        if (convertView == null) {
            holder = new BrandCarLineTypeHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_brandcar, null);
            holder.mTextView = (TextView) convertView.findViewById(R.id.brandcar_content);
            convertView.setTag(holder);
        } else {
            holder = (BrandCarLineTypeHolder) convertView.getTag();
        }
        BrandCarLineType b = getItem(position);
        holder.mTextView.setText(b.getName());


        return convertView;
    }

    class BrandCarLineTypeHolder {
        TextView mTextView;
    }
}
