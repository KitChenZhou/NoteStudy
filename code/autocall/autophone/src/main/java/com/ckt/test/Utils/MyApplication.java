package com.ckt.test.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by D22395 on 2017/9/18.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
