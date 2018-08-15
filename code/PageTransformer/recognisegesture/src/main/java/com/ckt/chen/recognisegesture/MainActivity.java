package com.ckt.chen.recognisegesture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GestureOverlayView mGestureView;
    GestureLibrary mGestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureLibrary = GestureLibraries.fromFile("/mnt/sdcard/mygestures");
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 25);
        }
        if (mGestureLibrary.load()) {
            Toast.makeText(MainActivity.this, "手势文件装载成功！" , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "手势文件装载失败！" , Toast.LENGTH_SHORT).show();
        }
        mGestureView = (GestureOverlayView) findViewById(R.id.gesture);
        mGestureView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                ArrayList<Prediction> predictions = mGestureLibrary.recognize(gesture);
                ArrayList<String> result = new ArrayList<String>();
                for (Prediction pred : predictions) {
                    if (pred.score > 2.0) {
                        result.add("与手势【" + pred.name + "】相似度为" + pred.score);
                    }
                }
                if (result.size() > 0) {
                    ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                            MainActivity.this, android.R.layout.simple_dropdown_item_1line,
                            result.toArray());
                    new AlertDialog.Builder(MainActivity.this)
                            .setAdapter(adapter, null)
                            .setPositiveButton("确定", null).show();
                } else {
                    Toast.makeText(MainActivity.this, "无法找到能够匹配的手势！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
