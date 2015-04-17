package com.xmyunyou.wcd.ui.circle.face;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xmyunyou.wcd.R;

import java.util.List;

/***
 * @author huangsm
 * @date 2014-4-11
 * @email huangsanm@gmail.com
 * @desc face适配器
 */
public class FaceAdapter extends BaseAdapter {

	private List<FaceInfo> mList;
	private Context mContext;

	public FaceAdapter(Context context, List<FaceInfo> list) {
		mContext = context;
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
		FaceInfo emoji = mList.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_face, null);
			viewHolder.iv_face = (ImageView) convertView
					.findViewById(R.id.item_iv_face);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (emoji.getId() == R.drawable.del_ico_dafeult) {
			//convertView.setBackgroundDrawable(null);
			viewHolder.iv_face.setImageResource(emoji.getId());
		} else if (TextUtils.isEmpty(emoji.getCharacter())) {
			//convertView.setBackgroundDrawable(null);
			viewHolder.iv_face.setImageDrawable(null);
		} else {
			viewHolder.iv_face.setTag(emoji);
			viewHolder.iv_face.setImageResource(emoji.getId());
		}
		return convertView;
	}

	class ViewHolder {

		public ImageView iv_face;
	}

}
