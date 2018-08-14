package com.d22395.android.uibestpractice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by D22395 on 2017/12/29.
 */

public class ClientThread implements Runnable {

    private Socket s;
    private Handler handler;
    public Handler revHandler;
    BufferedReader br = null;
    OutputStream os = null;

    public ClientThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            s = new Socket("10.240.2.99", 30002);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();
            new Thread() {
                @Override
                public void run() {
                    String content = null;
                    try {
                        while ((content = br.readLine()) != null) {
                            Message message = new Message();
                            message.what = 0x123;
//                            message.arg1 = MainActivity.randomNum;
                            message.obj = content;
                            Log.i("cherry", "ClientContent : " + content);
                            handler.sendMessage(message);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            revHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x345) {
                        try {
                            os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
                        } catch (IOException e ) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }  catch (SocketTimeoutException e) {
            System.out.println("网络连接超时");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
