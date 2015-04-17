package com.xmyunyou.wcd.ui.user.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.CheckIns;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/4/10.
 */
public class SignAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<CheckIns> mList;
    private LayoutInflater mInflater;
    public SignAdapter(BaseActivity activity, List<CheckIns> list){
        mActivity = activity;
        mList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CheckIns getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.adapter_user_index, null);
        TextView title = (TextView) convertView.findViewById(R.id.adapter_text);
        TextView date = (TextView) convertView.findViewById(R.id.adapter_date);

        CheckIns t = mList.get(position);
        title.setText(t.getContent());
        date.setText(Globals.convertDateHHMM(t.getCreateDate()));
        return convertView;
    }

}
