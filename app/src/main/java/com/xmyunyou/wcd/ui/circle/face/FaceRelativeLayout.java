package com.xmyunyou.wcd.ui.circle.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.xmyunyou.wcd.R;

import java.util.ArrayList;
import java.util.List;

/***
 * @author huangsm
 * @date 2014-4-11
 * @email huangsanm@gmail.com
 * @desc 表情view
 */
@SuppressLint("NewApi")
public class FaceRelativeLayout extends RelativeLayout {

	private Context mContext;
	private ViewPager mViewPager;
	private LinearLayout mPageLinearLayout;
	private List<View> mViews;
	private List<FaceAdapter> mFaceAdapters;
	private List<ImageView> mImageViewList;
	private List<List<FaceInfo>> mFaceList;
	private int mCurrent;
	private EditText mEditText;
	
	public FaceRelativeLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	private void init(){
		FaceManager.getInstance().paseFaceData(mContext);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setupView();
		initData();
		initPageIndex();
		initPageViewData();
	}
	
	private void setupView(){
		mViewPager = (ViewPager) findViewById(R.id.face_contain);
		mPageLinearLayout = (LinearLayout) findViewById(R.id.face_index);
		mViews = new ArrayList<View>();
		mFaceAdapters = new ArrayList<FaceAdapter>();
		mFaceList = FaceManager.getInstance().getFaceList();
		mImageViewList = new ArrayList<ImageView>();
	}
	
	private void initData(){
		// 左侧添加空页
		View nullView1 = new View(mContext);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		mViews.add(nullView1);
		
		final int size = mFaceList.size();
		for (int i = 0; i < size; i++) {
			GridView view = new GridView(mContext);
			FaceAdapter adapter = new FaceAdapter(mContext, mFaceList.get(i));
			view.setAdapter(adapter);
			mFaceAdapters.add(adapter);
			view.setOnItemClickListener(mOnItemClickListener);
			view.setNumColumns(7);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			mViews.add(view);
		}
		
		// 右侧添加空页面
		View nullView2 = new View(mContext);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		mViews.add(nullView2);
	}
	
	/**
	 * 初始化游标
	 */
	private void initPageIndex() {
		ImageView imageView;
		for (int i = 0; i < mViews.size(); i++) {
			imageView = new ImageView(mContext);
			imageView.setBackgroundResource(R.drawable.d1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			mPageLinearLayout.addView(imageView, layoutParams);
			if (i == 0 || i == mViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.d2);
			}
			mImageViewList.add(imageView);
		}
	}

	/**
	 * 填充数据
	 */
	private void initPageViewData() {
		mViewPager.setAdapter(new ViewPagerAdapter(mViews));
		mViewPager.setCurrentItem(1);
		mCurrent = 0;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrent = arg0 - 1;
				// 描绘分页点
				draw_Point(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == mImageViewList.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						mViewPager.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
						mImageViewList.get(1).setBackgroundResource(R.drawable.d2);
					} else {
						mViewPager.setCurrentItem(arg0 - 1);// 倒数第二屏
						mImageViewList.get(arg0 - 1).setBackgroundResource(
								R.drawable.d2);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 1; i < mImageViewList.size(); i++) {
			if (index == i) {
				mImageViewList.get(i).setBackgroundResource(R.drawable.d2);
			} else {
				mImageViewList.get(i).setBackgroundResource(R.drawable.d1);
			}
		}
	}
	
	public void setEditTextComponent(EditText editText){
		mEditText = editText;
	}
	
	public void toggleFaceView(){
		if(getVisibility() == View.VISIBLE){
			setVisibility(View.GONE);
		}else{
			setVisibility(View.VISIBLE);
		}
	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FaceInfo face = (FaceInfo) mFaceAdapters.get(mCurrent).getItem(position);
			if (face.getId() == R.drawable.del_ico_dafeult) {
				int selection = mEditText.getSelectionStart();
				String text = mEditText.getText().toString();
				if (selection > 0) {
					String text2 = text.substring(selection - 1);
					if ("]".equals(text2)) {
						int start = text.lastIndexOf("[");
						int end = selection;
						mEditText.getText().delete(start, end);
						return;
					}
					mEditText.getText().delete(selection - 1, selection);
				}
			}
			if (!TextUtils.isEmpty(face.getCharacter())) {
				/*if (mListener != null)
					mListener.onCorpusSelected(emoji);*/
				SpannableString spannableString = FaceManager.getInstance().addFace(getContext(), face.getId(), face.getCharacter());
				mEditText.append(spannableString);
			}
		}
	};
}
