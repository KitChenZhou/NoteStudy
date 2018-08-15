package com.ckt.chen.crazy13;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.show);
        new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("10.240.2.73", 3000);
                    socket.setSoTimeout(10000);
                    Scanner scan = new Scanner(socket.getInputStream());
                    final String str = scan.nextLine();

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()
                    ));
                    final String line = br.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("来自服务器的数据：" + line);
                            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        }
                    });
                    br.close();
                    socket.close();
                } catch (SocketTimeoutException ex) {
                    Toast.makeText(MainActivity.this, "已超时！", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
