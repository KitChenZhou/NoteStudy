package com.youli.feiyu.fynotification.utility;

/**
 * Created by Sean on 16/2/26.
 */
public class Tools {
    public static int transformMillisecondsToMinute(long millisecond) {
        return (int) (millisecond / 1000 / 60);
    }

    public static long transformMinuteToMilliseconds(int minute) {
        return minute * 60 * 1000;
    }

    public static long transformSecondsToMilliseconds(int seconds) {
        return seconds * 1000;
    }
}
