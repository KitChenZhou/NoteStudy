package com.youli.feiyu.fynotification.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.youli.feiyu.fynotification.R;
import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.utility.PreferenceHelper;
import com.youli.feiyu.fynotification.utility.UIHelper;

public class SettingActivity extends AppCompatActivity {

    private Spinner mSpinnerServerInterval;
    private Spinner mSpinnerCallInterval;
    private CheckBox mCBServerMsg;
    private CheckBox mCBSmsMsg;
    private boolean isReceiveServerMsg;
    private boolean isReceiveSmsMsg;
    private static final String[] mServerIntervals = {"5秒", "15秒", "30秒", "1分钟", "5分钟", "15分钟"};
    private static final int[] mServerIntervalSeconds = {5, 15, 30, 60, 300, 900};
    private static final String[] mCallIntervals = {"5分钟", "15分钟", "30分钟", "60分钟"};
    private static final int[] mCallIntervalSeconds = {300, 900, 1800, 3600};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        setupUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 对action bar的Up/Home按钮做出反应
            case android.R.id.home:
                UIHelper.returnHome(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        mSpinnerServerInterval = (Spinner) findViewById(R.id.spinner_server_interval);
        ArrayAdapter sAdapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mServerIntervals);
        sAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerServerInterval.setAdapter(sAdapter0);
        mSpinnerServerInterval.setOnItemSelectedListener(new SpinnerSelectedListener());
        mSpinnerServerInterval.setVisibility(View.VISIBLE);
        mSpinnerServerInterval.setSelection(getServerIntervalPosition());

        mSpinnerCallInterval = (Spinner) findViewById(R.id.spinner_call_interval);
        ArrayAdapter sAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCallIntervals);
        sAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCallInterval.setAdapter(sAdapter1);
        mSpinnerCallInterval.setOnItemSelectedListener(new SpinnerSelectedListener());
        mSpinnerCallInterval.setVisibility(View.VISIBLE);
        mSpinnerCallInterval.setSelection(getCallIntervalPosition());

        mCBServerMsg = (CheckBox)findViewById(R.id.cb_server_msg);
        mCBServerMsg.setOnCheckedChangeListener(new CheckBoxCheckedListener());
        mCBServerMsg.setChecked(isReceiveServerMsg());

        mCBSmsMsg = (CheckBox)findViewById(R.id.cb_sms_msg);
        mCBSmsMsg.setOnCheckedChangeListener(new CheckBoxCheckedListener());
        mCBSmsMsg.setChecked(isReceiveSmsMsg());
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getId() == R.id.spinner_server_interval) {
                setServerInterval(position);
            } else if (parent.getId() == R.id.spinner_call_interval){
                setCallInterval(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class CheckBoxCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId() == R.id.cb_server_msg) {
                setIsReceiveServerMsg(isChecked);
            } else if(buttonView.getId() == R.id.cb_sms_msg) {
                setIsReceiveSmsMsg(isChecked);
            }
        }
    }

    private void setServerInterval(int position) {
        PreferenceHelper.putInt(Config.FLAG_SERVER_INTERVAL_POSITION, position, getApplicationContext());
        PreferenceHelper.putInt(Config.FLAG_SERVER_INTERVAL_SECOND, mServerIntervalSeconds[position], getApplicationContext());
    }

    private int getServerIntervalPosition() {
        return PreferenceHelper.getInt(Config.FLAG_SERVER_INTERVAL_POSITION, getApplicationContext());
    }

    private void setCallInterval(int position) {
        PreferenceHelper.putInt(Config.FLAG_CALL_INTERVAL_POSITION, position, getApplicationContext());
        PreferenceHelper.putInt(Config.FLAG_CALL_INTERVAL_SECOND, mCallIntervalSeconds[position], getApplicationContext());
    }

    private int getCallIntervalPosition() {
        return PreferenceHelper.getInt(Config.FLAG_CALL_INTERVAL_POSITION, getApplicationContext());
    }

    public boolean isReceiveServerMsg() {
        return PreferenceHelper.getBoolean(Config.FLAG_IS_RECEIVE_SERVER_MSG, getApplicationContext());
    }

    public void setIsReceiveServerMsg(boolean isReceiveServerMsg) {
        PreferenceHelper.putBoolean(Config.FLAG_IS_RECEIVE_SERVER_MSG, isReceiveServerMsg, getApplicationContext());
        this.isReceiveServerMsg = isReceiveServerMsg;
    }

    public boolean isReceiveSmsMsg() {
        return PreferenceHelper.getBoolean(Config.FLAG_IS_RECEIVE_SMS_MSG, getApplicationContext());
    }

    public void setIsReceiveSmsMsg(boolean isReceiveSmsMsg) {
        PreferenceHelper.putBoolean(Config.FLAG_IS_RECEIVE_SMS_MSG, isReceiveSmsMsg, getApplicationContext());
        this.isReceiveSmsMsg = isReceiveSmsMsg;
    }
}
