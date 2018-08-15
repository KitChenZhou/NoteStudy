package com.ckt.test.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

public class PhoneUtils {

    private static Toast mToast = null;
    public static final int REQUEST_CODE_CALL_PERMISSION = 21;

//    /**
//     * 手机号码匹配
//     * @param number 手机号码
//     * @return 是否匹配
//     */
//    public static boolean isMobile(String number) {
//        String num = "[1][34578]\\d{9}";
//        if (TextUtils.isEmpty(number)) {
//            return false;
//        } else {
//            //matches():字符串是否在给定的正则表达式匹配
//            return number.matches(num);
//        }
//    }

    /**
     * 获取手机实例
     *
     * @param context 上下文
     * @return 手机实例
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
     * 吐司
     *
     * @param context 上下文
     * @param content 内容
     */
    public static void showToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

    /**
     * 拨号
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PERMISSION);
            return;
        }
        context.startActivity(intent);
    }

}
