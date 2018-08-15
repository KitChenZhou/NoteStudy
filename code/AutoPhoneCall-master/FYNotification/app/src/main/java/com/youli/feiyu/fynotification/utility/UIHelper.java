package com.youli.feiyu.fynotification.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.youli.feiyu.fynotification.MainActivity;
import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.ui.activity.AddActivity;
import com.youli.feiyu.fynotification.ui.activity.SettingActivity;

/**
 * Created by Sean on 16/2/23.
 */
public class UIHelper {

    public static void showAddActivity(Context context) {
        Intent intent = new Intent(context, AddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.FLAG_IS_EDIT, false);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showEditActivity(Context context, ItemBean itemBean) {
        Intent intent = new Intent(context, AddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.FLAG_IS_EDIT, true);
        bundle.putString(Config.FLAG_ITEM_SMS, itemBean.getSms());
        bundle.putString(Config.FLAG_ITEM_NUMBER, itemBean.getNumber());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    public static void returnHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
