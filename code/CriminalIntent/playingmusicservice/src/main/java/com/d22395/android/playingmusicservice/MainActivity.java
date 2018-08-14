package com.d22395.android.playingmusicservice;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /**
     * 规定开始音乐、暂停音乐、结束音乐的标志
     */
    public  static final int PLAT_MUSIC=1;
    public  static final int PAUSE_MUSIC=2;
    public  static final int STOP_MUSIC=3;

    private MyBroadCastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver=new MyBroadCastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.complete");
        registerReceiver(receiver,filter);
    }
    public void onClick(View view){
        switch (view.getId()){
            //开始音乐
            case R.id.btn_startmusic:
                playingmusic(PLAT_MUSIC);
                break;
            //暂停
            case R.id.btn_pausemusic:
                playingmusic(PAUSE_MUSIC);
                break;
            //停止
            case R.id.btn_stopmusic:
                playingmusic(STOP_MUSIC);
                break;
        }
    }

    private void playingmusic(int type) {
        //启动服务，播放音乐
        Intent intent=new Intent(this,PlayingMusicServices.class);
        intent.putExtra("type",type);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
