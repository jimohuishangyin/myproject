package com.xmyunyou.wcd.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.utils.DataUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/1/9.
 */
public class WanApp extends FrontiaApplication {

    public static List<Category> mCategoryList;
//    public MyLocationListener mMyLocationListener;
    @Override
    public void onCreate() {
        initImageLoader(this);

        mCategoryList = new ArrayList<Category>();
        String clist = DataUtils.getString(this, DataUtils.CAR_CATEGORY_LIST);
        if (!TextUtils.isEmpty(clist)) {
            List<Category> list = new Gson().fromJson(clist, new TypeToken<List<Category>>() {
            }.getType());
            mCategoryList.addAll(list);
        }
        super.onCreate();

        SDKInitializer.initialize(this);

    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.MAX_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(100 * 1024 * 1024) // 50 Mb
                .memoryCacheSize(100 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }




}