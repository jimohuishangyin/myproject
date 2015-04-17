package com.xmyunyou.wcd.ui.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xmyunyou.wcd.R;



/***
 * @author huangsm
 * @date 2014-1-7
 * @email huangsanm@gmail.com
 * @desc 重写linearlayout实现仿listview加载下一页效果
 * 主要用于listview外层由scrollview
 */
public class LoadMoreView extends LinearLayout {

	private Context mContext;
    private LinearLayout mFooterLinearLayout;

    //动画
    private ImageView mImageView;
    private Animation mAnimation;

    private ShowListView mListView;
//    private GifView mGifView;

    private View mEmptyView;

	public LoadMoreView(Context context) {
		super(context);
		init(context);
	}
	
	public LoadMoreView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	private void init(Context context){
        mContext = context;
        setOrientation(VERTICAL);

        addContentView();

        addFooterView();

        mEmptyView = createEmptyView();

	}

    private void addContentView(){
        mListView = new ShowListView(mContext);
        //设置自动滑倒顶部
        mListView.setFocusable(false);
        mListView.setFocusableInTouchMode(false);
        mListView.setDivider(mContext.getResources().getDrawable(android.R.color.transparent));
        mListView.setSelection(-1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mListener != null){
                    mListener.setOnItemLoadClick(i, mListView.getAdapter().getItem(i), view);
                }
            }
        });
        addView(mListView, 0);
    }

    private void addFooterView(){
        mFooterLinearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.load_more_layout, null);
        //设置加载动画
        /*mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        mImageView = (ImageView) mFooterLinearLayout.findViewById(R.id.dialog_loading);
        mImageView.startAnimation(mAnimation);*/

        ImageView mImageView = (ImageView) mFooterLinearLayout.findViewById(R.id.frame_image);
        AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(R.drawable.progress_large_loading);
        mImageView.setBackgroundDrawable(anim);
        anim.start();
        addView(mFooterLinearLayout, 1);
    }

    public void setAdapter(BaseAdapter adapter){
        mListView.setAdapter(adapter);
    }

    public ListView getListView(){
        return mListView;
    }

	
	/**
	 * 数据加载完成时调用
	 * @param hasNext true还有下一页，false没有
	 */
	public void onLoadComplete(boolean hasNext){
		if(hasNext){
            removeViewAt(1);
		} else {
		    final int childCount = getChildCount();
            if(childCount < 2) {
                addView(mFooterLinearLayout, 1);
            }
            //mImageView.startAnimation(mAnimation);
		}

        if(mListView.getAdapter() == null || mListView.getAdapter().isEmpty()){
            if(getChildAt(1) == null){
                addView(mEmptyView, 1);
            }
        }
	}

    /**
     * //检查是否为空，如果为空添加空显示
     * @return
     */
    private View createEmptyView(){
        TextView view = new TextView(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 0);
        view.setLayoutParams(lp);
        view.setText("暂时没有数据");
        view.setGravity(Gravity.CENTER);
        view.setTextSize(16);
        view.setTextColor(mContext.getResources().getColor(R.color.bg_title));
        return view;
    }
	
	/**
	 * item点击事件
	 * @author huangsm
	 *
	 */
	public interface OnItemLoadClickListener {
		public void setOnItemLoadClick(int position, Object obj, View item);
	}
	
	private OnItemLoadClickListener mListener;
	public void setOnItemLoadClickListener(OnItemLoadClickListener listener){
		mListener = listener;
	}
}
