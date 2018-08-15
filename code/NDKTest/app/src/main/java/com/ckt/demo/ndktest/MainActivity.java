package com.ckt.demo.ndktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = findViewById(R.id.textview);
        Button button = findViewById(R.id.button);

        String s = "  ";
        Log.d("ccc", "isEmpty1:" + TextUtils.isEmpty(s));
        Log.d("ccc", "isEmpty2:" + s.trim().isEmpty());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.setText(JNIUtils.sayHelloFromJNI());
            }
        });
    }
}
