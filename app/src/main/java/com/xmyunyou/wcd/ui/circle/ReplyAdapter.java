package com.xmyunyou.wcd.ui.circle;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Reply;
import com.xmyunyou.wcd.model.User;
import com.xmyunyou.wcd.ui.circle.face.FaceManager;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sanmee on 2015/4/8.
 */
public class ReplyAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<Reply> mList;
    private LayoutInflater mInflater;
    public ReplyAdapter(BaseActivity activity, List<Reply> list){
        mActivity = activity;
        mList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Reply getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReplyHolder reply = null;
        if(convertView == null){
            reply = new ReplyHolder();
            convertView = mInflater.inflate(R.layout.adapter_reply, parent, false);
            reply.mLogoImageView = (ImageView) convertView.findViewById(R.id.adapter_reply_logo);
            reply.mNameTextView = (TextView) convertView.findViewById(R.id.adapter_reply_name);
            reply.mMedalLinearLayout = (LinearLayout) convertView.findViewById(R.id.adapter_reply_medal);
            reply.mFloorTextView = (TextView) convertView.findViewById(R.id.adapter_reply_floor);
            reply.mDateTextView = (TextView) convertView.findViewById(R.id.adapter_reply_date);
            reply.mContentTextView = (TextView) convertView.findViewById(R.id.adapter_reply_content);
            reply.mReplyButton = (Button) convertView.findViewById(R.id.adapter_reply);
            reply.mPicImageView = (ImageView) convertView.findViewById(R.id.adapter_reply_pic);
            convertView.setTag(reply);
        }else{
            reply = (ReplyHolder) convertView.getTag();
        }
        final Reply r = mList.get(position);
        //设置用户信息
        User user = r.getReplyUser();
        mActivity.loadImg(user.getAvatarUrl(), mActivity.dip2px(35), mActivity.dip2px(35), reply.mLogoImageView);
        reply.mNameTextView.setText(user.getName());
        //设置勋章
        mActivity.createMedal(user.getMedals(), reply.mMedalLinearLayout);
        reply.mFloorTextView.setText("# " + (position + 1));
        reply.mDateTextView.setText(Globals.convertDateHHMM(r.getCreateDate()));
        reply.mReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DiscussDetailActivity) mActivity).replyToUser(r);
            }
        });

        List<String> list = r.getPictures();
        if(list != null && !list.isEmpty()){
            reply.mPicImageView.setVisibility(View.VISIBLE);
            if(r.getID() > 0){
                mActivity.loadImg(list.get(0), reply.mPicImageView);
            }else{
                reply.mPicImageView.setImageBitmap(BitmapFactory.decodeFile(list.get(0)));
            }
        }else{
            reply.mPicImageView.setVisibility(View.GONE);
        }
        //设置表情
        reply.mContentTextView.setText(FaceManager.getInstance().getExpressionString(mActivity, r.getContent()));
        return convertView;
    }

    class ReplyHolder {

        ImageView mLogoImageView;
        TextView mNameTextView;
        LinearLayout mMedalLinearLayout;
        TextView mFloorTextView;
        TextView mDateTextView;
        Button mReplyButton;
        TextView mContentTextView;
        ImageView mPicImageView;

    }

}
