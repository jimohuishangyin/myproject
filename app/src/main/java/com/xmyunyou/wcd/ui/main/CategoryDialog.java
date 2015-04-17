package com.xmyunyou.wcd.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.app.WanApp;
import com.xmyunyou.wcd.model.Category;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sanmee on 2015/2/11.
 */
public class CategoryDialog extends Dialog {


    private BaseActivity mContext;

    public CategoryDialog(BaseActivity context) {
        super(context, R.style.dialog_style);
        mContext = context;

        setupView();

    }

    private void setupView(){
        setContentView(R.layout.dialog_category);

        ImageView imageView = (ImageView) findViewById(R.id.dialog_loading);
        AnimationDrawable anim = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.progress_large_loading);
        imageView.setBackgroundDrawable(anim);
        anim.start();

        requestCategory();
    }

    private void requestCategory(){
        Type type = new TypeToken<List<Category>>(){}.getType();
        mContext.requestGet(Constants.CATEGORY_LIST, null, type, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<Category> list = (List<Category>) result;
                WanApp.mCategoryList.addAll(list);
                DataUtils.putString(mContext, DataUtils.CAR_CATEGORY_LIST, new Gson().toJson(list));
                mContext.log("category data cached");
                dismiss();
            }

            @Override
            public void onFailure(String errorMsg) {
            }
        });
    }
}