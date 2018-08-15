package com.ckt.test.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wgp on 2017/8/23.
 */

public class BaseTabActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "BaseTabActivity";

    /**
     * 一个初始化View的方法，用于显示布局的同时替换掉当前的布局
     *
     * @param fragment 采用多态的方式传递一个fragment，要求这个对象必须继承自Fragment
     * @param layoutId 你要替换布局的id
     * @param TAG      标记，表示你所在fragment的标签，是为以后找到这个标签使用
     */
    public void initView(Fragment fragment, int layoutId, String TAG) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment, TAG);
        transaction.commit();
    }
}
