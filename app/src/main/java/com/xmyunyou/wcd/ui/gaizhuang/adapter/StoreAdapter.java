package com.xmyunyou.wcd.ui.gaizhuang.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.StopArticle;
import com.xmyunyou.wcd.ui.baidu.AddressActivity;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 95 on 2015/3/20.
 */
public class StoreAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<StopArticle> mList;

    public StoreAdapter(BaseActivity activity, List<StopArticle> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public StopArticle getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreHolder holder = null;
        if(convertView == null){
            holder  = new StoreHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_store,null);
            holder.mPicImageView = (ImageView)convertView.findViewById(R.id.gaizhuang_store_pic);
            holder.mNameTextView = (TextView)convertView.findViewById(R.id.gaizhuang_store_name);
            holder.mStarImageView = (ImageView)convertView.findViewById(R.id.gaizhuang_store_star);
            holder.mCategoryTextView = (TextView)convertView.findViewById(R.id.gaizhuang_store_category);
            holder.mAddressText = (TextView)convertView.findViewById(R.id.gaizhuang_store_address);
            holder.mAddressLinearLayout = (LinearLayout)convertView.findViewById(R.id.gaizhuang_address);
            convertView.setTag(holder);
        }else {
            holder = (StoreHolder) convertView.getTag();
        }
        final StopArticle a = getItem(position);
        mActivity.loadImg(a.getImageUrl(),holder.mPicImageView);
        holder.mNameTextView.setText(a.getName());
        int star = a.getStar();
        holder.mStarImageView.setImageResource(Globals.createDrawableByIdentifier(mActivity,"s"+star));
        holder.mAddressText.setText(a.getAddress());
        holder.mAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddressActivity.class);
                intent.putExtra("address",a.getAddress());
                intent.putExtra("cityID",a.getCityID());
                mActivity.startActivity(intent);
            }
        });
        int category = a.getCategoryID();
        if(category == 4){
            holder.mCategoryTextView.setText("类    别：改装店");
        }else if(category ==5){
            holder.mCategoryTextView.setText("类    别：包养美容维修点");
        }else if(category ==13){
            holder.mCategoryTextView.setText("类    别：配件店");
        }
        return convertView;
    }

    class StoreHolder{
        ImageView mPicImageView;
        TextView mNameTextView;
        ImageView mStarImageView;
        TextView mCategoryTextView;
        TextView mAddressText;
        LinearLayout mAddressLinearLayout;

    }
}
