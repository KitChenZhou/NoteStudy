package com.ckt.test.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wgp on 2017/8/31.
 * 处理日期和格式化日期的工具类
 */

public class DateUtils {
    private static final String SHORT_YEAR_FORMAT = "yyyy-MM-dd HH:mm";// 采取年简短月简短，不显示秒
    private static final String DETAIL_FORMAT = "yyyy-MM-dd HH:mm:ss";// 带时间详情的格式方式2000-00-00
    private static SimpleDateFormat detailFormat = new SimpleDateFormat(DETAIL_FORMAT);
    private static SimpleDateFormat shortYearFormat = new SimpleDateFormat(SHORT_YEAR_FORMAT);

    /**
     * 返回日期的详细格式：yyyy-MM-dd HH:mm:ss
     *
     * @param date 传入一个日期
     * @return
     */
    public static String detailFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return detailFormat.format(date);
        }
    }

    /**
     * 返回两位数的年份的日期格式不显示秒，主要用于当两个时间相差很大时显示15-12-3 15:25
     *
     * @param date
     * @return
     */
    public synchronized static String shortYearFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return shortYearFormat.format(date);
        }
    }
}
