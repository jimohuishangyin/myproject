package com.xmyunyou.wcd.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by 95 on 2015/3/9.
 */
public class FileUtil {

    public static String getDefaultPath() {
        File file = null;
        String path = "";
        try{
            path = "/storage/sdcard1";
            file = new File(path);
            if (file.exists() && file.isDirectory()) {
                path = "/storage/sdcard1/玩车之家/";
                file = new File(path);
                if(!file.exists()){
                    file.mkdir();
                }
            }else{
                path = Environment.getExternalStorageDirectory() + "/玩车之家/";
                file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }catch (Exception ex){
            path = Environment.getExternalStorageDirectory() + "/玩车之家/";
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        return file.getAbsolutePath();
    }
}
