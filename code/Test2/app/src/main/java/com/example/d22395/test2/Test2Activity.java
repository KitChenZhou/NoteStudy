package com.example.d22395.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Test2Activity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;
    private TextView mResultGrade;
    private Button mPrevButton;
    private Button mNextbutton;
    private Button mCheatButton;

    private EditText mText;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.Question1,true),
            new Question(R.string.Question2,true),
            new Question(R.string.Question3,false),
            new Question(R.string.Question4,false),
            new Question(R.string.Question5,true)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "cheater";
    private static final String TAG = "Test2Activity";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final int CHEAT_COUNT = 3;

    private int temp = 0;
    private int count = 0;
    private ArrayList<Integer> ls = new ArrayList<Integer>(){
        {
            add(0);
            add(1);
            add(2);
            add(3);
            add(4);
        }
    };

    private boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER,false);
        }

        mText = (EditText) findViewById(R.id.test);

        mResultGrade = (TextView) findViewById(R.id.result_grade);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Test2Activity.this, containsEmoji(mText.getText().toString()) + "",Toast.LENGTH_SHORT).show();
                for (int i = 0; i < ls.size(); i++) {
                    if(ls.get(i) == mCurrentIndex) {
                        ls.remove(i);
                    }
                }
                checkAnswer(true);
                updateButtonClick(false);



            }
        });


        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Test2Activity.this,R.string.false_toast,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < ls.size(); i++) {
                    if(ls.get(i) == mCurrentIndex) {
                        ls.remove(i);
                    }
                }
                checkAnswer(false);
                updateButtonClick(false);

            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentIndex == 0) {
                    mCurrentIndex++;
                    Toast.makeText(Test2Activity.this,"这是第一页", Toast.LENGTH_SHORT).show();
                }
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                updateQuestion();
                if (ls.contains(mCurrentIndex)){
                    updateButtonClick(true);
                }
            }
        });

        mNextbutton = (Button) findViewById(R.id.next_button);
        mNextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex >= 4) {
                    mCurrentIndex--;
                    Toast.makeText(Test2Activity.this,"最后一页了", Toast.LENGTH_SHORT).show();
                }
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                Log.d(TAG,"mCurrentIndex = " + mCurrentIndex);
                mIsCheater = false;
                updateQuestion();
                if (ls.contains(mCurrentIndex)){
                    updateButtonClick(true);
                }
            }
        });


        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Test2Activity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswer();
                Intent intent = CheatActivity.newIntent(
                        Test2Activity.this,answerIsTrue);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);// 将答案传送给CheatActivity

                if (CHEAT_COUNT - count < 2) {
                    mCheatButton.setVisibility(View.INVISIBLE);
                }
                count++;
                mCheatButton.setText("Have " + (CHEAT_COUNT - count) + " Cheat!");

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIstrue = mQuestionBank[mCurrentIndex].isAnswer();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIstrue) {
                messageResId = R.string.true_toast;
                temp++;
            } else {
                messageResId = R.string.false_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        if (ls.size() == 0) {
            mResultGrade.setText("you got " + (temp * 100 / mQuestionBank.length)  + "\'");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBoolean(KEY_CHEATER,mIsCheater);
    }

    private void updateButtonClick(Boolean b){
        mTrueButton.setClickable(b);
        mFalseButton.setClickable(b);
    }
}
