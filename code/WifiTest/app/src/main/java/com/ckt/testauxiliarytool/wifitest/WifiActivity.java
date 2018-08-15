package com.ckt.testauxiliarytool.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.testauxiliarytool.wifitest.utils.NumberProgressBar;

public class WifiActivity extends AppCompatActivity {

    private static final String TAG = "WifiActivity";

    private static Toast toast = null;

    //退出时的时间
    private long mExitTime;

    private EditText mWifiVancantTimeEdit; // WiFi空闲时间编辑
    private EditText mWifiTestTimesEdit; // WiFi测试次数编辑
    private TextView mShowWifiCount; // 列出WiFi测试次数
    private TextView mReportMessage;
    private TextView mReportMessageTitle;
    Button wifiStop;
    Button wifiStart;
    private com.suke.widget.SwitchButton mWifiSwitchButton; // 模拟WiFi开关
    private NumberProgressBar mWifiProgressBar;// WiFi百分比进度条

    private int mWifiVancantTime; // WiFi空闲时间
    private int mWifiTestTimes; // WiFi 测试次数

    private int mWifiSucceedCount; // WiFi成功次数
    private int mWifiStartCount; // WiFi第几次测试
    private int mWifiNotes = 0;
    private boolean mWifiState = false; // WiFi状态
    private int mWifiCloseTemp = 0;
    private boolean isClosing = false;

    private boolean wifiClose = true;
    private boolean wifiOpenTemp = true;

    private Handler mHandler = new Handler();

    private Thread mWifiOpenThread = null; // WiFi开启线程
    private Thread mWifiCloseThread = null; // WiFi关闭线程

    private WifiManager mWifiManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initView();

        mWifiManager = (WifiManager) super.getSystemService(Context.WIFI_SERVICE);
        /* 初始WiFi状态 */
        mWifiManager.setWifiEnabled(false);
        mWifiSwitchButton.setChecked(false);

        mWifiSwitchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        wifiStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mWifiState) {
                    if (mWifiVancantTimeEdit.getText().toString().length() != 0
                            && mWifiTestTimesEdit.getText().toString().length() != 0) {
                        initWifi();
                    } else {
                        showToast(getString(R.string.wt_enter_correct_parameters));
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(WifiActivity.this);
                    dialog.setIcon(android.R.drawable.ic_dialog_info);
                    dialog.setTitle(getString(R.string.wt_notice));
                    dialog.setMessage(getString(R.string.wt_start_again));
                    dialog.setCancelable(true);
                    dialog.setPositiveButton(getString(R.string.wt_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showToast(getString(R.string.wt_show_restart_test));
                            initWifi();
                        }
                    });
                    dialog.setNegativeButton(getString(R.string.wt_cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
            }
        });

        wifiStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWifiState) {
                    mWifiState = false;
                    mWifiManager.setWifiEnabled(false);
                    mWifiSwitchButton.setChecked(false);
                    showToast(getString(R.string.wt_stop_bluetooth_test));
                } else {
                    showToast(getString(R.string.wt_show_not_start_test));
                }
            }
        });

        this.registerReceiver(mWifiReceiver, makeWifiFilter());

        mWifiOpenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mWifiState && mWifiTestTimes > mWifiStartCount) {
                    mWifiManager.setWifiEnabled(true);
                } else {
                    mWifiState = false;
                    showToast(getString(R.string.wt_test_finished));
                }
            }
        });

        mWifiCloseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mWifiManager.setWifiEnabled(false);
            }
        });
    }

    private void initView() {
        mWifiVancantTimeEdit = (EditText) findViewById(R.id.wifi_switch_space_time);
        mWifiTestTimesEdit = (EditText) findViewById(R.id.wifi_switch_times);
        wifiStart = (Button) findViewById(R.id.wifi_start);
        wifiStop = (Button) findViewById(R.id.wifi_stop);
        mShowWifiCount = (TextView) findViewById(R.id.show_wifi_count);
        mWifiSwitchButton = (com.suke.widget.SwitchButton) findViewById(R.id.wifi_switch_button);
        mWifiProgressBar = (NumberProgressBar) findViewById(R.id.number_bar2);
        mReportMessage = (TextView) findViewById(R.id.report_message);
        mReportMessageTitle = (TextView) findViewById(R.id.report_message_title);
    }

    private void initWifi() {
        if (mWifiManager.isWifiEnabled()) {
            showToast(getString(R.string.wt_close_bluetooth_first));
        } else {
            mWifiManager.setWifiEnabled(true);
            mWifiVancantTime = Integer.parseInt(mWifiVancantTimeEdit.getText().toString());
            mWifiTestTimes = Integer.parseInt(mWifiTestTimesEdit.getText().toString());
            mWifiStartCount = 0;
            mWifiSucceedCount = 0;
            wifiOpenTemp = true;
            mWifiState = true;
            mWifiProgressBar.setMax(mWifiTestTimes);
            mWifiProgressBar.setProgress(mWifiSucceedCount);
            mReportMessageTitle.setText(getString(R.string.wt_report_bluetooth_test));
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && getCurrentFocus() != null) {
                if (getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            getTestMessage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mWifiReceiver);
    }

    BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mWifiState) {
                switch (mWifiManager.getWifiState()) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.i(TAG, "onReceive---------WiFi正在开启");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        wifiClose = true;
                        if (wifiOpenTemp) {
                            Log.i(TAG, "onReceive---------WiFi开启");
                            mWifiNotes ++;
                            mWifiStartCount ++;
                            mShowWifiCount.setText(getString(R.string.wt_No_)+ mWifiStartCount + "/" + mWifiTestTimes + getString(R.string.wt_test_times));
                            mWifiProgressBar.setProgress(mWifiStartCount);
                            mWifiSwitchButton.setChecked(true);
                            mHandler.removeCallbacks(mWifiOpenThread);
                            mHandler.postDelayed(mWifiCloseThread, mWifiVancantTime * 1000 + 1000);
                            wifiOpenTemp = false;
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        isClosing = true;
                        Log.i(TAG, "onReceive---------WiFi正在关闭");
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        mWifiCloseTemp ++;
                        wifiOpenTemp = true;
                        if (wifiClose && isClosing || mWifiCloseTemp == 2 && !isClosing) {
                            Log.i(TAG, "onReceive---------WiFi关闭");
                            mWifiSwitchButton.setChecked(false);
                            mHandler.removeCallbacks(mWifiCloseThread);
                            mHandler.postDelayed(mWifiOpenThread, mWifiVancantTime * 1000 + 1000);
                            Log.i(TAG, "mWifiNote: " + mWifiNotes);
                            mWifiNotes ++;
                            getTestMessage();
                            mWifiCloseTemp = 0;
                            wifiClose = false;
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.i(TAG, "onReceive---------WiFi未知");
                        break;
                }
                if (mWifiNotes == 2) {
                    mWifiSucceedCount++;
                    Log.i(TAG, "mWifiSucceedCount: " + mWifiSucceedCount);
                    getTestMessage();
                    mWifiNotes = 0;
                }
            }
        }
    };

    private IntentFilter makeWifiFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        return filter;
    }

    private void getTestMessage() {
        double percentage;
        if (mWifiStartCount == 0) {
            percentage = 0;
        } else {
            percentage = (mWifiSucceedCount * 1.0) / mWifiStartCount * 100;
        }
        mReportMessage.setText(getString(R.string.wt_successes_times) + mWifiSucceedCount + "\n"
                + getString(R.string.wt_failures_times) + (mWifiStartCount - mWifiSucceedCount) + "\n"
                + getString(R.string.wt_all_times) + mWifiStartCount + "\n"
                + getString(R.string.wt_successes_rate) + percentage + getString(R.string.wt_percentage));
    }

    /**
     * Toast
     *
     * @param content 内容
     */
    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(WifiActivity.this, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(WifiActivity.this, R.string.wt_exit, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
//            System.exit(0);
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
        }
    }
}
