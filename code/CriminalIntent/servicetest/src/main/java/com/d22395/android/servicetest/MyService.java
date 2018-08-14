package com.d22395.android.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    /** 标志位 */
    private static String TAG = "MyService";
    /** 行为 */
    public static final String ACTION = "com.d22395.android.servicetest.MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "----- onCreate() ---");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "----- onStart() ---");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "----- onStartCommand() ---");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "----- onBind() ---");
        return null;
    }


    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "----- onRebind() ---");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "----- onUnbind() ---");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "----- onDestroy() ---");
        super.onDestroy();
    }

}
