package com.d22395.android.callshutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ShutDownReceiver shutDownReceiver;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter intent = new IntentFilter();
                intent.addAction("com.ckt.test.calltest.MY_CALL_SHUT_DOWN");
                shutDownReceiver = new ShutDownReceiver();
                registerReceiver(shutDownReceiver, intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(shutDownReceiver);
    }

    public class ShutDownReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//        boolean b = intent.getBooleanExtra("shutdown")
            Toast.makeText(MainActivity.this, "已挂断电话", Toast.LENGTH_SHORT).show();
            Log.i("callShutDown", "已挂断电话");
        }
    }

}
