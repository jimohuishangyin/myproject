package com.xmyunyou.wcd.ui.circle.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author huangsm
 * @date 2014-4-11
 * @email huangsanm@gmail.com
 * @desc 表情工具类
 */
public class FaceManager {
	
	private final static int PAGE_SIZE = 20;

	private static FaceManager mFaceManager;
	private FaceManager (){}
	
	private List<List<FaceInfo>> mPageFaceList = new ArrayList<List<FaceInfo>>();
	private List<FaceInfo> mFaceList = new ArrayList<FaceInfo>();
	private HashMap<String, String> mFaceMap = new HashMap<String, String>();
	
	public static FaceManager getInstance(){
		if(mFaceManager == null){
			mFaceManager = new FaceManager();
		}
		return mFaceManager;
	}
	
	public List<List<FaceInfo>> getFaceList(){
		return mPageFaceList;
	}
	
	//解析数据
	public void paseFaceData(Context context){
		if(mFaceList.isEmpty()){
			List<String> list = FaceUtils.readFaceStr(context);
			for (String value : list) {
				String[] strText = value.split(",");
				String fname = strText[0];
				String charText = strText[1];
				
				mFaceMap.put(charText, fname);
				//resid
				int resID = context.getResources().getIdentifier(fname, "drawable", context.getPackageName());
				if(resID != 0){
					FaceInfo info = new FaceInfo();
					info.setName(fname);
					info.setCharacter(charText);
					info.setId(resID);
					mFaceList.add(info);
				}
			}
			
			final int size = list.size();
			int pageCount = (int) Math.ceil(size / 20 + 0.1);
			for (int i = 0; i < pageCount; i++) {
				mPageFaceList.add(getData(i));
			}
		}
	}
	
	private List<FaceInfo> getData(int page) {
		int startIndex = page * PAGE_SIZE;
		int endIndex = startIndex + PAGE_SIZE;

		if (endIndex > mFaceList.size()) {
			endIndex = mFaceList.size();
		}
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		List<FaceInfo> list = new ArrayList<FaceInfo>();
		list.addAll(mFaceList.subList(startIndex, endIndex));
		if (list.size() < PAGE_SIZE) {
			for (int i = list.size(); i < PAGE_SIZE; i++) {
				FaceInfo object = new FaceInfo();
				list.add(object);
			}
		}
		if (list.size() == PAGE_SIZE) {
			FaceInfo object = new FaceInfo();
			object.setId(R.drawable.del_ico_dafeult);
			list.add(object);
		}
		return list;
	}
	
	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(BaseActivity context, int imgId,
			String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imgId);
		
		int width = context.dip2px(28);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imgId);

        int width = 40;
        bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
	
	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(BaseActivity context, String str) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}
	
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(BaseActivity context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = mFaceMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable",
					context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
				int px48 = context.dip2px(32);
				bitmap = Bitmap.createScaledBitmap(bitmap, px48, px48, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(bitmap);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				/*if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end);
				}*/
				//break;
			}
		}
	}
	
}
