package com.xmyunyou.wcd.ui.user.info;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.CheckIns;
import com.xmyunyou.wcd.model.Integral;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/4/10.
 */
public class IntegralAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<Integral> mList;
    private LayoutInflater mInflater;
    public IntegralAdapter(BaseActivity activity, List<Integral> list){
        mActivity = activity;
        mList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Integral getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.adapter_user_index_integral, null);
        TextView title = (TextView) convertView.findViewById(R.id.adapter_text);
        TextView value = (TextView) convertView.findViewById(R.id.adapter_value);
        TextView date = (TextView) convertView.findViewById(R.id.adapter_date);

        Integral t = mList.get(position);
        title.setText(t.getDescription());
        date.setText(Globals.convertDateHHMM(t.getCreateDate()));
        final Resources res = mActivity.getResources();
        if(t.getJifen() > 0) {
            value.setTextColor(res.getColor(R.color.bg_title));
            value.setText("+" + t.getJifen());
        }else{
            value.setTextColor(res.getColor(R.color.color2));
            value.setText("-" + t.getJifen());
        }
        return convertView;
    }

}
