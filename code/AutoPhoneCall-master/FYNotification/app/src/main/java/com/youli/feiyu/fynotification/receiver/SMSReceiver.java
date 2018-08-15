package com.youli.feiyu.fynotification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.util.Log;

import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.utility.ItemsDBHelper;
import com.youli.feiyu.fynotification.utility.PhoneCallHelper;
import com.youli.feiyu.fynotification.utility.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by Sean on 16/2/24.
 */
public class SMSReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(!getIsReceiveSmsMsg(context)) {
            return;
        }
        // 先判断广播消息
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // 取pdus内容,转换为Object[]
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                // 解析短信
                for (int i = 0; i < messages.length; i++) {
                    byte[] pdu = (byte[]) pdus[i];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API level 23 以后应该用
                        messages[i] = SmsMessage.createFromPdu(pdu, format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu(pdu);
                    }
                }
                // 解析完内容后分析具体参数
                for (SmsMessage msg : messages) {
                    // 获取短信内容
                    String sms = msg.getMessageBody();
                    // 获取短信号码
                    String number = msg.getOriginatingAddress();
                    ArrayList<String> numbers = new ItemsDBHelper(context).getNumbers(sms);
                    PhoneCallHelper sCallHelper = new PhoneCallHelper(context);
                    sCallHelper.addTaskNumbers(numbers);
                }
            }
        }
    }

    private boolean getIsReceiveSmsMsg(Context context) {
        return PreferenceHelper.getBoolean(Config.FLAG_IS_RECEIVE_SMS_MSG, context);
    }
}
