package com.d22395.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by D22395 on 2017/8/14.
 */

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private View mMoonView;
    private View mSeaView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private static boolean flag = true;
    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = v;
        mSunView = v.findViewById(R.id.sun);
        mSkyView = v.findViewById(R.id.sky);
        mMoonView = v.findViewById(R.id.moon);
        mSeaView = v.findViewById(R.id.sea);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);



        mSceneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == true) {
                        flag = false;
                        startAnimation();
                    } else {
                        flag = true;
                        reStartAnimation();
                    }
                }
            });
        return v;
    }

    private void startAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        float moonYStart = mMoonView.getTop();
        float moonYEnd = mSeaView.getPaddingTop()- mMoonView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart,sunYEnd)
                .setDuration(3000);

        ObjectAnimator heightAnimator1 = ObjectAnimator
                .ofFloat(mMoonView, "y", moonYStart, moonYEnd)
                .setDuration(3000);
        heightAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator)
                .with(heightAnimator1)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();

    }
    private void reStartAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        float moonYStart = mMoonView.getTop();
        float moonYEnd = mSeaView.getPaddingTop()- mMoonView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYEnd, sunYStart)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator heightAnimator1 = ObjectAnimator
                .ofFloat(mMoonView, "y", moonYEnd, moonYStart)
                .setDuration(3000);
        heightAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator1.start();

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator)
                .with(nightSkyAnimator)
                .before(sunsetSkyAnimator);
        animatorSet.start();

//        heightAnimator.start();
//        sunsetSkyAnimator.start();
    }
}
