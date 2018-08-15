package com.youli.feiyu.fynotification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youli.feiyu.fynotification.adapter.DataShowerAdapter;
import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.service.NetListenService;
import com.youli.feiyu.fynotification.utility.UIHelper;

public class MainActivity extends AppCompatActivity {

    private ListView mLVDataShower;
    private DataShowerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("飞鱼");

        mAdapter = new DataShowerAdapter(getApplicationContext());
        mLVDataShower = (ListView)findViewById(R.id.lv_data_shower);
        mLVDataShower.setAdapter(mAdapter);
        mLVDataShower.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean itemBean = (ItemBean) mAdapter.getItem(position);
                itemAlert(itemBean);
            }
        });

        startNetListenService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            UIHelper.showSettingActivity(this);
            return true;
        }
        if (id == R.id.action_add) {
            UIHelper.showAddActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void itemAlert(final ItemBean itemBean) {
        final CharSequence[] items = {"修改", "删除"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    UIHelper.showEditActivity(MainActivity.this, itemBean);
                } else {
                    mAdapter.deleteItem(itemBean);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startNetListenService() {
        Intent intent = new Intent(this, NetListenService.class);
        startService(intent);
    }
}
