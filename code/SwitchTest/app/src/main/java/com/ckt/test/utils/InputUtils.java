package com.ckt.test.utils;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by wgp on 2017/8/22.
 * 判断用户输入是否合法的工具类
 */

public class InputUtils {
    /**
     * 当editText失去焦点时候的监听事件
     *
     * @param editText EditText组件
     * @param context  传递的上下文
     * @return
     */
    public static boolean afterInput(final Context context, EditText editText) {
        final Editable result = editText.getText();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                } else {
                    if (!isInteger(result)) {
                        Toast.makeText(context, "请输入合法的数字", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return false;
    }

    /**
     * 判断用户的输入是否为整形
     *
     * @param o 输入的值
     * @return true为数字，false非数字
     */
    public static boolean isInteger(Object o) {
        if (!(o instanceof Integer)) {
            return false;
        } else
            return true;
    }

    /**
     * 判断用户的输入不能为空
     *
     * @param interal 测试的时间间隔
     * @param count   测试的总次数
     * @return
     */
    public static boolean handleParams(String interal, String count) {
        if (interal.equals("") || count.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户网页测试时输入参数
     *
     * @param interal 时间间隔
     * @param delay   延迟时间
     * @param con     持续时间
     * @param count   测试的次数
     * @return
     */
    public static boolean handleParams(String interal, String delay, String con, String count, String on) {
        if (interal.equals("") || delay.equals("") || con.equals("") || count.equals("") ||
                on.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 将输入的字符串转换为int类型
     *
     * @param param
     * @return
     */
    public static int stringToInt(String param) {
        return Integer.parseInt(param);
    }
}
