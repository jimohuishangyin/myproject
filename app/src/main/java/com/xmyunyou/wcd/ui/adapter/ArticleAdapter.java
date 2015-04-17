package com.xmyunyou.wcd.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.model.MyFav;
import com.xmyunyou.wcd.model.MyFavValue;
import com.xmyunyou.wcd.ui.car.NewsListActivity;
import com.xmyunyou.wcd.ui.car.NewsListActivity_;
import com.xmyunyou.wcd.ui.dialog.TipDialog;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/1/12.
 */
public class ArticleAdapter extends BaseAdapter {

    private List<Article> mList;
    private BaseActivity mActivity;
    private boolean mFav = false;

    public ArticleAdapter(BaseActivity activity, List<Article> list) {
        mList = list;
        mActivity = activity;
    }

    public ArticleAdapter(BaseActivity activity, List<Article> list, boolean isFav) {
        mList = list;
        mActivity = activity;
        mFav = isFav;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TuringHolder holder = null;
        if (convertView == null) {
            holder = new TuringHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_article, null);
            holder.mPicImageView = (ImageView) convertView.findViewById(R.id.adapter_turing_pic);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.adapter_turing_name);
            holder.mViewTextView = (TextView) convertView.findViewById(R.id.adapter_turing_view);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.adapter_turing_comment);
            holder.mCategoryTextView = (TextView) convertView.findViewById(R.id.adapter_turing_category);
            holder.mDelImageView = (ImageView) convertView.findViewById(R.id.adapter_delete_fav);
            convertView.setTag(holder);
        } else {
            holder = (TuringHolder) convertView.getTag();
        }

        final Article a = getItem(position);

        mActivity.loadImg(a.getImageUrl145145(), holder.mPicImageView);
        holder.mNameTextView.setText(a.getTitle());
        holder.mViewTextView.setText(Globals.convertDate(a.getCreateDate()));
        holder.mCommentTextView.setText(a.getCommentNums() + "");
        holder.mCategoryTextView.setText(a.getCategoryName() + "");
        if (!WanApp.mCategoryList.isEmpty()) {
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
        } else {
            holder.mCategoryTextView.setVisibility(View.GONE);
        }
        if (mFav) {
            holder.mDelImageView.setVisibility(View.VISIBLE);
            holder.mDelImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delFave(position);
                }
            });
        } else {
            holder.mDelImageView.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoryID = 0;
                final int size = WanApp.mCategoryList.size();
                for (int index = 0; index < size; index++) {
                    Category c = WanApp.mCategoryList.get(index);
                    if (c.getID() == a.getCategoryID()) {
                        categoryID = c.getParentID();
                        break;
                    }
                }

                if (categoryID == 4) {
                    mActivity.startNewsDetail(getItem(position), 4);
                } else if(categoryID == 3){
                    mActivity.startPicDetail(a);
                }else{
                    mActivity.startNewsDetail(a);
                }

            }
        });
        return convertView;
    }

    public String queryCategoryName(int id) {
        String name = "";
        final int size = WanApp.mCategoryList.size();
        for (int i = 0; i < size; i++) {
            Category c = WanApp.mCategoryList.get(i);
            if (c.getID() == id) {
                name = c.getName();
                break;
            }
        }
        return name;
    }

    //删除收藏
    private void delFave(final int position) {
        final Article g = getItem(position);
        new TipDialog(mActivity, g.getTitle(), g.getFavID()).setOnSuccessListener(new TipDialog.OnSuccessListener() {
            @Override
            public void onSuccess(TipDialog dialog) {
                mList.remove(position);
                notifyDataSetChanged();
                //TODO; json 先把缓存的json数据取出来，循环缓存中收藏数据，找到喝当前记录ID一样的值，删除当前记录，然后更新缓存
                List<MyFavValue> values = DataUtils.getMyFavList(mActivity);
                if (values != null) {

                    for (MyFavValue v : values) {
                        if (v.getFavID() == g.getFavID()) {
                            boolean is = values.remove(v);
                            break;
                        }
                    }
//                    isFav = false;
                    String jsonStr = new Gson().toJson(values);
//                    System.out.println("dddddddddddddddddddddddddjson:" + jsonStr);
                    DataUtils.putString(mActivity, DataUtils.COLLECTION, jsonStr);
                }

            }
        }).show();
    }

    class TuringHolder {
        ImageView mPicImageView;
        ImageView mDelImageView;
        TextView mNameTextView;
        TextView mViewTextView;
        TextView mCommentTextView;
        TextView mCategoryTextView;
    }

}
