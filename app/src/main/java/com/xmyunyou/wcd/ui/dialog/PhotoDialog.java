package com.xmyunyou.wcd.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;

/***
 * @author huangsm
 * @date 2014-1-8
 * @email huangsanm@gmail.com
 * @desc 导入图片对话框
 */
public class PhotoDialog extends Dialog implements OnClickListener {

	// 拍照
	public final static int REQUEST_TAKE = 1111;
	// 导入
	public final static int REQUEST_IMPORT = REQUEST_TAKE + 1;
	public final static int REQUEST_CAMERA = REQUEST_IMPORT + 1;
	//crop
	public final static int REQUEST_CROP_PIC = REQUEST_CAMERA + 1;
	
	private Uri mPicPath;

	private Context mContext;
	private String mPath;

	public PhotoDialog(Context context) {
		super(context, R.style.dialog_style);
		mContext = context;
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.take_photo);

		findViewById(R.id.dialog_take).setOnClickListener(this);
		findViewById(R.id.dialog_import).setOnClickListener(this);
		mPath = FileUtil.getDefaultPath() + File.separator + System.currentTimeMillis() + ".jpg";
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.dialog_take:
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				try {
					intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(mPath);
					mPicPath = Uri.fromFile(f);
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, mPicPath);
					((Activity) mContext).startActivityForResult(intent,
							REQUEST_TAKE);
				} catch (ActivityNotFoundException e) {
					//ToastUtils.show(mContext, "抱歉，没有找到设备上的相机程序");
				}
			} else {
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				((Activity) mContext).startActivityForResult(intent,
						REQUEST_CAMERA);
			}
			break;
		case R.id.dialog_import:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			((Activity) mContext)
					.startActivityForResult(intent, REQUEST_IMPORT);
			break;
		}
		dismiss();
	}
	
	public String getPicPath(){
		return mPath;
	}

	public Uri getPicUri() {
		return mPicPath;
	}
	
	public String getSaveBitmapPath(Bitmap bitmap){
		if(bitmap == null)
			return "";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(mPath));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mPath;
	}
	
	// 截取图片
	public void cropImage(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		((Activity) mContext).startActivityForResult(intent, REQUEST_CROP_PIC);
	}
}
