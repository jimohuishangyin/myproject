package com.xmyunyou.wcd.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sanmee on 2014/12/10.
 */
public class Globals {

    public static final void log(Object args){
        Log.i("Wanched", args + "");
    }

    public static final String convertDate(String date){
        String s = "-?\\d+";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(date);
        long subString = 0;
        if (ma.find()) {
            subString = Long.parseLong(ma.group());
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(subString));
    }

    public static final String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis()));
    }

    public static final String convertDateHHMM(String date){
        String s = "-?\\d+";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(date);
        long subString = 0;
        if (ma.find()) {
            subString = Long.parseLong(ma.group());
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(subString));
    }

    public static final String encode(String args){
        if(TextUtils.isEmpty(args))
            return "";
        String result = "";
        try {
            result = URLEncoder.encode(args, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  result;
    }

    //获取设备id
    public static String getDeviceID(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 生成图片
     *
     * @param context
     * @param drawableName
     *            名称
     * @return
     */
    public static int createDrawableByIdentifier(Context context,
                                                 String drawableName) {
        String pkgName = context.getPackageName();
        return context.getResources()
                .getIdentifier(drawableName, "drawable", pkgName);
    }

    public static String getDeviceName(){
        String name = android.os.Build.MODEL;
        String manufacturer = android.os.Build.MANUFACTURER;
        if(name.indexOf(manufacturer) >= 0){
            return name;
        }else{
            return manufacturer + " " + name;
        }
    }

    //邮箱验证
    public static boolean isEmail(String textStr){
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(textStr);
        return matcher.matches();
    }

    public static String Md5Encode(String key) {
        byte[] hash = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {

        }
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    public static SpannableString createSpannableText(String text, int start, int end, int color){
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ss;
    }

    /**
     * 是否链接网络
     * @param context
     * @return
     */
    public static boolean isConnectionNetWork(Context context){
        return (isConnectionWifi(context) || isConnectionMobile(context));
    }

    /**
     * 检查是否连接wifi
     *
     * @return
     */
    public static boolean isConnectionWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnected();
    }

    /**
     * 检查是否连接手机网络
     *
     * @return
     */
    public static boolean isConnectionMobile(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnected();
    }

    /**
     * 设置listview高度
     * @param listView
     */
    public static int getTotalHeightofListView(AbsListView listView, boolean isLoadData) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null || mAdapter.isEmpty()) {
            return 350;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight ;
        if (!isLoadData) {
            params.height += 250;
        }
        return params.height;
    }

    /**
     * 相册导入图片
     *
     * @param context
     * @param data
     * @return
     */
    public static String importImage(Context context, Intent data) {
        String elements = null;
        Uri uriData = data.getData();
        String scheme = uriData.getScheme();
        if ("content".equals(scheme)) {
            Cursor rets = context.getContentResolver().query(uriData,
                    new String[] { "_data" }, null, null, null);
            if (rets != null && rets.getCount() > 0) {
                rets.moveToFirst();
                elements = rets.getString(0);
            }
        } else {
            elements = uriData.getPath();
        }
        return elements;
    }

}
