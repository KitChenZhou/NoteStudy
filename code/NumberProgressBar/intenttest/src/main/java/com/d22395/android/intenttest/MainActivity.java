package com.d22395.android.intenttest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;

import java.io.File;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //打开指定网页
    public void browsePageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com/"));
        startActivity(intent);
    }

    //打开拨号面板
    public void openDialPageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    //直接拨打指定号码
    public void dialPhoneClick(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    // 打开发短信的界面:action+type
    public void openMsgPageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", "具体短信内容"); //"sms_body"为固定内容
        startActivity(intent);
    }

    // 打开发短信的界面(指定电话号码):action+data
    public void sendMsgClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:18780260012"));
        intent.putExtra("sms_body", "具体短信内容"); //"sms_body"为固定内容
        startActivity(intent);
    }

    // 播放指定路径音乐
    public void playMusicClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file:///storage/sdcard0/平凡之路.mp3");  //路径也可以写成："/storage/sdcard0/平凡之路.mp3"
        intent.setDataAndType(uri, "audio/mp3"); //方法：Intent android.content.Intent.setDataAndType(Uri data, String type)
        startActivity(intent);
    }

    //卸载某个应用程序，根据包名来识别
    public void uninstallClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri data = Uri.parse("package:com.example.smyh006intent01");
        intent.setData(data);
        startActivity(intent);
    }

    //安装某个应用程序，根据apk的文件名来识别
    public void installClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(new File("/storage/sdcard0/AndroidTest/smyh006_Intent01.apk"));    //路径不能写成："file:///storage/sdcard0/···"
        intent.setDataAndType(data, "application/vnd.android.package-archive");  //Type的字符串为固定内容
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
