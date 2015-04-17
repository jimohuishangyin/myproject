package com.xmyunyou.wcd.ui.user.Fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.MyFavTopic;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.user.UserIndexActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/4/17.
 */
public class FavPostsAdapter extends BaseAdapter {

    private List<MyFavTopic> mList;
    private BaseActivity mActivity;

    public FavPostsAdapter(BaseActivity activity,List<MyFavTopic> topicList){
        mActivity = activity;
        mList = topicList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MyFavTopic getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyFavTopicHolder holder = null;
        if(convertView == null){
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_discuss, null);
            holder = new MyFavTopicHolder();
            holder.mCarModelTextView = (TextView) convertView.findViewById(R.id.adapter_car_mode);
            holder.mCarModelTextView.setVisibility(View.GONE);
            holder.mTitleTextView = (TextView) convertView.findViewById(R.id.adapter_car_content);
            holder.mPicImageView = (ImageView) convertView.findViewById(R.id.adapter_car_pic);
            holder.mUserLogoImageView = (ImageView) convertView.findViewById(R.id.adapter_car_user_pic);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_name);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.adapter_car_user_medal);
            holder.mDateTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_date);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_comment);
            holder.mDeleteFavTopicImageView = (ImageView) convertView.findViewById(R.id.delete_fav_topic);
            holder.mLineImageView = (ImageView)convertView.findViewById(R.id.line);
            holder.mLineImageView.setVisibility(View.VISIBLE);
            holder.mDeleteFavTopicImageView.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }else {
            holder = (MyFavTopicHolder) convertView.getTag();
        }

        MyFavTopic t = getItem(position);
        holder.mCarModelTextView.setText("车型+");

        ArrayList<String> Pictures = t.getTopic().getPictures();

       System.out.println("ddddddddddddddddddgetPictures"+Pictures);


        /*final MyFavTopic t = getItem(position);
        holder.mCarModelTextView.setText("车型：" + t.getTopic().getGameID());
        holder.mTitleTextView.setText(t.getTopic().getTitle() + "");
        if(!TextUtils.isEmpty(t.getTopic().getImageUrl())){
            holder.mPicImageView.setVisibility(View.VISIBLE);
            final String url = t.getTopic().getImageUrl();
            mActivity.loadImg(url, 100, 80, holder.mPicImageView);
        }else{
            holder.mPicImageView.setVisibility(View.GONE);
        }
        User u = t.getTopic().getTopicUser();
        holder.mUserLogoImageView.setOnClickListener(new UserOnClickListener(u));
        holder.mNameTextView.setOnClickListener(new UserOnClickListener(u));
        mActivity.loadImg(t.getTopic().getTopicUser().getAvatarUrl(), 25, 25, holder.mUserLogoImageView);
        //设置勋章
        mActivity.createMedal(u.getMedals(), holder.mLinearLayout);
        holder.mNameTextView.setText(t.getTopic().getTopicUser().getName());
        holder.mDateTextView.setText(Globals.convertDate(t.getTopic().getCreateDate()));
        holder.mCommentTextView.setText(t.getTopic().getReplyCount() + "");*/
        return convertView;
    }

    class UserOnClickListener implements View.OnClickListener {

        private User mUser;

        public UserOnClickListener(User user){
            mUser = user;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.adapter_car_user_pic:
                case R.id.adapter_car_user_name:
                    UserIndexActivity_.intent(mActivity).mUser(mUser).start();
                    break;
            }
        }
    }

    class MyFavTopicHolder {

        TextView mCarModelTextView;
        TextView mTitleTextView;
        ImageView mPicImageView;
        ImageView mUserLogoImageView;
        TextView mNameTextView;
        LinearLayout mLinearLayout;
        TextView mDateTextView;
        TextView mCommentTextView;
        ImageView mDeleteFavTopicImageView;
        ImageView mLineImageView;

    }

}
