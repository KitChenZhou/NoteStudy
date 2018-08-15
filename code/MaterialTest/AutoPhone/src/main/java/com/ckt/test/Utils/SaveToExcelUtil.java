package com.ckt.test.Utils;

import com.ckt.test.model.CallRecord;

import java.io.File;
import java.util.ArrayList;

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


class SaveToExcelUtil {

    public SaveToExcelUtil(String excelPath) {
        File excelFile = new File(excelPath);
        createExcel(excelFile);
    }

    // 创建excel表.
    static void createExcel(File file) {
        WritableSheet ws;
        try {
            if (!file.exists()) {
                WritableWorkbook wwb = Workbook.createWorkbook(file);
                ws = wwb.createSheet("sheet1", 0);

                // 在指定单元格插入数据
                Label lbl1 = new Label(0, 0, "ID", getHeader());
                Label lbl2 = new Label(1, 0, "开始时间", getHeader());
                Label lbl3 = new Label(2, 0, "结束时间", getHeader());
                Label lbl4 = new Label(3, 0, "持续时间", getHeader());
                Label lbl5 = new Label(4, 0, "网络状态", getHeader());
                Label lbl6 = new Label(5, 0, "是否连接", getHeader());

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

    //将数据存入到Excel表中
    static void writeToExcel(File excelFile, ArrayList<CallRecord> list) {

        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);
            WritableWorkbook wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);

            Label lab1, lab2, lab3, lab4, lab5, lab6;
            WritableCellFormat wcf;
            CallRecord record;
            for (int i = 0; i < list.size(); i++) {
                record = list.get(i);
                if (record.isConnect().equals("pass")) {
                    wcf = getAlignment();
                } else {
                    wcf = getBackground();
                }
                lab1 = new Label(0, i + 1, record.getId(), wcf);
                lab2 = new Label(1, i + 1, record.getStartDate(), wcf);
                lab3 = new Label(2, i + 1, record.getEndDate(), wcf);
                lab4 = new Label(3, i + 1, record.getDuration(), wcf);
                lab5 = new Label(4, i + 1, record.getNetworkType(), wcf);
                lab6 = new Label(5, i + 1, record.isConnect(), wcf);

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
}
