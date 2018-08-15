package com.ckt.test.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.ckt.test.MainActivity;
import com.ckt.test.model.CallRecord;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 *
 * Created by D22395 on 2017/9/1.
 */

public class Utils {

    private static final int REQUEST_CODE_ASK_STORAGE_SETTINGS = 3;
    private static Toast toast = null;
    private static final String TAG = "MainActivityTag";
    private static final int REQUEST_CODE_ASK_CALL_SETTINGS = 2;

    private static final int NETWORK_TYPE_GPRS = 1;
    private static final int NETWORK_TYPE_EDGE = 2;
    private static final int NETWORK_TYPE_UMTS = 3;
    private static final int NETWORK_TYPE_CDMA = 4;
    private static final int NETWORK_TYPE_EVDO_0 = 5;
    private static final int NETWORK_TYPE_EVDO_A = 6;
    private static final int NETWORK_TYPE_1xRTT = 7;
    private static final int NETWORK_TYPE_HSDPA = 8;
    private static final int NETWORK_TYPE_HSUPA = 9;
    private static final int NETWORK_TYPE_HSPA = 10;
    private static final int NETWORK_TYPE_IDEN = 11;
    private static final int NETWORK_TYPE_EVDO_B = 12;
    private static final int NETWORK_TYPE_LTE = 13;
    private static final int NETWORK_TYPE_EHRPD = 14;
    private static final int NETWORK_TYPE_HSPAP = 15;


    /**
     * Toast
     * @param context 上下文
     * @param content 内容
     */
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 拨打电话
     * @param context 上下文
     * @param mPhoneNum  电话号码
     */
    public static void call(Context context, String mPhoneNum) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_SETTINGS);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhoneNum));
            context.startActivity(intent);
        }
    }

    /**
     * Log
     * @param content 上下文
     */
    public static void showLog(String content) {
        Log.i(TAG, content);
    }

    /**
     * 导出EXCEL文件
     * @param context 上下文
     * @param callLogList 需要导出的数据
     */
    public static void exportFile(final Context context, final ArrayList<CallRecord> callLogList) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_STORAGE_SETTINGS);
        } else {
            final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
            Log.i(TAG, "fileName = " + fileName);
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("提示！");
            dialog.setMessage("正在导出EXCEL文件到" + fileName + "，是否继续？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    File file = new File(fileName);
                    SaveToExcelUtil.createExcel(file);//  创建表格
                    for (CallRecord c : callLogList) {
                        SaveToExcelUtil.writeToExcel(file, c);
                    }
                    MainActivity.isExportReport = true;
                    showToast(context, "成功导出");
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.isExportReport = false;
                }
            });
            dialog.show();
        }
    }

    /**
     * 获取报告
     * @param context 上下文
     * @param callLogList 通话记录List
     */
    public static void getReport(Context context,  ArrayList<CallRecord> callLogList) {
        int validCall = 0; //有效拨打次数
        for (int i = 0; i < callLogList.size(); i++) {
            if (callLogList.get(i).getDuration() > 0) {
                validCall++;
            }
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("通话信息");
        dialog.setMessage("成功拨打次数： " + validCall + "\n"
                + "失败拨打次数： " + (callLogList.size() - validCall) + "\n"
                + "总计拨打次数： " + callLogList.size() + "\n"
                + "      呼通率      ： " + (validCall * 100.0f) / callLogList.size() + "%");
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
     * 检查SIM卡状态
     * @param context 上下文
     */
    public static void CheckSimCardState(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imsi = tm.getSimOperator();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                Utils.showToast(context, "中国移动:" + Utils.getNetworkClass(context,tm.getNetworkType()));
            } else if (imsi.startsWith("46001")) {
                Utils.showToast(context, "中国联通:" + Utils.getNetworkClass(context, tm.getNetworkType()));
            } else if (imsi.startsWith("46003")) {
                //中国电信
                Utils.showToast(context, "中国电信:" + Utils.getNetworkClass(context, tm.getNetworkType()));
            } else {
                Utils.showToast(context, "SIM不可用");
            }
        } else {
            Utils.showToast(context, "SIM不可用");
        }
    }

    /**
     * 获取ITelephony实例
     * @param context 上下文
     * @return ITelephony实例
     */
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

    /**
     * 获取当前网络状态
     * @param context 上下文
     * @param networkType 网络状态值
     * @return 网络状态
     */
    public static String getNetworkClass(Context context, int networkType) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.
                getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        String strSubTypeName = networkInfo.getSubtypeName();
        Log.i(TAG, "strSubTypeName :" + strSubTypeName);
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return "2G";
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return "3G";
            case NETWORK_TYPE_LTE:
                return "4G";
            default:
                if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                    return "3G";
                }
                return "unknow";
        }
    }
}
