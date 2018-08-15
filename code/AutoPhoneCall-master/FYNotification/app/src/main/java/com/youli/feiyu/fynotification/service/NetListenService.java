package com.youli.feiyu.fynotification.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youli.feiyu.fynotification.MainActivity;
import com.youli.feiyu.fynotification.R;
import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.utility.ItemsDBHelper;
import com.youli.feiyu.fynotification.utility.PhoneCallHelper;
import com.youli.feiyu.fynotification.utility.PreferenceHelper;
import com.youli.feiyu.fynotification.utility.Tools;

import java.util.ArrayList;

public class NetListenService extends Service {

    private NetListenBinder mBinder;
    private RequestQueue mQueue;
    private int notificationCount;

    public NetListenService() {
        mBinder = new NetListenBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Config.TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Config.TAG, "onCreate: ");
        mQueue = Volley.newRequestQueue(getApplicationContext());
        refreshNotification(0);
        executeNetListen();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Config.TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(Config.TAG, "onDestroy: ");
        super.onDestroy();
    }

    class NetListenBinder extends Binder {

    }

    private void refreshNotification(int notificationCount) {
        NotificationManager sNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder sBuilder = new NotificationCompat
                .Builder(NetListenService.this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在监听")
                .setContentText("已收到警报" + notificationCount + "条");
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        sBuilder.setContentIntent(resultPendingIntent);
        sNM.notify(0, sBuilder.build());
    }

    private void requestServer() {

        StringRequest request = new StringRequest(Request.Method.GET, Config.URL_BASE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Config.TAG, "NetListenService->onResponse: " + response);
                executeCallTask(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(request);
    }

    private void executeNetListen() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(getIsReceiveServerMsg()) {
                    requestServer();
                }
                executeNetListen();
            }
        }, getIntervalTime());
    }

    private void executeCallTask(String sms) {
        ArrayList<String> numbers = new ItemsDBHelper(getApplicationContext()).getNumbers(sms);
        PhoneCallHelper sCallHelper = new PhoneCallHelper(getApplicationContext());
        sCallHelper.addTaskNumbers(numbers);
    }

    private long getIntervalTime() {
        int sIntervalSecond = PreferenceHelper.getInt(Config.FLAG_SERVER_INTERVAL_SECOND, getApplicationContext());
        return Tools.transformSecondsToMilliseconds(sIntervalSecond);
    }

    private boolean getIsReceiveServerMsg() {
        return PreferenceHelper.getBoolean(Config.FLAG_IS_RECEIVE_SERVER_MSG, getApplicationContext());
    }
}
