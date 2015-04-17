package com.xmyunyou.wcd.baidu;

import android.content.Context;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.xmyunyou.wcd.utils.DataUtils;

import java.util.List;

/**
 * Created by sanmee on 2014/12/18.
 */
public class BaiduFrontiaPushMessageReceiver extends FrontiaPushMessageReceiver {

    @Override
    public void onBind(Context context, int i, String userID, String channelID, String s3, String s4) {
        //Globals.log("ddddddddddddddd:" + userID + "channelID:" + channelID);
        DataUtils.putString(context, DataUtils.PushUserID, userID);
        DataUtils.putString(context, DataUtils.PushChannelID, channelID);
    }



    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> strings, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s2) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s2, String s3) {

    }
}
