package com.ckt.chen.crazy13;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by D22395 on 2017/12/28.
 */

public class SimpleServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3000);
        while (true) {
            System.out.println("running....");
            Socket s = ss.accept();
            OutputStream os = s.getOutputStream();
            os.write("Hello, you received the server's New Year's greeting!\n".getBytes("utf-8"));
            os.close();
            s.close();
        }
    }

}
