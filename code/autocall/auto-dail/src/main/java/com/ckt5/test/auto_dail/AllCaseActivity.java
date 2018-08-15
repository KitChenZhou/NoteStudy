package com.ckt5.test.auto_dail;

import android.support.v4.app.Fragment;

public class AllCaseActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new AllCaseFragment();
    }
}
