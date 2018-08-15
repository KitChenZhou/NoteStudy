//
// Created by D22395 on 2018/7/17.
//
#include "com_ckt_demo_ndktest_JNIUtils.h"
JNIEXPORT jstring JNICALL Java_com_ckt_demo_ndktest_JNIUtils_sayHelloFromJNI
        (JNIEnv *env, jclass jclass){
return env->NewStringUTF("Hello World From JNI!!!!!");
}
