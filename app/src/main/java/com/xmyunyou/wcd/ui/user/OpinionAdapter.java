package com.xmyunyou.wcd.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Opinion;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;


/**
 * Created by 95 on 2015/1/15.
 */
public class OpinionAdapter extends BaseAdapter{

    private BaseActivity mActivity;
    private List<Opinion> mOpinionList;

    public OpinionAdapter (OpinionFeedbacksActivity activity,List<Opinion> list){
        mActivity =activity;
        mOpinionList =list;
    }
    @Override
    public int getCount() {
        return mOpinionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOpinionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpinionHolder holder = null;
        if(convertView == null){
            holder = new OpinionHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_opinionfeedback,null);
            holder.mTitleTextView = (TextView) convertView.findViewById(R.id.opinionfeedback_title);
            holder.mContentTextView = (TextView) convertView.findViewById(R.id.opinionfeedback_content);
            holder.mDatetimeTextView = (TextView) convertView.findViewById(R.id.opinionfeedback_datetime);
            convertView.setTag(holder);
        }else {
            holder = (OpinionHolder) convertView.getTag();
        }
        final Opinion o = mOpinionList.get(position);
        holder.mTitleTextView.setText(o.getName());
        holder.mContentTextView.setText(o.getContent());
        holder.mDatetimeTextView.setText(Globals.convertDate(o.getCreateDate()));
        return convertView;
    }

    class OpinionHolder{
        private TextView mTitleTextView;
        private TextView mContentTextView;
        private TextView mDatetimeTextView;
    }
}
