package com.xmyunyou.wcd.ui.main.turing;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.ui.main.MessageFragment;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by 95 on 2015/2/5.
 */
public class MessageAdapter extends BaseAdapter {

    private List<Article> mList;
    private BaseActivity mActivity;

    public MessageAdapter(BaseActivity activity, List<Article> list) {
        mActivity = activity;
        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Article getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder holder = null;
        if (convertView == null) {
            holder = new MessageHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_message, null);
            holder.mImageView = (ImageView)convertView.findViewById(R.id.message_image);
            holder.mTitleTextView = (TextView)convertView.findViewById(R.id.message_text);
            holder.mTimeTextView = (TextView)convertView.findViewById(R.id.adapter_message_view);
            holder.mCommentTextView = (TextView)convertView.findViewById(R.id.adapter_message_comment);
            convertView.setTag(holder);
        }else {
            holder = (MessageHolder) convertView.getTag();
        }
        final Article a = getItem(position);
//        mActivity.loadImg(a.getImageUrl(),holder.mImageView);
        mActivity.loadImg(a.getImageUrl(),100,100,holder.mImageView);
        holder.mTitleTextView.setText(a.getTitle());
        holder.mTimeTextView.setText(Globals.convertDate(a.getCreateDate()));
        holder.mCommentTextView.setText(a.getCommentNums()+"");
        return convertView;
    }

    class MessageHolder {

        ImageView mImageView;
        TextView mTitleTextView;
        TextView mTimeTextView;
        TextView mCommentTextView;

    }
}
