package com.xmyunyou.wcd.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/***
 * @author huangsm
 * @date 2014-1-6
 * @email huangsanm@gmail.com
 * @desc 返回按钮
 */
public class BackButton extends ImageView {

private OnBackListener mBackListener;
	
	public BackButton(Context context) {
		super(context);
		init(context);
	}
	
	public BackButton(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}
	
	public BackButton(Context context , AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init(context);
	}

	private void init(final Context context){
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mBackListener != null){
					mBackListener.onBack(v);
				}
				
				if(v.getTag() == null){
					((Activity) context).finish();
				}
			}
		});
	}
	
	public void setOnBackListener(OnBackListener listener){
		mBackListener = listener;
	}
	
	public interface OnBackListener {
		void onBack(View v);
	}
}
