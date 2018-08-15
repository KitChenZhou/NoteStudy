package com.ckt.test.utils;

import android.support.v4.app.Fragment;

import com.ckt.test.fragments.APModeFragment;
import com.ckt.test.fragments.DataModeFragment;

/**
 * Created by wgp on 2017/8/23.
 * 管理TabFragment的工具类,这里用一个Activity来托管四个Fragment
 */

public class TabFragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = APModeFragment.newInstance();
                break;
            case 2:
                fragment = DataModeFragment.newInstance();
                break;
            default:
                fragment = com.ckt.test.fragments.APModeFragment.newInstance();
        }
        return fragment;
    }
}