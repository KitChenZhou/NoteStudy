package com.d22395.android.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutDownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        boolean b = intent.getBooleanExtra("shutdown")
        Log.i("TAG", "广播：" + true);
    }
}
