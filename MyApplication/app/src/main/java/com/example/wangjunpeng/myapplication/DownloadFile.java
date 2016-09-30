package com.example.wangjunpeng.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wangjunpeng on 16/9/30.
 */

public class DownloadFile extends Activity {

    public static final String LOG_TAG = "test";

    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;


    File rootDir = Environment.getExternalStorageDirectory();

    //定义要下载的文件名
    public String fileName = "test.jpg";
    public String fileURL = "https://lh4.googleusercontent.com/-HiJOyupc-tQ/TgnDx1_HDzI/AAAAAAAAAWo/DEeOtnRimak/s800/DSC04158.JPG";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        TextView tv = new TextView(this);
        tv.setText("Android Download File With Progress Bar");

        //检查下载目录是否存在
        checkAndCreateDirectory("/mydownloads");

        //执行asynctask
        new DownloadFileAsync().execute(fileURL);
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }


        @Override
        protected String doInBackground(String... aurl) {

            try {
                //连接地址
                URL u = new URL(fileURL);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                //计算文件长度
                int lenghtOfFile = c.getContentLength();


                FileOutputStream f = new FileOutputStream(new File(rootDir + "/my_downloads/", fileName));

                InputStream in = c.getInputStream();

                //下载的代码
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;

                while ((len1 = in.read(buffer)) > 0) {
                    total += len1; //total = total + len1
                    publishProgress("" + (int)((total*100)/lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();

            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d(LOG_TAG,progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            //dismiss the dialog after the file was downloaded
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }


    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS: //we set this to 0
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }
}
