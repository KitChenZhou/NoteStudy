package com.d22395.android.bc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBc = (Button) findViewById(R.id.sendBC);
        sendBc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.ckt.test.calltest.MY_CALL_SHUT_DOWN");
                intent.putExtra("shutdown", true);
                sendBroadcast(intent);
            }
        });

    }
}
