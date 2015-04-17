package com.xmyunyou.wcd.ui.circle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Topic;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.user.UserIndexActivity_;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import java.util.List;

/**
 * Created by sanmee on 2015/3/27.
 */
public class CircleAdapter extends BaseAdapter {

    private List<Topic> mTopicList;
    private BaseActivity mActivity;
    private LayoutInflater mInflater;
    public CircleAdapter(BaseActivity activity, List<Topic> list){
        mActivity = activity;
        mTopicList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mTopicList.size();
    }

    @Override
    public Topic getItem(int position) {
        return mTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopicHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.adapter_discuss, null);
            holder = new TopicHolder();
            holder.mCarModelTextView = (TextView) convertView.findViewById(R.id.adapter_car_mode);
            holder.mCarModelTextView.setVisibility(View.GONE);
            holder.mTitleTextView = (TextView) convertView.findViewById(R.id.adapter_car_content);
            holder.mPicImageView = (ImageView) convertView.findViewById(R.id.adapter_car_pic);
            holder.mUserLogoImageView = (ImageView) convertView.findViewById(R.id.adapter_car_user_pic);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_name);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.adapter_car_user_medal);
            holder.mDateTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_date);
            holder.mCommentTextView = (TextView) convertView.findViewById(R.id.adapter_car_user_comment);
            convertView.setTag(holder);
        }else{
            holder = (TopicHolder) convertView.getTag();
        }

        Topic t = getItem(position);
        holder.mCarModelTextView.setText("车型：" + t.getGameID());
        holder.mTitleTextView.setText(t.getTitle() + "");
        //List<String> list = t.getPictures();
        if(!TextUtils.isEmpty(t.getImageUrl())){
            holder.mPicImageView.setVisibility(View.VISIBLE);
            final String url = t.getImageUrl();
            mActivity.loadImg(url, 100, 80, holder.mPicImageView);
        }else{
            holder.mPicImageView.setVisibility(View.GONE);
        }
        User u = t.getTopicUser();
        holder.mUserLogoImageView.setOnClickListener(new UserOnClickListener(u));
        holder.mNameTextView.setOnClickListener(new UserOnClickListener(u));
        mActivity.loadImg(t.getTopicUser().getAvatarUrl(), 25, 25, holder.mUserLogoImageView);
        //设置勋章
        mActivity.createMedal(u.getMedals(), holder.mLinearLayout);
        holder.mNameTextView.setText(t.getTopicUser().getName());
        holder.mDateTextView.setText(Globals.convertDate(t.getCreateDate()));
        holder.mCommentTextView.setText(t.getReplyCount() + "");
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

    class TopicHolder {

        TextView mCarModelTextView;
        TextView mTitleTextView;
        ImageView mPicImageView;
        ImageView mUserLogoImageView;
        TextView mNameTextView;
        LinearLayout mLinearLayout;
        TextView mDateTextView;
        TextView mCommentTextView;

    }

}
