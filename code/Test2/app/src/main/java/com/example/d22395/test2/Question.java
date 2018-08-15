package com.example.d22395.test2;

/**
 * Created by D22395 on 2017/7/24.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswer;

    public Question(int textResId,boolean answer) {
        mTextResId = textResId;
        mAnswer = answer;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswer() {
        return mAnswer;
    }

    public void setAnswer(boolean answer) {
        mAnswer = answer;
    }
}
