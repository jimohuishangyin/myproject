package com.xmyunyou.wcd.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Products;
import com.xmyunyou.wcd.utils.Globals;

/**
 * @author lvzx
 * @date 2013-7-22
 * @email 348883824@qq.com
 * @desc 标签
 */

public class TextLabelView extends ViewGroup {

    private OnItemClickListener mListener;
    private int ITEM_SPACING = 0;
    private int VERTICAL_SPACING = 0;
    private TagInfo downInfo;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int HORIZONTAL = 5;
    private int gravity;
    private Context mContext;
    private int mNum = 0;
    private int mWidth = 0;
    private int mHeight = 0;

    public TextLabelView(Context context) {
        super(context);
        mContext = context;
    }

    public TextLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs,
                R.styleable.TextLabelView);
        ITEM_SPACING += arr.getInt(R.styleable.TextLabelView_itemSpacing, 0)
                + arr.getInt(R.styleable.TextLabelView_margin, 0);
        VERTICAL_SPACING += arr.getInt(R.styleable.TextLabelView_verticalSpacing, 0)
                + arr.getInt(R.styleable.TextLabelView_margin, 0);
        gravity = arr.getInt(R.styleable.TextLabelView_gravity, 1);
        arr.recycle();
        mContext = context;
    }

    public TextLabelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray arr = context.obtainStyledAttributes(attrs,
                R.styleable.TextLabelView);
        ITEM_SPACING += arr.getInt(R.styleable.TextLabelView_itemSpacing, 0)
                + arr.getInt(R.styleable.TextLabelView_margin, 0);
        VERTICAL_SPACING += arr.getInt(R.styleable.TextLabelView_verticalSpacing, 0)
                + arr.getInt(R.styleable.TextLabelView_margin, 0);
        gravity = arr.getInt(R.styleable.TextLabelView_gravity, 1);
        arr.recycle();
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        if (mWidth != 0 && mHeight != 0)
            setMeasuredDimension(mWidth, mHeight);
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        final int count = getChildCount();
        int row = 0;
        // 获取margin
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int marginLeft = mlp.leftMargin;
        int right = arg3 - paddingRight;
        // 获取起始坐标
        int lengthX = arg1;
        int lengthY = arg2;
        // 获取宽高
        int layout_width = 0;
        int layout_height = 0;
        // 子布局坐标大小
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            if (i == 0) {
                lengthX = width + paddingLeft;
                lengthY = height + paddingTop;
            } else {
                lengthX += width + ITEM_SPACING;
                if (lengthX + paddingRight > right - arg1 + marginLeft) {
                    if (mNum != 0)
                        return;
                    lengthX = width + paddingLeft;
                    lengthY += height + VERTICAL_SPACING;
                    row++;
                }
            }
            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }

        // 设置tagview宽高
        View parentView = (View) getParent();
        // int parentWidthPadding = parentView.getPaddingLeft()
        // + parentView.getPaddingRight();
        int parentHightPadding = parentView.getPaddingTop()
                + parentView.getPaddingBottom();
        if (row > 0)
            layout_width = right - arg1 + marginLeft;
        else
            layout_width = lengthX + paddingRight - arg1 + marginLeft
                    + ((View) getParent()).getPaddingLeft();
        layout_height = lengthY + paddingBottom;
        mWidth = layout_width;
        mHeight = layout_height;
        setMinimumWidth(layout_width);
        setMinimumHeight(layout_height);
        if (gravity != LEFT)
            viewGravity(layout_width);
    }

    // 每行居中对齐
    private void viewGravity(int parent_width) {
        List<View> viewlist = new ArrayList<View>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            viewlist.add(child);
            if (i > 0) {
                View lastchild = this.getChildAt(i - 1);
                int margin = 0;
                int nowY = child.getTop();
                int lastY = lastchild.getTop();
                if (nowY != lastY) {
                    if (gravity == HORIZONTAL)
                        margin = (parent_width - lastchild.getRight() - getPaddingLeft()) / 2;
                    else if (gravity == RIGHT)
                        margin = parent_width - lastchild.getRight()
                                - getPaddingLeft();
                    for (int j = 0; j < viewlist.size() - 1; j++) {
                        View view = viewlist.get(j);
                        int X = view.getLeft();
                        view.layout(X + margin, lastY,
                                X + view.getMeasuredWidth() + margin, lastY
                                        + view.getMeasuredHeight());
                    }
                    viewlist.clear();
                    viewlist.add(child);
                }
                if (i == count - 1) {
                    if (gravity == HORIZONTAL)
                        margin = (parent_width - child.getRight() - getPaddingLeft()) / 2;
                    else if (gravity == RIGHT)
                        margin = parent_width - child.getRight()
                                - getPaddingLeft();
                    for (int j = 0; j < viewlist.size(); j++) {
                        View view = viewlist.get(j);
                        int X = view.getLeft();
                        view.layout(X + margin, nowY,
                                X + view.getMeasuredWidth() + margin, nowY
                                        + view.getMeasuredHeight());
                    }
                    viewlist.clear();
                }
            }
        }
    }

    /**
     * 设置item间距
     *
     * @param item_spacing
     */
    public void setItemSpacing(int item_spacing) {
        ITEM_SPACING += item_spacing;
    }

    /**
     * 设置行间距
     *
     * @param vertical_spacing
     */
    public void setVerticalSpacing(int vertical_spacing) {
        VERTICAL_SPACING += vertical_spacing;
    }

    /**
     * 统一设置间距
     *
     * @param margin
     */
    public void setMargin(int margin) {
        ITEM_SPACING += margin;
        VERTICAL_SPACING += margin;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                downInfo = getChildView(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                TagInfo upInfo = getChildView(upX, upY);
                if (upInfo != null && downInfo != null)
                    if (downInfo.getPosition() == upInfo.getPosition())
                        if (mListener != null)
                            mListener.OnItemClick(upInfo.getView(),
                                    upInfo.getPosition());
                break;

            default:
                break;
        }
        return true;
    }

    private TagInfo getChildView(float X, float Y) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (X <= child.getRight() && X >= child.getLeft()
                    && Y >= child.getTop() && Y <= child.getBottom()) {
                TagInfo taginfo = new TagInfo(child, i);
                return taginfo;
            }
        }
        return null;
    }

    /**
     * 标签
     *
     * @param str

    public void addTag(String str) {
        if (TextUtils.isEmpty(str))
            return;
        String[] s = str.split(",");
        int count;
        if (mNum != 0) {
            count = s.length < mNum ? s.length : mNum;
        } else
            count = s.length;
        for (int i = 0; i < count; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                addView(textTag(s[i], i));
            }
        }
    }*/


    /**
     * 标签
     */
    public void addTag(List<Products> plist) {
        if(plist != null && !plist.isEmpty()){
            final int size = plist.size();
            for (int index = 0; index < size; index ++){
                Products p = plist.get(index);
                addView(textTag(p.getName(), p.getID(), index));
            }
        }
        /*if (TextUtils.isEmpty(str))
            return;
        String[] s = str.split(",");
        int count;
        if (mNum != 0) {
            count = s.length < mNum ? s.length : mNum;
        } else
            count = s.length;
        for (int i = 0; i < count; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                addView(textTag(s[i], i));
            }
        }*/
    }



    /**
     * 设置显示数
     */
    public void setNums(int num) {
        mNum = num;
    }

    /**
     * 设置标签
     *
     * @param s
     * @return
     */
    private TextView textTag(String s, int id, int i) {
        TypedArray colors = getResources().obtainTypedArray(R.array.tag_color);
        TextView tv = new TextView(mContext);
        tv.setTag(id);
        int index = i;
        if(index > colors.length()){
            index = (int) (Math.random()*24);
        }
        tv.setBackgroundColor(colors.getColor(index, 0));
        tv.setText(s);
        tv.setTextSize(12);
        tv.setTextColor(mContext.getResources().getColor(R.color.search_bg));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 10, 5);
        return tv;
    }

    /**
     * 标签
     */
    public void addTag(String str) {
        if (TextUtils.isEmpty(str))
            return;
        String[] s = str.split(",");
        int count;
        if (mNum != 0) {
            count = s.length < mNum ? s.length : mNum;
        } else
            count = s.length;
        for (int i = 0; i < count; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                addView(textTag(s[i], i, i));
            }
        }
    }

    /**
     * 移除标签
     */
    public void removeViews() {
        removeAllViews();
        requestLayout();
        invalidate();
    }

    public void setOnItemClick(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        public void OnItemClick(View view, int position);
    }
}


class TagInfo {
    private View view;
    private int position;

    public TagInfo(View view, int position) {
        this.view = view;
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
