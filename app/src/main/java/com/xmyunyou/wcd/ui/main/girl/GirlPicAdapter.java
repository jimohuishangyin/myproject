package com.xmyunyou.wcd.ui.main.girl;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.ui.car.PicDetailActivity;
import com.xmyunyou.wcd.ui.car.PicDetailActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.List;

/**
 * Created by sanmee on 2015/1/14.
 */
public class GirlPicAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    //    private List<List<Article>> mList;
    private List<Article> mList;

    public GirlPicAdapter(BaseActivity activity, List<Article> list) {
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
        ImageViewHolder holder = null;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_girl_pic, null);
            holder = new ImageViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.adapter_pic);
            holder.mTextView = (TextView) convertView.findViewById(R.id.adapter_pic_text);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.adapter_gril_comment);
//            holder.mOneImageView = (ImageView) convertView.findViewById(R.id.adapter_pic_one);
//            holder.mTwoImageView = (ImageView) convertView.findViewById(R.id.adapter_pic_two);
//            //holder.mThreeImageView = (ImageView) convertView.findViewById(R.id.adapter_pic_three);
//            holder.mFourImageView = (ImageView) convertView.findViewById(R.id.adapter_pic_four);
            convertView.setTag(holder);
        } else {
            holder = (ImageViewHolder) convertView.getTag();
        }

//        List<Article> alist = getItem(position);
        final Article a = getItem(position);
        mActivity.loadImg(a.getImageUrlHD(), holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startPicDetail(a);
            }
        });
        holder.mTextView.setText(a.getTitle());
        holder.mCommentTextView.setText(a.getCommentNums()+"");
//        holder.mOneImageView.setOnClickListener(new OnImageClickListener(alist.get(0)));
//        holder.mTwoImageView.setOnClickListener(new OnImageClickListener(alist.get(1)));
//        //holder.mThreeImageView.setOnClickListener(new OnImageClickListener(alist.get(2)));
//        holder.mFourImageView.setOnClickListener(new OnImageClickListener(alist.get(2)));
//        mActivity.loadImg(alist.get(0).getImageUrl145145(), 150, 100, holder.mOneImageView);
//        mActivity.loadImg(alist.get(1).getImageUrl145145(), 120, 100, holder.mTwoImageView);
//        //mActivity.loadImg(alist.get(2).getImageUrl145145(), holder.mThreeImageView);
//        mActivity.loadImg(alist.get(2).getImageUrl145145(), 120, 100, holder.mFourImageView);
        return convertView;
    }

    /*public class OnImageClickListener implements View.OnClickListener {

        private Article mA;

        public OnImageClickListener(Article a) {
            mA = a;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, PicDetailActivity_.class);
            intent.putExtra(PicDetailActivity.PARAMS_NEWS_ID, mA.getID());
            mActivity.startActivity(intent);
        }
    }*/


    class ImageViewHolder {

        ImageView mImageView;
        TextView mTextView;
        TextView mCommentTextView;
/*        ImageView mOneImageView;
        ImageView mTwoImageView;
        ImageView mThreeImageView;
        ImageView mFourImageView;*/

    }


}
