package com.d22395.android.baseactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by D22395 on 2017/9/6.
 */

public abstract class SuperActivity extends AppCompatActivit {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        LogUtil.e("--->onCreate");
    }
    public abstract int getContentViewId();

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("--->onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("--->onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("--->onRestart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        LogUtil.e("--->onpause");
    }
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("--->onResume");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("--->onDestroy");
    }

}
