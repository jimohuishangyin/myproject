package com.xmyunyou.wcd.ui.gaizhuang.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.ProductArticle;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by 95 on 2015/3/23.
 */
public class ProductAdapter extends BaseAdapter {
    private List<ProductArticle> mList;
    private BaseActivity mActivity;

    public ProductAdapter(BaseActivity activity, List<ProductArticle> list) {
        mActivity = activity;
        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ProductArticle getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProductHolder holder = null;
        if (convertView == null) {
            holder = new ProductHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_product, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.gaizhuang_production_pic);
            holder.mTitleTextView = (TextView) convertView.findViewById(R.id.gaizhuang_production_title);
            holder.mTimeTextView = (TextView) convertView.findViewById(R.id.gaizhuang_production_time);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.gaizhuang_production_comment);
            convertView.setTag(holder);
        } else {
            holder = (ProductHolder) convertView.getTag();
        }
        final ProductArticle p = getItem(position);
        mActivity.loadImg(p.getImageUrl(),holder.mImageView);
        holder.mTitleTextView.setText(p.getTitle());
        holder.mTimeTextView.setText(Globals.convertDate(p.getCreateDate()));
        holder.mCommentTextView.setText(p.getCommentNums()+"");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoryID = 0;
                final int size = WanApp.mCategoryList.size();
                for (int index = 0; index < size; index++) {
                    Category c = WanApp.mCategoryList.get(index);
                    if (c.getID() == p.getCategoryID()) {
                        categoryID = c.getParentID();
                        break;
                    }
                }

                if (categoryID == 4) {
                    mActivity.startNewsDetail(getItem(position), 4);
                } else if(categoryID == 3){
                    mActivity.startPicDetail(p);
                }else{
                    mActivity.startNewsDetail(p);
                }

            }
        });

        return convertView;
    }

    class ProductHolder {

        ImageView mImageView;
        TextView mTitleTextView;
        TextView mTimeTextView;
        TextView mCommentTextView;

    }

}
