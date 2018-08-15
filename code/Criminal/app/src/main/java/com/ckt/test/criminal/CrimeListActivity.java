package com.ckt.test.criminal;

import android.support.v4.app.Fragment;

/**
 * Created by D22395 on 2017/9/28.
 */

public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
