package com.ckt.test.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ckt.test.MainActivity;
import com.ckt.test.model.CallRecord;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.content.ContentValues.TAG;
import static com.ckt.test.MainActivity.REQUEST_CODE_WRITE_FILE;

public class ExcelUtil {

    /**
     * 创建excel表.
     *
     * @param file 文件
     */
    private static void createExcel(File file) {
        WritableSheet ws;
        try {
            if (!file.exists()) {
                WritableWorkbook wwb = Workbook.createWorkbook(file);

                ws = wwb.createSheet("sheet1", 0);

                // 在指定单元格插入数据
                Label lbl1 = new Label(0, 0, "id", getHeader());
                Label lbl2 = new Label(1, 0, "startTime", getHeader());
                Label lbl3 = new Label(2, 0, "endTime", getHeader());
                Label lbl4 = new Label(3, 0, "duration", getHeader());
                Label lbl5 = new Label(4, 0, "networkType", getHeader());
                Label lbl6 = new Label(5, 0, "isSuccessful", getHeader());

                ws.addCell(lbl1);
                ws.addCell(lbl2);
                ws.addCell(lbl3);
                ws.addCell(lbl4);
                ws.addCell(lbl5);
                ws.addCell(lbl6);

                // 从内存中写入文件中
                wwb.write();
                wwb.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据写入Excel表
     *
     * @param file 文件
     * @param list 数据list
     */
    private static void writeToExcel(File file, ArrayList<CallRecord> list) {

        try {
            Workbook oldWwb = Workbook.getWorkbook(file);
            WritableWorkbook wwb = Workbook.createWorkbook(file, oldWwb);
            WritableSheet ws = wwb.getSheet(0);
            Label lab1, lab2, lab3, lab4, lab5, lab6;

            for (int i = 0; i < list.size(); i++) {
                CallRecord record = list.get(i);
                if (record.isSuccessful().equals("pass")) {
                    lab1 = new Label(0, i + 1, record.getId() + "", getAlignment());
                    lab2 = new Label(1, i + 1, record.getStartTime(), getAlignment());
                    lab3 = new Label(2, i + 1, record.getEndTime(), getAlignment());
                    lab4 = new Label(3, i + 1, record.getDuration() + "", getAlignment());
                    lab5 = new Label(4, i + 1, record.getNetworkType(), getAlignment());
                    lab6 = new Label(5, i + 1, record.isSuccessful() + "", getAlignment());
                } else {
                    lab1 = new Label(0, i + 1, record.getId() + "", getBackground());
                    lab2 = new Label(1, i + 1, record.getStartTime(), getBackground());
                    lab3 = new Label(2, i + 1, record.getEndTime(), getBackground());
                    lab4 = new Label(3, i + 1, record.getDuration() + "", getBackground());
                    lab5 = new Label(4, i + 1, record.getNetworkType(), getBackground());
                    lab6 = new Label(5, i + 1, record.isSuccessful() + "", getBackground());
                }
                ws.addCell(lab1);
                ws.addCell(lab2);
                ws.addCell(lab3);
                ws.addCell(lab4);
                ws.addCell(lab5);
                ws.addCell(lab6);
            }

            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 表头样式
     *
     * @return 样式
     */
    private static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            format.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);// 黑色边框
            format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 加背景颜色样式
     *
     * @return 样式
     */
    private static WritableCellFormat getBackground() {
        WritableCellFormat format = new WritableCellFormat();
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            format.setBackground(Colour.RED);// 红色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 字体样式
     *
     * @return 样式
     */
    private static WritableCellFormat getAlignment() {
        WritableCellFormat format = new WritableCellFormat();
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 导出Excel文件
     */
    public static void exportReport(final Context context, final ArrayList<CallRecord> list) {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
        final File file = new File(fileName);

        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("提示！");
        mDialog.setMessage("正在导出EXCEL文件：" + "\n"
                + fileName + "\n" +
                "是否继续？");
        mDialog.setCancelable(true);
        Log.i(TAG, list.toString());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_FILE);
            return;
        }
        mDialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ExcelUtil.createExcel(file);//  创建表格
                ExcelUtil.writeToExcel(file, list);//写数据
                PhoneUtils.showToast(context, "导出成功!");
                MainActivity.isExportReport = true;
            }
        });
        mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.isExportReport = false;
            }
        });
        mDialog.show();
    }
}
