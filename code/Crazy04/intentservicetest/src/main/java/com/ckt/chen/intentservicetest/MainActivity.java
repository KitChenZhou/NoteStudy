package com.ckt.chen.intentservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 普通Service的执行会阻塞主线程最终会导致ＡＮＲ异常
    public void startService(View source) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    // 不会阻塞 IntentService会使用单独线程来完成耗时任务
    public void startIntentService(View source) {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

}
