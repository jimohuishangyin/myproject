package com.xmyunyou.wcd.ui.car;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.ArticleComment;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2014/12/17.
 */
public class CommentAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<ArticleComment> mList;
    public CommentAdapter(BaseActivity activity, List<ArticleComment> list){
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

        convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_comment, null);

        TextView name = (TextView) convertView.findViewById(R.id.comment_name);
        TextView date = (TextView) convertView.findViewById(R.id.comment_date);
        TextView content = (TextView) convertView.findViewById(R.id.comment_content);

        ArticleComment c = mList.get(position);
        name.setText(c.getUserName() + "");
        if(c.getID() > 0){
            date.setText(Globals.convertDate(c.getCreateDate()));
        }else{
            date.setText(c.getCreateDate());
        }
        content.setText(c.getContent());

        return convertView;
    }
}
