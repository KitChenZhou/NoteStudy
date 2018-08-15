package com.ckt.test.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ckt.test.R;
import com.ckt.test.fragments.APModeFragment;
import com.ckt.test.utils.TabFragmentFactory;

/**
 * Created by wgp on 2017/8/23.
 * 控制主界面的Activity,它托管了四个Fragment
 */


public class MyTabMain extends BaseTabActivity {
    private RadioGroup radioGroup;
    private static final String TAG = "MyTabMain";
    private RadioButton air, data;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        radioGroup = (RadioGroup) findViewById(R.id.tab_main);
        air = (RadioButton) findViewById(R.id.air);
        data = (RadioButton) findViewById(R.id.data);
        initView(APModeFragment.newInstance(), R.id.content, TAG);//让默认显示自己定义好的布局
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (air.getId() == checkedId) {
                    index = 1;
                } else if (data.getId() == checkedId) {
                    index = 2;
                }
                Fragment fragment = TabFragmentFactory.getInstanceByIndex(index);
                initView(fragment, R.id.content, TAG);
            }
        });
    }
}

