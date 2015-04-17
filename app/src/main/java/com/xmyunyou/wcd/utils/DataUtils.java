package com.xmyunyou.wcd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.model.MyFavValue;
import com.xmyunyou.wcd.model.User;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sanmee on 2014/12/16.
 */
public class DataUtils {

    private final static String USER_LOGIN_INFO = "user_login_info";
    public static final String PushUserID = "pushuserid";
    public static final String PushChannelID = "PushChannelID";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PWD = "USER_PWD";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_LOGO = "USER_LOGO";
    public static final String COLLECTION = "COLLECTION";
    public static final String ZAN = "zan_key";
    public static final String CACHE_DATA = "CACHE_DATA";
    public static final String RSA_DATA = "RSA_DATA";
    public static final String CATEGORY_DATA = "CATEGORY_DATA";
    public static final String CAR = "CAR";
    public static final String CAR_LINE= "CAR_LINE";
    public static final String CAR_NAME = "CAR_NAME";
    public static final String CAR_PIC = "CAR_PIC";
    public static final String CAR_LINE_NAME="CAR_LINE_NAME";
    public static final String CAR_CATEGORY_LIST = "CATEGORY_LIST";
    public static final String USER_JF = "USER_JF";
    public static final String USER_CAR_ID = "CAR_ID";

    /*public static User getLoginUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        User u = null;
        int uid = sp.getInt(USER_ID, 0);
        if(uid > 0){
            u = new User();
            u.setID(sp.getInt(USER_ID, 0));
            u.setName(sp.getString(USER_NAME, ""));
            u.setPassword(sp.getString(USER_PWD, ""));
            u.setPushUserID(sp.getString(PushUserID, ""));
            u.setPushUserID(sp.getString(PushChannelID, ""));
            u.setEmail(sp.getString(USER_EMAIL, ""));
        }
        return u;
    }

    public static void clearCache(Context context){
        context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static void cacheUser(Context context, User u){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        if(u != null){
            sp.edit().putInt(USER_ID, u.getID()).putString(USER_NAME, u.getName()).putString(USER_PWD, u.getPassword()).putString(USER_EMAIL, u.getEmail()).putString(USER_LOGO, u.getAvatarUrl()).commit();
        }

    }



    //收藏列表
    public static List<MyFavValue> getMyFavList(Context context){
        String favJson = DataUtils.getString(context, DataUtils.COLLECTION);
        Type type = new TypeToken<List<MyFavValue>>(){}.getType();
        List<MyFavValue> values = new Gson().fromJson(favJson, type);
        return values;
    }

    //赞
    public static List<MyFavValue> getMyZanList(Context context){
        String favJson = DataUtils.getString(context, DataUtils.ZAN);
        Type type = new TypeToken<List<MyFavValue>>(){}.getType();
        List<MyFavValue> values = new Gson().fromJson(favJson, type);
        return values;
    }*/

    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void putRsaStr(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO + "RSA", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getRsaStr(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO + "RSA", Context.MODE_PRIVATE);
        return sp.getString(key , "");
    }

    public static User getLoginUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        User u = null;
        int uid = sp.getInt(USER_ID, 0);
        if(uid > 0){
            u = new User();
            u.setID(sp.getInt(USER_ID, 0));
            u.setName(sp.getString(USER_NAME, ""));
            u.setPassword(sp.getString(USER_PWD, ""));
            u.setPushUserID(sp.getString(PushUserID, ""));
            u.setPushUserID(sp.getString(PushChannelID, ""));
            u.setEmail(sp.getString(USER_EMAIL, ""));
            u.setMedals(sp.getString("medals", "1"));
            u.setCheckInNums(sp.getInt("checkinnums", 0));
            u.setAvatarUrl(sp.getString("avatarurl", ""));
            u.setJiFen(sp.getInt(USER_JF,0));
            u.setCarID(sp.getInt(USER_CAR_ID,0));
        }
        return u;
    }

    public static int getInt(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void putInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static String getString(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void cacheUser(Context context, User u){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        if(u != null){
            sp.edit()
                    .putInt(USER_CAR_ID,u.getCarID())
                    .putInt(USER_JF,u.getJiFen())
                    .putInt(USER_ID, u.getID())
                    .putString(USER_NAME, u.getName())
                    .putString(USER_PWD, u.getPassword())
                    .putString(USER_EMAIL, u.getEmail())
                    .putString(USER_LOGO, u.getAvatarUrl())
                    .putString("medals", u.getMedals())
                    .putInt("checkinnums", u.getCheckInNums())
                    .putString("avatarurl", u.getAvatarUrl())
                    .commit();
        }

    }
    public static void clearCache(Context context){
        context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE).edit().clear().commit();
    }

    //收藏列表
    public static List<MyFavValue> getMyFavList(Context context){
        String favJson = DataUtils.getString(context, DataUtils.COLLECTION);
        Type type = new TypeToken<List<MyFavValue>>(){}.getType();
        List<MyFavValue> values = new Gson().fromJson(favJson, type);
        return values;
    }

    /**
     * 保存收藏
     * @param context
     * @return

    public static Set<String> getFav(Context context){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        return sp.getStringSet(COLLECTION, null);
    }

    public static void putSet(Context context, String key, Set<String> params){
        SharedPreferences sp = context.getSharedPreferences(USER_LOGIN_INFO, Context.MODE_PRIVATE);
        sp.edit().putStringSet(key, params).commit();
    }*/

}
