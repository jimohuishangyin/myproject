package com.xmyunyou.wcd.utils.net;

import com.xmyunyou.wcd.model.FileInfo;
import com.xmyunyou.wcd.utils.Globals;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/***
 * @author huangsm
 * @date 2013-12-24
 * @email huangsanm@gmail.com
 * @desc http请求封装
 */
public class UploadImageManager {
	private final static int CONNECTION_TIMEOUT = 30000;

	private final static String CHARSET = "UTF-8";

	public static String httpPost(String httpUrl, Map<String, String> params,
			FileInfo info) {
		String result = "";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(CONNECTION_TIMEOUT);
			con.setConnectTimeout(CONNECTION_TIMEOUT);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("POST");

			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// 開始寫資料
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			for (String key : params.keySet()) {
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes(("Content-Disposition: form-data; name=\"" + key
						+ "\"" + end));
				ds.writeBytes(end);
				ds.writeBytes(params.get(key));
				ds.writeBytes(end);
			}

			FileInputStream fStream;
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			
			// 圖片
			ds.writeBytes((twoHyphens + boundary + end));
			ds.writeBytes(("Content-Disposition: form-data; name=\""
					+ info.getName() + "\";filename=\"" + info.getName()
					+ "" + info.getEndWith() + "\"" + end));
			ds.writeBytes(("Content-Type: application/octet-stream; charset="
					+ CHARSET + end));
			ds.writeBytes(end);
			// 取得檔案
			fStream = new FileInputStream(info.getFile());
			length = -1;
			// 讀取至緩衝區
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			fStream.close();
			ds.flush();

			// 結束
			ds.writeBytes((twoHyphens + boundary + twoHyphens + end));

			// 取得網頁回應內容
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			result = b.toString();
			Globals.log("result:" + result);
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
