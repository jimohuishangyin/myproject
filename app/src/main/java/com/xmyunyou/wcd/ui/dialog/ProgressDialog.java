package com.xmyunyou.wcd.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xmyunyou.wcd.R;

/**
 * Created by huangsm on 2014/8/25 0025.
 * Email:huangsanm@foxmail.com
 */
public class ProgressDialog extends Dialog  {

    //private Animation mAnimation;
    private ImageView mImageView;
    public ProgressDialog(Context context){
        super(context, R.style.dialog_style);

        setContentView(R.layout.dialog_progress_layout);
        mImageView = (ImageView)findViewById(R.id.frame_image);
        AnimationDrawable anim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.progress_large_loading);
        mImageView.setBackgroundDrawable(anim);
        anim.start();
        //设置动画效果
        //mAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        //mImageView = (ImageView) findViewById(R.id.dialog_tip);
    }



    public ImageView getImageView(){
        return mImageView;
    }

}
