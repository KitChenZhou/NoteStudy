package com.ckt.test.autobtwifi;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.test.autobtwifi.progressBar.NumberProgressBar;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivitytag";

    private static Toast toast = null;

    //退出时的时间
    private long mExitTime;

    private EditText mBTVancantTimeEdit;// 蓝牙空闲时间编辑
    private EditText mBTTestTimesEdit; // 蓝牙测试次数编辑
    private TextView mShowBTCount; // 列出蓝牙测试次数
    private com.suke.widget.SwitchButton mBTSwitchButton; // 模拟蓝牙开关
    private NumberProgressBar mBTProgressBar;// 蓝牙百分比进度条

    private EditText mWifiVancantTimeEdit; // WiFi空闲时间编辑
    private EditText mWifiTestTimesEdit; // WiFi测试次数编辑
    private TextView mShowWifiCount; // 列出WiFi测试次数
    private com.suke.widget.SwitchButton mWifiSwitchButton; // 模拟WiFi开关
    private NumberProgressBar mWifiProgressBar;// WiFi百分比进度条

    private int mBTVancantTime; // 蓝牙空闲时间
    private int mBTTestTimes; // 蓝牙测试次数
    private int mWifiVancantTime; // WiFi空闲时间
    private int mWifiTestTimes; // WiFi 测试次数

    private int mBTSucceedCount; // 蓝牙成功次数
    private int mWifiSucceedCount; // WiFi成功次数
    private int mBTStartCount; // 蓝牙第几次测试
    private int mWifiStartCount; // WiFi第几次测试
    private int mBTNotes = 0; // 蓝牙流程记录
    private boolean mWifiNotes = false;
    private boolean mBTState = false; // 蓝牙状态
    private boolean mWifiState = false; // WiFi状态
    private int mWifiCloseTemp = 0;
    private boolean isClosing = false;

    private boolean wifiClose = true;
    private boolean wifiOpenTemp = true;

    private Handler mHandler = new Handler();

    private Thread mBTOpenThread = null; // 蓝牙开启线程
    private Thread mBTCloseThread = null; // 蓝牙关闭线程

    private Thread mWifiOpenThread = null; // WiFi开启线程
    private Thread mWifiCloseThread = null; // WiFi关闭线程

    private WifiManager mWifiManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTVancantTimeEdit = (EditText) findViewById(R.id.BT_switch_space_time);
        mBTTestTimesEdit = (EditText) findViewById(R.id.BT_switch_times);
        Button BTStart = (Button) findViewById(R.id.BT_start);
        Button BTStop = (Button) findViewById(R.id.BT_stop);
        final Button getBTReport = (Button) findViewById(R.id.get_BT_report);
        mShowBTCount = (TextView) findViewById(R.id.show_BT_count);
        mBTSwitchButton = (com.suke.widget.SwitchButton) findViewById(R.id.BT_switch_button);
        mBTProgressBar = (NumberProgressBar) findViewById(R.id.number_bar1);

        mWifiVancantTimeEdit = (EditText) findViewById(R.id.wifi_switch_space_time);
        mWifiTestTimesEdit = (EditText) findViewById(R.id.wifi_switch_times);
        Button wifiStart = (Button) findViewById(R.id.wifi_start);
        Button wifiStop = (Button) findViewById(R.id.wifi_stop);
        final Button getWifiReport = (Button) findViewById(R.id.get_wifi_report);
        mShowWifiCount = (TextView) findViewById(R.id.show_wifi_count);
        mWifiSwitchButton = (com.suke.widget.SwitchButton) findViewById(R.id.wifi_switch_button);
        mWifiProgressBar = (NumberProgressBar) findViewById(R.id.number_bar2);

        mWifiManager = (WifiManager) super.getSystemService(Context.WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /* 初始蓝牙WiFi状态 */
        mWifiManager.setWifiEnabled(false);
        mBluetoothAdapter.disable();
        mBTSwitchButton.setChecked(false);
        mWifiSwitchButton.setChecked(false);

        mBTSwitchButton.setClickable(false);

        mBTSwitchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mWifiSwitchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        /* 蓝牙模拟开关监听 改变蓝牙状态 */
//        mBTSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (isChecked) {
//                    mBluetoothAdapter.enable();
//                } else {
//                    mBluetoothAdapter.disable();
//                }
//            }
//        });
//
//        /* 蓝牙模拟开关监听 改变蓝牙状态 */
//        mWifiSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                mWifiManager.setWifiEnabled(isChecked);
//            }
//        });

        /* 蓝牙开关测试开始 */
        BTStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBTState) {
                    if (mBluetoothAdapter != null) {
                        if (mBTVancantTimeEdit.getText().toString().length() != 0
                                && mBTTestTimesEdit.getText().toString().length() != 0) {
                            initBT();
                        } else {
                            showToast("请输入正确的参数！");
                        }
                    } else {
                        showToast("手机不支持蓝牙功能！！");
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setIcon(android.R.drawable.ic_dialog_info);
                    dialog.setTitle("提示!");
                    dialog.setMessage("正在反复开/关蓝牙压力测试，是否重新开始");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(TAG, "蓝牙状态：" + mBluetoothAdapter.isEnabled());
                            showToast("重新开始蓝牙压力测试");
                            initBT();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
            }
        });

        BTStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTState) {
                    mBTState = false;
                    showToast("停止蓝牙压力测试");
                    mBluetoothAdapter.disable();
                    mBTSwitchButton.setChecked(false);
                } else {
                    showToast("未开始蓝牙压力测试");
                }
            }
        });

        getBTReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBTReport();
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
                        showToast("请输入正确的参数！");
                    }
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setIcon(android.R.drawable.ic_dialog_info);
                    dialog.setTitle("提示!");
                    dialog.setMessage("正在反复开/关Wi-Fi压力测试，是否重新开始");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showToast("重新开始Wi-Fi压力测试");
                            initWifi();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                    showToast("停止Wi-Fi压力测试");
                } else {
                    showToast("未开始Wi-Fi压力测试");
                }
            }
        });

        getWifiReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWifiReport();
            }
        });

        this.registerReceiver(mBTReceiver, makeBTFilter());
        this.registerReceiver(mWifiReceiver, makeWifiFilter());

        mBTOpenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mBTState && mBTTestTimes > mBTStartCount) {
                    mBluetoothAdapter.enable();
                } else {
                    mBTState = false;
                    getBTReport();
                }
            }
        });

        mBTCloseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.disable();
            }
        });

        mWifiOpenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mWifiState && mWifiTestTimes > mWifiStartCount) {
                    mWifiManager.setWifiEnabled(true);
                } else {
                    mWifiState = false;
                    getWifiReport();
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

    private void initWifi() {
        if (mWifiManager.isWifiEnabled()){
            mWifiManager.
            showToast("请先关闭Wi-Fi状态！");
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
            mShowWifiCount.setText("Wi-Fi测试的次数");
        }
    }

    private void initBT() {
        if (mBluetoothAdapter.isEnabled()){
            showToast("请先关闭蓝牙状态！");
        } else {
            mBluetoothAdapter.enable();
            mBTVancantTime = Integer.parseInt(mBTVancantTimeEdit.getText().toString());
            mBTTestTimes = Integer.parseInt(mBTTestTimesEdit.getText().toString());
            mBTStartCount = 0;
            mBTSucceedCount = 0;
            Log.i(TAG, "蓝牙状态init：" + mBluetoothAdapter.isEnabled());
            mBTNotes = 0;
            mBTState = true;
            mBTProgressBar.setMax(mBTTestTimes);
            mBTProgressBar.setProgress(mBTSucceedCount);
            mShowBTCount.setText("BT测试的次数");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBTReceiver);
        this.unregisterReceiver(mWifiReceiver);
    }

    BroadcastReceiver mBTReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (mBTState) {
                switch (intent.getAction()) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch (blueState) {
                            case BluetoothAdapter.STATE_TURNING_ON:
                                mBTNotes ++;
                                mBTStartCount ++;
                                mShowBTCount.setText("第" + mBTStartCount + "/" + mBTTestTimes + "次测试");
                                mBTProgressBar.setProgress(mBTStartCount);
                                Log.i(TAG, "onReceive---------蓝牙正在开启");
                                break;
                            case BluetoothAdapter.STATE_ON:
                                mBTNotes ++;
                                Log.i(TAG, "onReceive---------蓝牙开启");
                                mBTSwitchButton.setChecked(true);
                                mHandler.removeCallbacks(mBTOpenThread);
                                mHandler.postDelayed(mBTCloseThread, mBTVancantTime * 1000 + 1000);
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF:
                                mBTNotes ++;
                                Log.i(TAG, "onReceive---------蓝牙正在关闭");
                                break;
                            case BluetoothAdapter.STATE_OFF:
                                mBTNotes ++;
                                Log.i(TAG, "onReceive---------蓝牙关闭");
                                mBTSwitchButton.setChecked(false);
                                mHandler.removeCallbacks(mBTCloseThread);
                                mHandler.postDelayed(mBTOpenThread, mBTVancantTime * 1000 + 1000);
                                if (mBTNotes == 4) {
                                    mBTSucceedCount++;
                                }
                                Log.i(TAG, "mBTSucceedCount: " + mBTSucceedCount);
                                mBTNotes = 0;
                                break;
                        }
                        break;
                }
            }
        }
    };


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
                                mWifiNotes = true;
                                mWifiStartCount ++;
                                mShowWifiCount.setText("第" + mWifiStartCount + "/" + mWifiTestTimes + "次测试");
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
                                if (mWifiNotes) {
                                    mWifiSucceedCount++;
                                    Log.i(TAG, "mWifiSucceedCount: " + mWifiSucceedCount);
                                }
                                mWifiNotes = false;
                                mWifiCloseTemp = 0;
                                wifiClose = false;
                            }
                            break;
                        case WifiManager.WIFI_STATE_UNKNOWN:
                            Log.i(TAG, "onReceive---------WiFi未知");
                            break;
                    }
                }
            }
    };

    private IntentFilter makeBTFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return filter;
    }


    private IntentFilter makeWifiFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        return filter;
    }

    private void getBTReport() {
        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("蓝牙测试信息");
        dialog.setMessage("成功测试次数： " + mBTSucceedCount + "\n"
                + "失败测试次数： " + (mBTStartCount - mBTSucceedCount) + "\n"
                + "总计测试次数： " + mBTStartCount + "\n"
                + "      成功率      ： " + (mBTSucceedCount * 1.0) / mBTStartCount * 100 + "%");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void getWifiReport() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Wi-Fi测试信息");
        dialog.setMessage("成功测试次数： " + mWifiSucceedCount + "\n"
                + "失败测试次数： " + (mWifiStartCount - mWifiSucceedCount) + "\n"
                + "总计测试次数： " + mWifiStartCount + "\n"
                + "      成功率      ： " + (mWifiSucceedCount * 1.0) / mWifiStartCount * 100 + "%");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }



    /**
     * Toast
     *
     * @param content 内容
     */
    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT);
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
            Toast.makeText(MainActivity.this, "再按一次退出AutoBTWifi", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
//            finish();
//            System.exit(0);
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }
}
