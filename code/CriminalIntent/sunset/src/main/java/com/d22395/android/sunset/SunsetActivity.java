package com.d22395.android.sunset;

import android.support.v4.app.Fragment;

public class SunsetActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SunsetFragment();
    }
}
