package com.xmyunyou.wcd.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by sanmee on 2014/12/29.
 */
public class UpgradeDialog extends Dialog implements View.OnClickListener {

    private static final long UPDATE_INTERVAL = 2000;
    private static final int BUFFER_SIZE = 1024 * 8;

    private ProgressBar mDownloadProgressBar;
    private TextView mMsgTextView;
    private Button mDownloadButton;
    private Button mCancelButton;

    private String mTitle;
    private String mUrl;

    private BaseActivity mContext;

    //是否下载完成
    private boolean isFinish = false;
    //线程开关
    private boolean isRunning = true;
    private DownloadThread mDownloadThread;

    public UpgradeDialog(BaseActivity context, String msg, String url) {
        super(context, R.style.dialog_style);
        mTitle = msg;
        mUrl = url;
        buildComponent(context);
    }

    protected UpgradeDialog(BaseActivity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        buildComponent(context);
    }


    private void buildComponent(BaseActivity context){
        mContext = context;
        setContentView(R.layout.dialog_upgrade);
        mDownloadProgressBar = (ProgressBar) findViewById(R.id.dialog_download_progress);
        mMsgTextView = (TextView) findViewById(R.id.dialog_title);
        mMsgTextView.setText(mTitle);
        mDownloadButton = (Button) findViewById(R.id.btn_ok);
        mDownloadButton.setOnClickListener(this);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                download();
                break;
            case R.id.btn_cancel:
                if(isFinish){
                    //下载完成，自动安装
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(mDownloadThread.getFile()), "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                }else{
                    isRunning = false;
                }
                dismiss();
                break;
        }
    }


    //开始下载
    private void download(){
        mDownloadButton.setVisibility(View.GONE);
        mDownloadProgressBar.setVisibility(View.VISIBLE);
        mDownloadThread = new DownloadThread(mUrl);
        mDownloadThread.start();
    }

    public class DownloadThread extends Thread {
        //文件大小
        private int mFileSize;
        private String mUrl;
        private String mFileName;
        private File mFile;
        public DownloadThread(String url){
            mUrl = url;
            mFileName = mUrl.substring(mUrl.lastIndexOf("/") + 1);
        }

        public File getFile(){
            return mFile;
        }

        @Override
        public void run() {
            AndroidHttpClient aClient = null;
            RandomAccessFile randomAccessFile = null;
            try {
                aClient = AndroidHttpClient.newInstance("Android");
                HttpGet httpGet = new HttpGet(mUrl);
                HttpResponse response = aClient.execute(httpGet);
                mFileSize = (int) response.getEntity().getContentLength();
                //设置进度条大小
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = mFileSize;
                mHandler.sendMessage(msg);

                InputStream is = response.getEntity().getContent();
                mFile = new File(Environment.getExternalStorageDirectory() + File.separator + mFileName);
                Globals.log(mFile.getAbsolutePath());
                randomAccessFile = new RandomAccessFile(mFile, "rw");
                transferData(is, randomAccessFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void transferData(InputStream in, RandomAccessFile out) {
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = 0;
                long updateDelta = 0;
                int progress = 0;
                long updateStart = System.currentTimeMillis();
                bis = new BufferedInputStream(in, BUFFER_SIZE);
                while (true) {
                    len = bis.read(buffer, 0, BUFFER_SIZE);
                    if (len == -1) {
                        break;
                    }
                    Globals.log("ddddddddddddddd");
                    out.write(buffer, 0, len);
                    progress += len;
                    // Notification状态栏不能更新太过于频繁
                    if (updateDelta > UPDATE_INTERVAL) {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.arg1 = progress;
                        mHandler.sendMessage(msg);
                        // reset data
                        updateStart = System.currentTimeMillis();
                    }
                    updateDelta = System.currentTimeMillis() - updateStart;

                    if(!isRunning){
                        break;
                    }
                }
                if(mFileSize == progress){
                    Message msg = new Message();
                    msg.what = 3;
                    msg.arg1 = progress;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //notify("下载出错", 0, 0, 0);
            } finally {
                try {
                    if (in != null)
                        in.close();
                    if (bis != null)
                        bis.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private Handler mHandler = new Handler (){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mDownloadProgressBar.setMax(msg.arg1);
                    break;
                case 2:
                    mDownloadProgressBar.setProgress(msg.arg1);
                    break;
                case 3:
                    mDownloadProgressBar.setProgress(msg.arg1);
                    final Resources res = mContext.getResources();
                    mCancelButton.setBackgroundColor(res.getColor(R.color.bg_title));
                    mCancelButton.setTextColor(res.getColor(android.R.color.white));
                    mCancelButton.setText("立即安装");
                    isFinish = true;
                    break;
            }
        }
    };

}
