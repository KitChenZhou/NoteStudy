package com.youli.feiyu.fynotification.utility;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.youli.feiyu.fynotification.bean.CurrentTaskBean;
import com.youli.feiyu.fynotification.bean.DoneTaskBean;
import com.youli.feiyu.fynotification.config.Config;

import java.util.ArrayList;

/**
 * Created by Sean on 16/2/26.
 */
public class PhoneCallHelper {

    private Context mContext;
    private CurrentTaskDBHelper mCDBHelper;
    private DoneTaskDBHelper mDDBHelper;
    private long mIntervalTime;

    public PhoneCallHelper(Context context) {
        mContext = context;
        mCDBHelper = new CurrentTaskDBHelper(context);
        mDDBHelper = new DoneTaskDBHelper(context);
        mIntervalTime = getIntervalTime();
    }

    public void addTaskNumbers(ArrayList<String> numbers) {
        for (String number: numbers) {
            CurrentTaskBean bean = new CurrentTaskBean();
            bean.setPhoneNumber(number);
            bean.setAddTime(System.currentTimeMillis());
            mCDBHelper.saveItem(bean);
        }
        Log.d(Config.TAG, "PhoneCallHelper->addTaskNumbers: " + getOnCall());
        if(!getOnCall()) { //如果通话状态为否执行，否则只添加
            executeTask();
        }
    }

    public void executeTask() {
        CurrentTaskBean cBean = mCDBHelper.getLastItem();
        if(cBean == null) {
            //设置通话状态为否
            setOnCall(false);
            return;
        }
        //设置通话状态为是
        setOnCall(true);
        String cNumber = cBean.getPhoneNumber();
        mCDBHelper.deleteItemWithNumber(cNumber);

        DoneTaskBean dBean = mDDBHelper.getItemWithNumber(cNumber);
        if(dBean == null) {
            call(cNumber);
            updateTime(cNumber);
        } else {
            long sTime = System.currentTimeMillis() - dBean.getAddTime();
            Log.d(Config.TAG, "PhoneCallHelper->executeTask: " + cNumber + " 间隔: " + mIntervalTime + " 过去: " + sTime);
            if(sTime > mIntervalTime) {
                call(cNumber);
                updateTime(cNumber);
            } else {
                executeTask();
            }
        }
    }

    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
    }

    private void updateTime(String number) {
        DoneTaskBean dNewBean = new DoneTaskBean();
        dNewBean.setPhoneNumber(number);
        dNewBean.setAddTime(System.currentTimeMillis());
        mDDBHelper.updateItem(dNewBean);
    }

    private void setOnCall(boolean isOnCall) {
        PreferenceHelper.putBoolean(Config.FLAG_IS_ON_CALL_TASK, isOnCall, mContext);
    }

    private boolean getOnCall() {
        return PreferenceHelper.getBoolean(Config.FLAG_IS_ON_CALL_TASK, mContext);
    }

    private long getIntervalTime() {
        int sIntervalSecond = PreferenceHelper.getInt(Config.FLAG_CALL_INTERVAL_SECOND, mContext);
        return Tools.transformSecondsToMilliseconds(sIntervalSecond);
    }
}
