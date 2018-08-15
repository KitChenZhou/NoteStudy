package com.ckt.demo.ndktest;

import android.util.Log;

/**
 * Created by D22395 on 2018/7/17.
 */

public class JNIUtils {

    static {
        System.loadLibrary("JNIHello");
    }


    public static native String sayHelloFromJNI();

}
