package com.xmyunyou.wcd.ui.user;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.JiFenLog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by 95 on 2015/4/13.
 */
public class UserJifenAdapter extends BaseAdapter{

    private List<JiFenLog> mList;
    private BaseActivity mActivity;

    public UserJifenAdapter(BaseActivity activity, List<JiFenLog> list){
        mActivity = activity;
        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public JiFenLog getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JifenHolder holder = null;
        if(convertView == null) {
            holder = new JifenHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_user_jifen, null);
            holder.method = (TextView) convertView.findViewById(R.id.record_method);
            holder.time = (TextView) convertView.findViewById(R.id.record_time);
            holder.jifen = (TextView) convertView.findViewById(R.id.record_jifen);
            convertView.setTag(holder);
        }else {
            holder = (JifenHolder) convertView.getTag();
        }
        JiFenLog jf = getItem(position);
        final Resources res = mActivity.getResources();
        holder.method.setTextColor(res.getColor(R.color.record_mothed));
        holder.method.setText(jf.getDescription());
        holder.time.setTextColor(res.getColor(R.color.record_time));
        holder.time.setText(Globals.convertDateHHMM(jf.getCreateDate()));

        if(jf.getJifen()>0){
            holder.jifen.setTextColor(res.getColor(R.color.record_jifen_red));
            holder.jifen.setText("+"+jf.getJifen());
        }else {
            holder.jifen.setTextColor(res.getColor(R.color.record_jifen_green));
            holder.jifen.setText(""+jf.getJifen());
        }

        return convertView;
    }

    class JifenHolder{
        TextView method;
        TextView time;
        TextView jifen;
    }
}
