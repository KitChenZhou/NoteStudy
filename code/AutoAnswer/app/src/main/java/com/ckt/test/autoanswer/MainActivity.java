package com.ckt.test.autoanswer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivityTAG";
    private static final int REQUEST_CODE_ASK_CALL_SETTINGS = 2;

    private Button startBtn;
    private ITelephony iPhoney = null;
    private TelephonyManager tm;
    private boolean startState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_SETTINGS);
        }

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        iPhoney = getITelephony(this);

        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startBtn.getText().equals("点击开始自动接电话")) {
                    startBtn.setText("正在自动接电话");
                    startState = true;
                } else {
                    startBtn.setText("点击开始自动接电话");
                    startState = false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

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
                        Log.i(LOG_TAG, "[Listener] Ringing State:" + incomingNumber);
                        if (iPhoney != null) {
                            try {
                                iPhoney.answerRingingCall();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public ITelephony getITelephony(Context context) {
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
