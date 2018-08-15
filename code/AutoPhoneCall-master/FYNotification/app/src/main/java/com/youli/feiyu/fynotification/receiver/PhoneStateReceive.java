package com.youli.feiyu.fynotification.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.utility.PhoneCallHelper;
import com.youli.feiyu.fynotification.utility.PreferenceHelper;

/**
 * Created by Sean on 16/2/24.
 */
public class PhoneStateReceive extends BroadcastReceiver {

    private int mCurrentState = TelephonyManager.CALL_STATE_IDLE ;
    private int mOldState = TelephonyManager.CALL_STATE_IDLE ;

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            mOldState = PreferenceHelper.getInt(Config.FLAG_CALL_STATE, mContext);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    mCurrentState = TelephonyManager.CALL_STATE_IDLE;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mCurrentState = TelephonyManager.CALL_STATE_OFFHOOK;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    mCurrentState = TelephonyManager.CALL_STATE_RINGING;
                    break;
            }

            if(mOldState == TelephonyManager.CALL_STATE_IDLE && mCurrentState == TelephonyManager.CALL_STATE_OFFHOOK ) {
                Log.i(Config.TAG, "onCallStateChanged: 接通");
                PreferenceHelper.putInt(Config.FLAG_CALL_STATE, mCurrentState, mContext);
            } else if (mOldState == TelephonyManager.CALL_STATE_OFFHOOK && mCurrentState == TelephonyManager.CALL_STATE_IDLE) {
                Log.i(Config.TAG, "onCallStateChanged: 挂断");
                PreferenceHelper.putInt(Config.FLAG_CALL_STATE, mCurrentState, mContext);
                PhoneCallHelper sHelper = new PhoneCallHelper(mContext);
                sHelper.executeTask();
            }
        }
    }
}
