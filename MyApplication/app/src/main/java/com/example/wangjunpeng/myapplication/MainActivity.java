package com.example.wangjunpeng.myapplication;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lab = (TextView) findViewById(R.id.textView);
        lab.setText("aaaaa");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lab.setText("bbbb");

                Bundle paramBundle = new Bundle();
                paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);
                String dexoutputpath =  "/data/data/com.example.wangjunpeng.myapplication/files";
                String dexpath = "/data/data/com.example.wangjunpeng.myapplication/files/TestB.apk";


                Boolean exist = new File(dexpath).exists();
                File f = new File(dexpath);

                System.out.println("------------------" + exist + " ,,,,,,," + f.canRead() + "ssss " + f.canWrite());

//                LoadAPK(paramBundle, dexpath, dexoutputpath);

            }
        });
    }

    public void LoadAPK(Bundle paramBundle, String dexpath, String dexoutputpath) {
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
                dexoutputpath, null, localClassLoader);
        try {
            PackageInfo plocalObject = getPackageManager().getPackageArchiveInfo(dexpath, 1);

            if ((plocalObject.activities != null)
                    && (plocalObject.activities.length > 0)) {
                String activityname = plocalObject.activities[0].name;
                Log.d(TAG, "activityname = " + activityname);

                Class localClass = localDexClassLoader.loadClass(activityname);
                Constructor localConstructor = localClass.getConstructor(new Class[]{});
                Object instance = localConstructor.newInstance(new Object[]{});
                Log.d(TAG, "instance = " + instance);

                Method localMethodSetActivity = localClass.getDeclaredMethod(
                        "setActivity", new Class[]{Activity.class});
                localMethodSetActivity.setAccessible(true);
                localMethodSetActivity.invoke(instance, new Object[]{this});

                Method methodonCreate = localClass.getDeclaredMethod(
                        "onCreate", new Class[]{Bundle.class});
                methodonCreate.setAccessible(true);
                methodonCreate.invoke(instance, new Object[]{paramBundle});
            }
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
