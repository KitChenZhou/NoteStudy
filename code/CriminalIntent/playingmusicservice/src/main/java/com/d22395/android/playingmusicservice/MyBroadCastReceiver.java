package com.d22395.android.playingmusicservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by D22395 on 2017/8/10.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"音乐播放结束",Toast.LENGTH_SHORT).show();
    }
}
