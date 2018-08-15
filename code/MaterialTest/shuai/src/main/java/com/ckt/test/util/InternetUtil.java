package com.ckt.test.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

public class InternetUtil {

    //没有网络连接
    private static final int NETWORN_NONE = 0;
    //手机网络数据连接类型
    private static final int NETWORN_2G = 2;
    private static final int NETWORN_3G = 3;
    private static final int NETWORN_4G = 4;
    private static final int NETWORN_MOBILE = 5;

    /**
     * 获取当前网络连接类型
     *
     * @param context 上下文
     * @return 状态
     */
    private static int getNetworkState(Context context) {
        //获取系统的网络服务
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager) return NETWORN_NONE;
        //获取当前网络类型，如果为空，或者不可用，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }

        // 判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.
                getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case NETWORK_TYPE_GPRS: // 联通2g
                        case NETWORK_TYPE_CDMA: // 电信2g
                        case NETWORK_TYPE_EDGE: // 移动2g
                        case NETWORK_TYPE_1xRTT:
                        case NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //如果是3g类型
                        case NETWORK_TYPE_EVDO_A: // 电信3g
                        case NETWORK_TYPE_UMTS:
                        case NETWORK_TYPE_EVDO_0:
                        case NETWORK_TYPE_HSDPA:
                        case NETWORK_TYPE_HSUPA:
                        case NETWORK_TYPE_HSPA:
                        case NETWORK_TYPE_EVDO_B:
                        case NETWORK_TYPE_EHRPD:
                        case NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //如果是4g类型
                        case NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") ||
                                    strSubTypeName.equalsIgnoreCase("WCDMA") ||
                                    strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    public static String getNetworkStateString(Context context) {
        switch (getNetworkState(context)) {
            case NETWORN_NONE:
                return "unknow";
            case 2:
                return "2G";
            case 3:
                return "3G";
            case 4:
                return "4G";
        }
        return null;
    }

    /**
     * 获得网络类型
     *
     * @param networkType 网络类型字段
     * @return 网络类型
     */
    public static String getNetworkType(int networkType) {

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
                return "unknow";
        }
    }

}
