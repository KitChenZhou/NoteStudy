package com.ckt.test;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.internal.telephony.ITelephony;
import com.ckt.test.model.CallRecord;
import com.ckt.test.util.ExcelUtil;
import com.ckt.test.util.FileUtil;
import com.ckt.test.util.InternetUtil;
import com.ckt.test.util.PhoneUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static com.ckt.test.util.PhoneUtils.REQUEST_CODE_CALL_PERMISSION;
import static com.ckt.test.util.PhoneUtils.showToast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivitytag";
    private static final int REQUEST_CODE_SET_DORATION = 25;
    public static final int REQUEST_CODE_WRITE_FILE = 23;

    private EditText mInputPhoneNumber = null;
    private EditText mInputSpaceTime = null;
    private EditText mInputDialingCount = null;
    private EditText mInputDurationTime = null;

    private Button mConnection = null;
    private Button mStartBtn = null;

    private ProgressBar mProgressBar = null;

    private boolean endCall = false;//是否结束通话
    private boolean callState = false;//控制监听器内部状态
    private boolean isGetReport = false;//是否获取报告
    public static boolean isExportReport = false;//是否导出报告

    private String PhoneNumber = null;//电话号码
    private String networkType = null;//网络类型
    private String mSleepTimeString = null;//休眠时间字符串

    private int spaceTime = 0;//间隔时间
    private int durationTime = 0;//通话时间
    private int alreadyCallCount = 0;//已拨次数
    private int allCallCount = 0;//总拨号次数
    private int sleepTime = 0;//休眠时间

    private ArrayList<Integer> durationTimesList = new ArrayList<>();//通话时间集合
    private ArrayList<CallRecord> callDetailList = new ArrayList<>();//通话详情集合
    private ArrayList<String> networkTypeList = new ArrayList<>();//网络类型集合

    ITelephony iPhone = null;//手机实例
    Thread callThread = null;//拨号线程
    Thread endThread = null;//挂断线程

    Handler handler = new Handler();
    TelephonyManager tm = null;//电话管理器

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PhoneUtils.showToast(MainActivity.this, "屏幕旋转");
//        PhoneUtils.showToast(MainActivity.this, "alreadyCallCount = " + alreadyCallCount);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mSetSleep = (Button) findViewById(R.id.set_sleep_time);
        mStartBtn = (Button) findViewById(R.id.startBtn);
        Button mStopBtn = (Button) findViewById(R.id.stopBtn);
        mConnection = (Button) findViewById(R.id.dial_count);
        Button mGetReport = (Button) findViewById(R.id.get_reports);
        Button mExportReport = (Button) findViewById(R.id.export_reports);

        mInputPhoneNumber = (EditText) findViewById(R.id.input_phone_number);
        mInputSpaceTime = (EditText) findViewById(R.id.input_space_time);
        mInputDurationTime = (EditText) findViewById(R.id.input_call_duration);
        mInputDialingCount = (EditText) findViewById(R.id.input_dialing_counts);

        Spinner spinner = (Spinner) findViewById(R.id.sleep_spinner);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        PhoneNumber = FileUtil.load(MainActivity.this);
        if (!TextUtils.isEmpty(PhoneNumber)) {
            mInputPhoneNumber.setText(PhoneNumber);
            mInputPhoneNumber.setSelection(PhoneNumber.length());
        }
        /*
          设置下拉框格式：
          建立数据源
          建立Adapter并且绑定数据源
          绑定 Adapter到控件
         */
        String[] mItems = getResources().getStringArray(R.array.sleep_time_choose);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);//获得电话服务
        iPhone = PhoneUtils.getITelephony(this);//获取电话实例
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//注册监听器

        //开始拨号按钮
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    showToast(MainActivity.this, "请插入SIM卡");
                } else {
                    if (mInputPhoneNumber.getText().toString().length() != 0
                            && mInputSpaceTime.getText().toString().length() != 0
                            && mInputDialingCount.getText().toString().length() != 0
                            && mInputDurationTime.getText().toString().length() != 0
                            && Integer.parseInt(mInputDialingCount.getText().toString()) != 0) {

                        networkTypeList.clear();//网络类型清零

                        PhoneNumber = mInputPhoneNumber.getText().toString().trim();//获得电话号码
                        spaceTime = Integer.parseInt(mInputSpaceTime.getText().toString());//获得间隔时间
                        durationTime = Integer.parseInt(mInputDurationTime.getText().toString());//获得通话时间
                        allCallCount = Integer.parseInt(mInputDialingCount.getText().toString());//获得拨号总次数
                        networkType = InternetUtil.getNetworkType(tm.getNetworkType());//获得通话之前网络类型
                        networkTypeList.add(networkType);//添加到集合

                        PhoneUtils.call(MainActivity.this, PhoneNumber);//拨号
                        mStartBtn.setEnabled(false);
                        FileUtil.save(MainActivity.this, PhoneNumber);
                        Log.i(TAG, "开始打电话");
                        callState = true;
                        isGetReport = false;
                        alreadyCallCount = 1;
                        Log.i(TAG, "拨打次数：" + alreadyCallCount);
                        mConnection.setText("第" + alreadyCallCount + "/" + allCallCount + "次拨打");
                        //更新进度条
                        mProgressBar.setMax(allCallCount);
                        mProgressBar.setProgress(alreadyCallCount);
                    } else {
                        showToast(MainActivity.this, "请输入正确的通话参数");
                    }

                }
            }
        });

        //停止通话按钮
        mStopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callState = false;
                mStartBtn.setEnabled(true);//开始通话按钮可点击
                showToast(MainActivity.this, "停止通话");
            }
        });

        //获得下拉框数据
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                mSleepTimeString = adapter.getItem(i);
                switch (i) {
                    case 0:
                        sleepTime = 15;
                        break;
                    case 1:
                        sleepTime = 30;
                        break;
                    case 2:
                        sleepTime = 60;
                        break;
                    case 3:
                        sleepTime = 120;
                        break;
                    case 4:
                        sleepTime = 300;
                        break;
                    case 5:
                        sleepTime = 600;
                        break;
                    case 6:
                        sleepTime = 1800;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //设置休眠按钮
        mSetSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Settings.System.canWrite(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQUEST_CODE_SET_DORATION);
                } else {
                    Settings.System.putInt(getContentResolver(),
                            Settings.System.SCREEN_OFF_TIMEOUT,
                            sleepTime * 1000);//设置休眠
                    showToast(MainActivity.this, "已设置休眠时间为：" + mSleepTimeString);
                }
            }
        });

        //获得报告按钮
        mGetReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alreadyCallCount != 0) {
                    getCallRecords();//获得通话记录
                    int validCallTimes = 0;//有效通话次数
                    for (int i = 0; i < durationTimesList.size(); i++) {
                        if (durationTimesList.get(i) > 0) {
                            validCallTimes++;
                        }
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("通话信息");
                    dialog.setMessage("总计拨打次数： " + durationTimesList.size() + "\n"
                            + "成功拨打次数： " + validCallTimes + "\n"
                            + "失败拨打次数： " + (durationTimesList.size() - validCallTimes) + "\n"
                            + "呼通率： " + (validCallTimes * 1.0) / durationTimesList.size() * 100 + "%");
                    dialog.setCancelable(true);
                    dialog.show();
                    isGetReport = true;
                } else {
                    showToast(MainActivity.this, "请拨号后再查询");
                }
            }
        });

        //导出文件按钮
        mExportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetReport) {
                    ExcelUtil.exportReport(MainActivity.this, callDetailList);
                } else {
                    showToast(MainActivity.this, "请先获取报告");
                }
            }
        });

        //拨号线程
        callThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (callState && alreadyCallCount < allCallCount) {
                    networkType = InternetUtil.getNetworkType(tm.getNetworkType());//获取拨号前网络状态
                    networkTypeList.add(networkType);//添加到集合
                    PhoneUtils.call(MainActivity.this, PhoneNumber);//拨号
                    Log.i(TAG, "开始打电话");
                    alreadyCallCount++;
                    Log.i(TAG, "拨打次数:" + alreadyCallCount);
                    //进度更新
                    mProgressBar.setProgress(alreadyCallCount);
                    mConnection.setText("第" + alreadyCallCount + "/" + allCallCount + "次拨打");
                }
            }
        });

        //挂断线程
        endThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    endCall = iPhone.endCall();//挂断电话
                    if (alreadyCallCount > allCallCount - 1) {
                        callState = false;
                        mStartBtn.setEnabled(true);
                    }
                    Log.i(TAG, "是否成功挂断：" + endCall);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示！");
            if (alreadyCallCount == 0) {
                dialog.setMessage("是否继续执行退出操作？");
            } else {
                if (!isExportReport) {
                    dialog.setMessage("未导出EXCEL文件..." + "\n" +
                            "是否继续退出？");
                } else {
                    dialog.setMessage("是否继续执行退出操作？");
                }
                if (!isGetReport) {
                    dialog.setMessage("未获取报告..." + "\n" +
                            "是否继续退出？");
                }
            }
            dialog.setCancelable(false);
            dialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        Log.i(TAG, "监听结束");
    }

    //电话状态监听器
    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            if (callState) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE://空闲状态
                        if (alreadyCallCount == allCallCount) {
                            mStartBtn.setEnabled(true);
                        }
                        Log.i(TAG, "挂断状态");
                        handler.removeCallbacks(endThread);//杀死挂断线程
                        handler.postDelayed(callThread, spaceTime * 1000);//延时执行拨号线程
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://摘机状态
                        Log.i(TAG, "接听状态");
                        handler.postDelayed(endThread, durationTime * 1000 + 5000);//延时执行挂断状态
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.i(TAG, "响铃:来电号码" + incomingNumber);
                        // 输出来电号码
                        break;
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CALL_PERMISSION:
                if (grantResults.length != 0 && grantResults[0] == PERMISSION_GRANTED) {
                    showToast(MainActivity.this, "已获得通话权限...");
                    PhoneUtils.call(MainActivity.this, PhoneNumber);
                } else {
                    showToast(MainActivity.this, "已拒绝通话权限...");
                    mConnection.setText("已拨打次数");
                    mProgressBar.setProgress(0);
                    mStartBtn.setEnabled(true);
                }
                break;
            case REQUEST_CODE_WRITE_FILE:
                if (grantResults.length != 0 && grantResults[0] == PERMISSION_GRANTED) {
                    showToast(MainActivity.this, "已获得导出文件权限...");
                    ExcelUtil.exportReport(MainActivity.this, callDetailList);
                } else {
                    showToast(MainActivity.this, "已拒绝导出文件权限...");
                }
                break;
        }
    }

    /**
     * 获得通话时间
     */
    public void getCallRecords() {
        //记录清零
        durationTimesList.clear();
        callDetailList.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.DURATION, CallLog.Calls.DATE, CallLog.Calls.DATA_USAGE},
                "type = 2",
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
        MainActivity.this.startManagingCursor(cursor);
        assert cursor != null;
        boolean hasRecord = cursor.moveToFirst();
        int count = 0;//读取通话记录计数器
        while (hasRecord) {
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//持续时间
            long startLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            Date date = new Date(startLong);
            long endLong = date.getTime() + duration * 1000;
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//开始时间
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endLong));//结束时间

            CallRecord record = new CallRecord(
                    alreadyCallCount - count,
                    startTime,
                    endTime,
                    duration,
                    networkTypeList.get(alreadyCallCount - count - 1),
                    duration > 0 ? "pass" : "fail");
            durationTimesList.add(duration);//通话时间List
            callDetailList.add(record);//通话详细记录List
            count++;
            hasRecord = cursor.moveToNext();
            if (count == alreadyCallCount) {
                Collections.reverse(durationTimesList);
                Collections.reverse(callDetailList);
                Log.i(TAG, networkTypeList.toString());
                break;
            }
        }
    }
}
