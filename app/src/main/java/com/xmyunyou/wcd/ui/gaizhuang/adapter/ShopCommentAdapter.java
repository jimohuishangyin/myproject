package com.xmyunyou.wcd.ui.gaizhuang.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Gaizhuang_stop_comment;
import com.xmyunyou.wcd.model.json.Gaizhuang_shop_commentlist;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by 95 on 2015/4/9.
 */
public class ShopCommentAdapter extends BaseAdapter {

    private List<Gaizhuang_stop_comment> mList;
    private BaseActivity mActivity;

    public ShopCommentAdapter(BaseActivity activity, List<Gaizhuang_stop_comment> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopCommentHolder holder = null;
        if (convertView == null) {
            holder = new ShopCommentHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_comment, null);
            holder.mUserNameTextView = (TextView) convertView.findViewById(R.id.comment_name);
            holder.mContentTextView = (TextView) convertView.findViewById(R.id.comment_content);
            holder.mTimeTextView = (TextView) convertView.findViewById(R.id.comment_date);
            convertView.setTag(holder);
        } else {
            holder = (ShopCommentHolder) convertView.getTag();
        }
        Gaizhuang_stop_comment c = mList.get(position);
        holder.mUserNameTextView.setText(c.getUserName());
        holder.mContentTextView.setText(c.getContent());
        holder.mTimeTextView.setText(Globals.convertDate(c.getCreateDate()));
        return convertView;
    }

    class ShopCommentHolder{
        TextView mUserNameTextView;
        TextView mContentTextView;
        TextView mTimeTextView;
    }
}
