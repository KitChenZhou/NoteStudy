package com.ckt.test.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends Activity {

    CallRecord mCallRecord;
    ArrayList<CallRecord> mList = new ArrayList<>();
    String[] ss = {"qndyd", "madongmei", "mashenmemei", "xialuo", "outman", "shenzhen", "beijing"
            , "clearlove7", "dandy", "qwe", "fsf"};
    Thread t;

    String fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 500; i++) {
            Random r = new Random();
            int t = r.nextInt(ss.length);
            mCallRecord = new CallRecord(ss[t], ss[t], ss[t], ss[t], ss[t], ss[t]);
            mList.add(mCallRecord);
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
//                for (CallRecord c : mList) {
//                    SaveToExcelUtil.writeToExcel(new File(fileName), c);
//                }
            }
        });
    }



    public void keepExcel(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 40);
        } else {
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("提示！");
            dialog.setMessage("正在导出EXCEL文件到" + fileName + "，是否继续？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    File file = new File(fileName);
                    SaveToExcelUtil.createExcel(file);//  创建表格
                    SaveToExcelUtil.writeToExcel(file, mList);
//                    MainActivity.isExportReport = true;
                    Log.i("tag", "succeed");
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    MainActivity.isExportReport = false;
                }
            });
            dialog.show();
        }
    }

}