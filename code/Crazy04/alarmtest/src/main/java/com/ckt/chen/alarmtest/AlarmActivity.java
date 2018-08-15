package com.ckt.chen.alarmtest;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by D22395 on 2017/12/20.
 */

public class AlarmActivity extends Activity {

    MediaPlayer alarmMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        alarmMusic = MediaPlayer.create(this, );
        alarmMusic.setLooping(true);
        alarmMusic.start();
        new AlertDialog.Builder(AlarmActivity.this).setTitle("闹钟").show();
    }
}
