package com.xmyunyou.wcd.ui.search;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.BrandCarLine;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/1/23.
 */
public class BrandCarLineAdapter extends BaseAdapter {
    private List<BrandCarLine> mList;
    private BaseActivity mActivity;
    private int mBrandID;

    public BrandCarLineAdapter(BaseActivity activity, List<BrandCarLine> list) {
        mList = list;
        mActivity = activity;
    }

    public void setBrandCarID(int id) {
        mBrandID = id;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BrandCarLine getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandCarLineHolder holder = null;
        if (convertView == null) {
            holder = new BrandCarLineHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_brandcar, null);
            holder.mTextView = (TextView) convertView.findViewById(R.id.brandcar_content);
            convertView.setTag(holder);
        } else {
            holder = (BrandCarLineHolder) convertView.getTag();
        }
        final BrandCarLine b = getItem(position);
        holder.mTextView.setSelected(b.getID() == mBrandID);
        holder.mTextView.setText(b.getName());
        return convertView;
    }

    class BrandCarLineHolder {
        TextView mTextView;
    }
}
