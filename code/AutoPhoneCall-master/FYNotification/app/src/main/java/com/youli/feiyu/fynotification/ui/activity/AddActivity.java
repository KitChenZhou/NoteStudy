package com.youli.feiyu.fynotification.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youli.feiyu.fynotification.R;
import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.config.Config;
import com.youli.feiyu.fynotification.utility.ItemsDBHelper;
import com.youli.feiyu.fynotification.utility.UIHelper;

public class AddActivity extends AppCompatActivity {

    private ItemsDBHelper mItemsDBHelper;
    private EditText etSmsContent;
    private EditText etCallNumber;
    private Button btnAdd;
    private boolean isEditStatus;
    private ItemBean mOldItemBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("添加");

        mItemsDBHelper = new ItemsDBHelper(getApplicationContext());

        etSmsContent = (EditText)findViewById(R.id.et_sms_content);
        etCallNumber = (EditText)findViewById(R.id.et_call_number);

        btnAdd = (Button)findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(btnClickListener);

        showEditStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                UIHelper.returnHome(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditStatus() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            return;
        }
        if (bundle.getBoolean(Config.FLAG_IS_EDIT)) {
            String sOldSms = bundle.getString(Config.FLAG_ITEM_SMS);
            String sOldNumber = bundle.getString(Config.FLAG_ITEM_NUMBER);
            mOldItemBean = new ItemBean();
            mOldItemBean.setSms(sOldSms);
            mOldItemBean.setNumber(sOldNumber);
            etSmsContent.setText(sOldSms);
            etCallNumber.setText(sOldNumber);
            this.isEditStatus = true;
            getSupportActionBar().setTitle("修改");
            btnAdd.setText("修改");
        }
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btn_add) {
                String sSMS = etSmsContent.getText().toString();
                String sNumber = etCallNumber.getText().toString();
                ItemBean sItemBean = new ItemBean();
                sItemBean.setSms(sSMS);
                sItemBean.setNumber(sNumber);
                if(isEmpety(sSMS, sNumber)) {
                    Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(isEditStatus) {
                    mItemsDBHelper.updateItem(mOldItemBean, sItemBean);
                    Toast.makeText(getApplicationContext(), "已修改", Toast.LENGTH_LONG).show();
                } else {
                    mItemsDBHelper.saveItem(sItemBean);
                    Toast.makeText(getApplicationContext(), "已添加", Toast.LENGTH_LONG).show();
                }

                UIHelper.returnHome(AddActivity.this);
            }
        }
    };

    private Boolean isEmpety(String sms, String number) {
        return sms.isEmpty() || number.isEmpty();
    }
}
