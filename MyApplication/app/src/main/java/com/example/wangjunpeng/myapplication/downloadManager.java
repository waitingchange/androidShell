package com.example.wangjunpeng.myapplication;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by wangjunpeng on 16/9/30.
 */

public class downloadManager {
    public static final String APKNAME = "texas.apk";
    private static final String TAG = "OGDownloadAPK";
    public static URL mDownUrl;
    private static boolean mbShowProgress;
    private int mErrCode;
    private File mSaveFile;
    private int miDownLength = 0;
    private int miFileLength;
    private String msUrl;



    public downloadManager(String paramString)
    {
        this.msUrl = paramString;
    }

    public static File create(String folder, String fileName){
        File file = new File(
                address(folder, fileName));
        Log.d("---------------------", "Create file address: " + address(folder, fileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static FileOutputStream getOutputStream(String folder, String fileName){
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOut;
    }

    public static FileInputStream getInputStream(String folder, String fileName){
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileIn;
    }

    public static String address(String folder, String file){
        return folder+"/"+file;
    }

    public static void forceCreate(String folder, String fileName){
        File dirs = new File(folder);
        dirs.mkdirs();

        File file = new File(
                address(folder, fileName));

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String folder, String fileName){
        File file = new File(
                address(folder, fileName));
        file.delete();
    }


    private void createFile()
    {
        String str = Environment.getExternalStorageDirectory().toString();
        File localFile = new File(str + "/download/");
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        localFile = new File(str + "/download/ourgame/");
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        this.mSaveFile = new File(str + "/download/ourgame/", "landlord-android.apk");
        if (!this.mSaveFile.exists())
        {
            localFile.mkdir();
            return;
        }
        this.mSaveFile.delete();
    }


    public void downLoadFile( String downloadurl, String save_address, String fileName) {
        try {

            URL url = new URL(downloadurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Avoid timeout exception which usually occurs in low network
            connection.setConnectTimeout(0);
            connection.setReadTimeout(0);
//            if (chunk.end != 0) // support unresumable links
//                connection.setRequestProperty("Range", "bytes=" + chunk.begin + "-" + chunk.end);

            connection.connect();


            File cf = new File(address(save_address, fileName));
            // Check response code first to avoid error stream
            int status = connection.getResponseCode();
            InputStream remoteFileIn;
            if(status == 416)
                remoteFileIn = connection.getErrorStream();
            else
                remoteFileIn = connection.getInputStream();

            FileOutputStream chunkFile = new FileOutputStream(cf, true);

            int len = 0;
            // set watchDoger to stop thread after 1sec if no connection lost

            chunkFile.flush();
            chunkFile.close();
            connection.disconnect();



        }catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }




}
