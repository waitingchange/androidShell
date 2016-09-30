package com.example.wangjunpeng.applicationb;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TestBActivity";
    private Activity otherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean b = false;
        if (savedInstanceState != null) {
            b = savedInstanceState.getBoolean("KEY_START_FROM_OTHER_ACTIVITY", false);
            if (b) {
                this.otherActivity.setContentView(new TBSurfaceView(
                        this.otherActivity));
            }
        }
        if (!b) {
            super.onCreate(savedInstanceState);
            // setContentView(R.layout.main);
            setContentView(new TBSurfaceView(this));
        }
    }

    public void setActivity(Activity paramActivity) {
        Log.d(TAG, "setActivity..." + paramActivity);
        this.otherActivity = paramActivity;
    }
}
