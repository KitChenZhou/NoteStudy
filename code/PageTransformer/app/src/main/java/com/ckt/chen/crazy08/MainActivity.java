package com.ckt.chen.crazy08;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    GestureDetector mDetector;
    ViewFlipper mFlipper;
    Animation[] mAnimations = new Animation[4];
    final int FLIP_DISTANCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetector(this, this);
        mFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mFlipper.addView(addImageview(R.drawable.er));
        mFlipper.addView(addImageview(R.drawable.ca));
        mFlipper.addView(addImageview(R.drawable.da));
        mFlipper.addView(addImageview(R.drawable.yonna));
        mFlipper.addView(addImageview(R.drawable.yun));
        mFlipper.addView(addImageview(R.drawable.fa));

        mAnimations[0] = AnimationUtils.loadAnimation(this, R.anim.left_in);
        mAnimations[1] = AnimationUtils.loadAnimation(this, R.anim.left_out);
        mAnimations[2] = AnimationUtils.loadAnimation(this, R.anim.right_in);
        mAnimations[3] = AnimationUtils.loadAnimation(this, R.anim.right_out);
    }

    private View addImageview(int resId) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        return imageView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
//        Toast.makeText(MainActivity.this, "onDown", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
//        Toast.makeText(MainActivity.this, "onShowPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
//        Toast.makeText(MainActivity.this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//        Toast.makeText(MainActivity.this, "onScroll", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
//        Toast.makeText(MainActivity.this, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float x, float y) {
//        Toast.makeText(MainActivity.this, "onFling", Toast.LENGTH_SHORT).show();
//        x = x > 4000 ? 4000 : x;
//        x = x < -4000 ? -4000 : x;
//        currentScale += currentScale * x / 4000.0f;
//        currentScale = currentScale > 0.01 ? currentScale : 0.01f;
//        mMatrix.reset();
//        mMatrix.setScale(currentScale, currentScale, 160, 200);
//        BitmapDrawable tmp = (BitmapDrawable) mImageView.getDrawable();
//        if (!tmp.getBitmap().isRecycled()) {
//            tmp.getBitmap().recycle();
//        }
//        Bitmap bitmap2 = Bitmap.createBitmap(mBitmap, 0, 0, width, height, mMatrix, true);
//        mImageView.setImageBitmap(bitmap2);
//        return true;
        if (motionEvent.getX() - motionEvent1.getX() > FLIP_DISTANCE) {
            mFlipper.setInAnimation(mAnimations[0]);
            mFlipper.setOutAnimation(mAnimations[1]);
            mFlipper.showPrevious();
            return true;
        } else if (motionEvent1.getX() - motionEvent.getX() > FLIP_DISTANCE) {
            mFlipper.setInAnimation(mAnimations[2]);
            mFlipper.setOutAnimation(mAnimations[3]);
            mFlipper.showNext();
            return true;
        }
        return false;
    }
}
