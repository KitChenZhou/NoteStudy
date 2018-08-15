package com.ckt.test.abclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wgp on 2017/8/23.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(initView(), null);
        return view;
    }

    /**
     * 一个抽象方法，子类必须实现这个方法然后将布局塞进来
     *
     * @return
     */
    public abstract int initView();
}
