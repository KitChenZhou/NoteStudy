package com.ckt.test.autoendphone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private ITelephony iPhony = null;
    private EditText callDisplayTimeEdit;
    private Thread mEndThread;

    private int callDisplayTime;
    private boolean endCall;
    private Handler handler = new Handler();
    private TelephonyManager tm;
    private boolean startState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        iPhony = getITelephony(this);//获取电话实例
        callDisplayTimeEdit = (EditText) findViewById(R.id.wait_time);
        final Button startListen = (Button) findViewById(R.id.startListen);
        Button endListen = (Button) findViewById(R.id.endListen);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        startListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!callDisplayTimeEdit.getText().toString().isEmpty()) {
                    if (!startState) {
                        startState = true;
                        callDisplayTime = Integer.parseInt(callDisplayTimeEdit.getText().toString());

                    } else {
                        Toast.makeText(MainActivity.this, "已开始监听来电", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "请输入来电显示时间", Toast.LENGTH_SHORT).show();
                }
            }
            });

        endListen.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                startState = false;
            }
            });


        /*
        挂断电话线程
         */
            mEndThread =new

            Thread(new Runnable() {
                @Override
                public void run () {
                    try {
                        endCall = iPhony.endCall();// 挂断电话

                        Log.i("MainActivity", "是否成功挂断：" + endCall);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        }

    /*
       电话监听器
        */
        PhoneStateListener listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
                super.onCallStateChanged(state, incomingNumber);
                if (startState) {
                    switch (state) {
                        // 挂断状态
                        case TelephonyManager.CALL_STATE_IDLE:

                            break;
                        // 接听状态
                        case TelephonyManager.CALL_STATE_OFFHOOK:

                            break;
                        // 响铃状态
                        case TelephonyManager.CALL_STATE_RINGING:
                            // 输出来电号码
                            handler.postDelayed(mEndThread, callDisplayTime * 1000);
                            break;
                    }
                }
            }

        };


    public static ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null); // 获取声明的方法
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        ITelephony iTelephony;
        try {
            assert getITelephonyMethod != null;
            iTelephony = (ITelephony) getITelephonyMethod.invoke(
                    mTelephonyManager, (Object[]) null); // 获取实例
            return iTelephony;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
