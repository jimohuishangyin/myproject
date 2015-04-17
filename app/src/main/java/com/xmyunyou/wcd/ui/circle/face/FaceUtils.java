package com.xmyunyou.wcd.ui.circle.face;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/***
 * @author huangsm
 * @date 2014-4-11
 * @email huangsanm@gmail.com
 * @desc 读取表情字符
 */
public class FaceUtils {
	
	private final static String FACE_FILE_NAME = "emoji";

	public static List<String> readFaceStr(Context context){
		List<String> list = new ArrayList<String>();
		try {
			InputStream is = context.getResources().getAssets().open(FACE_FILE_NAME);
			BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = "";
			while ((line = bis.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
