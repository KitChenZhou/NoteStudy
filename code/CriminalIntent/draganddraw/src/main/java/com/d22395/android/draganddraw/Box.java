package com.d22395.android.draganddraw;

import android.graphics.PointF;

/**
 * Created by D22395 on 2017/8/14.
 */

public class Box {

    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return  mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

}
