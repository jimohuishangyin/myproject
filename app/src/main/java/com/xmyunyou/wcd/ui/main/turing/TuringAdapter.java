package com.xmyunyou.wcd.ui.main.turing;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.ui.car.NewsListActivity;
import com.xmyunyou.wcd.ui.car.NewsListActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/1/12.
 */
public class TuringAdapter extends BaseAdapter {

    private List<Article> mList;
    private BaseActivity mActivity;
    private int mImage;
    private int mItemHeight;
    public TuringAdapter(BaseActivity activity, List<Article> list){
        mList = list;
        mActivity = activity;
    }
    public TuringAdapter(BaseActivity activity, List<Article> list,int image){
        mList = list;
        mActivity = activity;
        mImage =image;
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
        TuringHolder holder = null;
        if(convertView == null){
            holder = new TuringHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_turing, null);
            holder.mPicImageView = (ImageView) convertView.findViewById(R.id.adapter_turing_pic);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.adapter_turing_name);
            holder.mViewTextView = (TextView) convertView.findViewById(R.id.adapter_turing_view);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.adapter_turing_comment);
            holder.mCategoryTextView = (TextView) convertView.findViewById(R.id.adapter_turing_category);
            holder.mImageView = (ImageView)convertView.findViewById(R.id.ic_video);
            convertView.setTag(holder);
        }else{
            holder = (TuringHolder) convertView.getTag();
        }
        if(mImage==1){
            holder.mImageView.setVisibility(View.VISIBLE);
        }
        final Article a = getItem(position);

        mActivity.loadImg(a.getImageUrl145145(), holder.mPicImageView);
        holder.mNameTextView.setText(a.getTitle());
        holder.mViewTextView.setText(Globals.convertDate(a.getCreateDate()));
        holder.mCommentTextView.setText(a.getCommentNums()+"");
        if(!WanApp.mCategoryList.isEmpty()){
            final String name = queryCategoryName(a.getCategoryID());
            holder.mCategoryTextView.setVisibility(View.VISIBLE);
            holder.mCategoryTextView.setText(name);
            holder.mCategoryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, NewsListActivity_.class);
                    intent.putExtra(NewsListActivity.PARAMS_NEWS_ID, a.getCategoryID());
                    intent.putExtra(NewsListActivity.PARAMS_NEWS_TITLE, name);
                    mActivity.startActivity(intent);
                }
            });
        }else{
            holder.mCategoryTextView.setVisibility(View.GONE);
        }
        mItemHeight = convertView.getMeasuredHeight();
        return convertView;
    }

    public int getItemHeight(){
        return mItemHeight;
    }

    public String queryCategoryName(int id){
        String name = "";
        final int size = WanApp.mCategoryList.size();
        for (int i = 0; i < size; i ++){
            Category c = WanApp.mCategoryList.get(i);
            if(c.getID() == id){
                name = c.getName();
                break;
            }
        }
        return name;
    }

    class TuringHolder {
        ImageView mPicImageView;
        TextView mNameTextView;
        TextView mViewTextView;
        TextView mCommentTextView;
        TextView mCategoryTextView;
        ImageView mImageView;
    }

}
