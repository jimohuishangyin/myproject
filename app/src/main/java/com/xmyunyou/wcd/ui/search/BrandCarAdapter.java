package com.xmyunyou.wcd.ui.search;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.BrandCar;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by 95 on 2015/1/22.
 */
public class BrandCarAdapter extends BaseAdapter {
    private List<BrandCar> mList;
    private BaseActivity mActivity;
    private int mBrandID;

    public BrandCarAdapter(BaseActivity activity, List<BrandCar> list){
        mList = list;
        mActivity = activity;
    }

    public void setBrandID(int id){
        mBrandID = id;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BrandCar getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandCarHolder holder = null;
        if(convertView ==null){
            holder = new BrandCarHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_brandcar, null);
            holder.mTextView =(TextView) convertView.findViewById(R.id.brandcar_content);
            convertView.setTag(holder);
        }else{
            holder = (BrandCarHolder) convertView.getTag();
        }
        final BrandCar b = (BrandCar) getItem(position);
       holder.mTextView.setSelected(b.getID() == mBrandID);
        holder.mTextView.setText(b.getName());
        return convertView;
    }

    class BrandCarHolder{
        TextView mTextView;
    }
}
