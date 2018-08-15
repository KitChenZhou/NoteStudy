package com.ckt.test;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.CallLog.Calls;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.ckt.test.model.CallRecord;
import com.ckt.test.Utils.MyDataBaseHelper;
import com.ckt.test.Utils.Utils;
import com.ckt.test.progressBar.NumberProgressBar;
import com.ckt.test.progressBar.OnProgressBarListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnProgressBarListener {

    public static final String TAG = "MainActivityTag";
    private static final int REQUEST_CODE_ASK_WRITE_SETTINGS = 1;
    private static final int REQUEST_CODE_ASK_CALL_SETTINGS = 2;
    private static final int REQUEST_CODE_ASK_STORAGE_SETTINGS = 3;

    private AutoCompleteTextView mPhoneNumEdit = null; // 设置拨打的电话号码
    private EditText mSpaceTimeEdit = null; // 设置间隔时间
    private EditText mCallDurationTimeEdit = null; // 设置通话时长
    private EditText mCallCountsEdit = null; // 设置拨号次数
    private TextView mShowAlreadyCallCount = null;// 显示已打电话次数
    private NumberProgressBar mNumberProgressBar;// 百分比进度条

    private boolean mEndCall = false;  // 判断是否正常挂断
    private boolean mCallState = false; // 判断电话通话状态
    private boolean isGetReport = false; // 判断是否获得报告
    public static boolean isExportReport = false;// 判断是否导出报告

    private int spaceTime = 0; // 间隔时间
    private int alreadyCallCount = 0; // 已拨打次数
    private int allCallCount = 0;  // 需要拨打次数
    private int sleepTimes; // 休眠时间
    private int durationTime = 0; // 拨号持续时间

    private String phoneNum = null; // 电话号码
    private String sleepTimeString; // spinner里的休眠时间
    private String networkType; // 网络状态

    private ITelephony iPhoney = null;
    private TelephonyManager tm; // 电话管理器

    Thread mCallThread = null; // 打电话线程
    Thread mEndThread = null;  // 挂电话线程
    Handler handler = new Handler();

    private ArrayList<CallRecord> callRecordList = new ArrayList<>(); // 存放CallRecord对象
    private ArrayList<String> phoneNumberList = new ArrayList<>();// 存放电话号码
    private ArrayList<String> networkTypeList = new ArrayList<>(); // 存放信号状态

    private MyDataBaseHelper dbHelper;
    private ArrayAdapter<String> phoneNumberAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sleepSpinner = (Spinner) findViewById(R.id.sleep_spinner);
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
        sleepSpinner.setAdapter(adapter);

        Button setSleepTime = (Button) findViewById(R.id.start_sleep_time);
        Button startBtn = (Button) findViewById(R.id.startBtn);
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        Button getReports = (Button) findViewById(R.id.get_reports);
        Button exportFile = (Button) findViewById(R.id.export_file);

        mPhoneNumEdit = (AutoCompleteTextView) findViewById(R.id.phone_num);
        mSpaceTimeEdit = (EditText) findViewById(R.id.wait_time);
        mCallDurationTimeEdit = (EditText) findViewById(R.id.call_duration_time);
        mCallCountsEdit = (EditText) findViewById(R.id.call_times);
        mShowAlreadyCallCount = (TextView) findViewById(R.id.show_count);
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.numberbar1);
        mNumberProgressBar.setOnProgressBarListener(this);

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); // 手机通话状态监听器开启
        iPhoney = Utils.getITelephony(this);//获取电话实例

        /*
         * 获取数据库数据
         */
        dbHelper = new MyDataBaseHelper(this, "Number.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("database", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String Num = cursor.getString(cursor.getColumnIndex("number"));
                if (!phoneNumberList.contains(Num)) {
                    Log.i(TAG, "Num = " + Num);
                    phoneNumberList.add(Num);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(phoneNumberList);
        phoneNumberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, phoneNumberList);//配置Adapter
        mPhoneNumEdit.setAdapter(phoneNumberAdapter);
        mPhoneNumEdit.setDropDownHeight(280);
        mPhoneNumEdit.setThreshold(1);

        /*
          开始拨号按钮
         */
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    Utils.showToast(MainActivity.this, "无SIM卡");
                } else {
                    if (!mCallState) {
                        if (mPhoneNumEdit.getText().toString().length() != 0
                                && mSpaceTimeEdit.getText().toString().length() != 0
                                && mCallCountsEdit.getText().toString().length() != 0
                                && mCallDurationTimeEdit.getText().toString().length() != 0
                                && Integer.parseInt(mCallCountsEdit.getText().toString()) != 0) {
                            phoneNum = mPhoneNumEdit.getText().toString().trim();

                            //插入号码到数据库
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("number", phoneNum);
                            if (!phoneNumberList.contains(phoneNum)) {
                                db.insert("database", null, values);// 将手机号存入数据库
                                Log.i(TAG, "insert");
                                Log.i(TAG, "Num = " + phoneNum);
                                phoneNumberList.add(0, phoneNum);
                            } else {
                                db.delete("database", "number = ?", new String[]{phoneNum});
                                db.insert("database", null, values);
                                phoneNumberList.remove(phoneNum);
                                phoneNumberList.add(0, phoneNum);
                            }
                            initData();// 初始化号码
                            values.clear();
                            mPhoneNumEdit.setAdapter(phoneNumberAdapter);

                            networkTypeList.clear();// 清空网络状态
                            callRecordList.clear();// 清空通话数据
                            Utils.CheckSimCardState(MainActivity.this);// Toast当前信号状态及运营商

                            spaceTime = Integer.parseInt(mSpaceTimeEdit.getText().toString());//获取拨号间隔
                            allCallCount = Integer.parseInt(mCallCountsEdit.getText().toString()); // 获取拨号次数
                            durationTime = Integer.parseInt(mCallDurationTimeEdit.getText().toString()); // 获取通话时间

                            mNumberProgressBar.setMax(allCallCount); // 设置进度条
                            mCallState = true; // 开始设置拨号状态为true
                            isGetReport = false;

                            networkType = Utils.getNetworkClass(MainActivity.this, tm.getNetworkType());// 获取拨号前的信号状态
                            networkTypeList.add(networkType);
                            Utils.call(MainActivity.this, phoneNum);// 拨打电话
                            alreadyCallCount = 1;
                            mNumberProgressBar.setProgress(alreadyCallCount); // 设置进度条
                            mShowAlreadyCallCount.setText("第" + alreadyCallCount + "/" + allCallCount + "次拨打");
                        } else {
                            Utils.showToast(MainActivity.this, "请输入正确的拨号参数");
                        }
                    } else {
                        Utils.showToast(MainActivity.this, "请拨号完成后再开始");
                    }
                }
            }
        });

        /*
          设置下拉框
         */
        sleepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                sleepTimeString = adapter.getItem(i);
                switch (i) {
                    case 0:
                        sleepTimes = 15;
                        break;
                    case 1:
                        sleepTimes = 30;
                        break;
                    case 2:
                        sleepTimes = 60;
                        break;
                    case 3:
                        sleepTimes = 120;
                        break;
                    case 4:
                        sleepTimes = 300;
                        break;
                    case 5:
                        sleepTimes = 600;
                        break;
                    case 6:
                        sleepTimes = 1800;
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*
          设置休眠时间
         */
        setSleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Settings.System.canWrite(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQUEST_CODE_ASK_WRITE_SETTINGS);
                } else {
                    Settings.System.putInt(getContentResolver(),
                            Settings.System.SCREEN_OFF_TIMEOUT,
                            sleepTimes * 1000);
                    Utils.showToast(MainActivity.this, "已设置休眠时间为：" + sleepTimeString);
                }
            }
        });

        /*
          停止按钮
         */
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallState = false;//切换监听器状态
            }
        });

        /*
          获取报告按钮
         */
        getReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alreadyCallCount != 0) {
                    getCallInfo();//获取通话数据
                    isGetReport = true;
                    Utils.getReport(MainActivity.this, callRecordList);
                } else {
                    Utils.showToast(MainActivity.this, "请拨打电话后查询详细信息");
                }
            }
        });
        /*
          导出通话记录文件
         */
        exportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetReport) {
                    Utils.exportFile(MainActivity.this, callRecordList); // 导出文件
                } else {
                    Utils.showToast(MainActivity.this, "请先获取报告！！");
                }
            }
        });

        /*
        拨打电话线程
         */
        mCallThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mCallState && allCallCount > alreadyCallCount) {
                    // 获取拨号时的网络状态
                    networkType = Utils.getNetworkClass(MainActivity.this, tm.getNetworkType());
                    networkTypeList.add(networkType);
                    Utils.call(MainActivity.this, phoneNum);// 拨打电话
                    Utils.showToast(MainActivity.this, networkType);
                    Utils.showLog("开始打电话");

                    alreadyCallCount++;// 拨打次数 + 1
                    mNumberProgressBar.setProgress(alreadyCallCount);
                    mShowAlreadyCallCount.setText("第" + alreadyCallCount + "/" + allCallCount + "次拨打");
                    Utils.showLog("拨打次数:" + alreadyCallCount);
                } else {
                    Utils.showToast(MainActivity.this, "已停止打电话");
                    mCallState = false;
                }
            }
        });

        /*
        挂断电话线程
         */
        mEndThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mEndCall = iPhoney.endCall();// 挂断电话
                    if (alreadyCallCount > allCallCount - 1) {// 判断打电话次数更改拨号状态
                        mCallState = false;
                    }
                    Utils.showLog("是否成功挂断：" + mEndCall);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        Utils.showLog("over");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示！");
        alertDialog.setMessage("正在退出程序，是否继续？");
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        if (alreadyCallCount != 0) {
            alertDialog.setTitle("警告！");
            if (!isExportReport) {
                alertDialog.setMessage("未导出EXCEL文件，是否继续退出程序？");
            }
            if (!isGetReport) {
                alertDialog.setMessage("未获取报告，是否继续退出程序？");
            }
        }
        alertDialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“取消”后的操作,这里不设置没有任何操作
            }
        });
        alertDialog.show();
    }

    /*
    电话监听器
     */
    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            Utils.showLog("监听器开启....");
            if (mCallState) {
                switch (state) {
                    // 挂断状态
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (allCallCount == alreadyCallCount) {
                            mCallState = false;
                        }
                        handler.removeCallbacks(mEndThread);
                        handler.postDelayed(mCallThread, spaceTime * 1000);
                        break;
                    // 接听状态
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        handler.postDelayed(mEndThread, durationTime * 1000 + 5000);
                        break;
                    // 响铃状态
                    case TelephonyManager.CALL_STATE_RINGING:
                        Utils.showLog("响铃:来电号码" + incomingNumber);
                        // 输出来电号码
                        break;
                }
            } else {
                Utils.showLog("监听器不运行");
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_SETTINGS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.showToast(MainActivity.this, "已获取拨打电话权限...");
                    Utils.call(MainActivity.this, phoneNum);
                } else {
                    mNumberProgressBar.setProgress(0);
                    Utils.showToast(MainActivity.this, "已拒绝拨打电话权限...");
                }
                break;
            case REQUEST_CODE_ASK_STORAGE_SETTINGS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.showToast(MainActivity.this, "已获取导出文件权限...");
                    Utils.exportFile(MainActivity.this, callRecordList);
                } else {
                    Utils.showToast(MainActivity.this, "已拒绝导出文件权限...");
                }
        }
    }

    /*
        获取通话数据
    */
    private void getCallInfo() {
        callRecordList.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, 20);
            return;
        }
        Cursor cursor = getContentResolver().query(Calls.CONTENT_URI,
                new String[]{Calls.DURATION, Calls.DATE},
                "type = 2",
                null,
                Calls.DEFAULT_SORT_ORDER);
        MainActivity.this.startManagingCursor(cursor);
        assert cursor != null;
        boolean hasRecord = cursor.moveToFirst();
        int count = 0;
        while (hasRecord) {
            int duration = cursor.getInt(cursor.getColumnIndex(Calls.DURATION));
            long startLong = cursor.getLong(cursor.getColumnIndex(Calls.DATE));
            Date startDate = new Date(startLong);
            long endLong = startDate.getTime() + duration * 1000;
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endLong));

            CallRecord callLog = new CallRecord(
                    alreadyCallCount - count,
                    startTime,
                    endTime,
                    duration,
                    networkTypeList.get(alreadyCallCount - count - 1),
                    duration > 0 ? "pass" : "fail");
            count++;
            callRecordList.add(callLog);
            hasRecord = cursor.moveToNext();
            if (count == alreadyCallCount) {
                Collections.reverse(callRecordList);
                break;
            }
        }
    }

    private void initData() {
        phoneNumberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumberList);
    }

    @Override
    public void onProgressChange(int current, int max) {

    }
}
