package com.d22395.android.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
    /** 标志位 */
    private static final String TAG = "com.d22395.android.servicetest.MainActivity";
    /** 启动服务 */
    private Button mBtnStart;
    /** 绑定服务 */
    private Button mBtnBind;

    private Button mBtnStop;

    private Button mBtnUnbind;

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        intent.setAction("com.d22395.android.servicetest.MyService");
        intent.setPackage(this.getPackageName());
    }

    /**
     * init the View
     */
    private void initView() {
        mBtnStart = (Button) findViewById(R.id.startservice);
        mBtnBind = (Button) findViewById(R.id.bindservice);
        mBtnStop = findViewById(R.id.stopservice);
        mBtnUnbind = findViewById(R.id.unbindservice);
        mBtnStart.setOnClickListener(this);
        mBtnBind.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnUnbind.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 启动服务的方式
            case R.id.startservice:
                startService(intent);
                break;
            // 绑定服务的方式
            case R.id.bindservice:
                bindService(intent, conn, BIND_AUTO_CREATE);
                break;
            case R.id.stopservice:
                stopService(intent);
                break;
            case R.id.unbindservice:
                unbindService(conn);
                break;
            default:
                break;
        }

    }

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    };
}
