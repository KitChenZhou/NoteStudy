package com.d22395.android.criminalintent2;

import android.support.v4.app.Fragment;

/**
 * Created by D22395 on 2017/8/15.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
