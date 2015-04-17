package com.xmyunyou.wcd.ui.user.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/4/10.
 */
public class TopicLogAdatper extends BaseAdapter {

    private BaseActivity mActivity;
    private List<Topic> mList;
    private LayoutInflater mInflater;
    public TopicLogAdatper(BaseActivity activity, List<Topic> list){
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
        convertView = mInflater.inflate(R.layout.adapter_user_index, null);
        TextView title = (TextView) convertView.findViewById(R.id.adapter_text);
        TextView date = (TextView) convertView.findViewById(R.id.adapter_date);

        Topic t = mList.get(position);
        title.setText(t.getTitle());
        date.setText(Globals.convertDateHHMM(t.getCreateDate()));
        return convertView;
    }


}
