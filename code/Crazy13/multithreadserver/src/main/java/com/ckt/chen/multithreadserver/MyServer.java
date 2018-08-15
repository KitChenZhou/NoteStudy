package com.ckt.chen.multithreadserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by D22395 on 2017/12/29.
 */

public class MyServer {

    public static ArrayList<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(30001);
        while (true) {
            System.out.println("running....");
            Socket s = ss.accept();
            socketList.add(s);
            new Thread(new ServerThread(s)).start();
        }

    }

}
