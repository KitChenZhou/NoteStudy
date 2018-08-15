package com.ckt.chen.gesturetest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    GestureOverlayView mGestureView;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureView = (GestureOverlayView) findViewById(R.id.gesture);
        mGestureView.setGestureColor(Color.RED);
        mGestureView.setGestureStrokeWidth(4);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 21);
        }
        mGestureView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, final Gesture gesture) {
                View saveDialog = getLayoutInflater().inflate(R.layout.save, null);
                ImageView imageView = saveDialog.findViewById(R.id.show);
                final EditText gestureName = saveDialog.findViewById(R.id.gesture_name);
                Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                imageView.setImageBitmap(bitmap);
                new AlertDialog.Builder(MainActivity.this)
                        .setView(saveDialog)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i++;
                                GestureLibrary gestureLibrary = GestureLibraries
                                        .fromFile("/mnt/sdcard/mygestures" + i);
                                gestureLibrary.addGesture(gestureName.getText()
                                .toString(), gesture);
                                gestureLibrary.save();
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });
    }
}
