package com.d22395.android.numberprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnProgressBarListener {
    private Timer timer;

    private NumberProgressBar bnp;
    private NumberProgressBar[] mBars = new NumberProgressBar[8];
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBars[0] = (NumberProgressBar) findViewById(R.id.numberbar1);
        mBars[1] = (NumberProgressBar) findViewById(R.id.numberbar2);
        mBars[2] = (NumberProgressBar) findViewById(R.id.numberbar3);
        mBars[3] = (NumberProgressBar) findViewById(R.id.numberbar4);
        mBars[4] = (NumberProgressBar) findViewById(R.id.numberbar5);
        mBars[5] = (NumberProgressBar) findViewById(R.id.numberbar6);
        mBars[6] = (NumberProgressBar) findViewById(R.id.numberbar7);
        mBars[7] = (NumberProgressBar) findViewById(R.id.numberbar8);
        mBars[count].setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBars[count].incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (count < 7) {
                count++;
            }else {
                count=0;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {
            Toast.makeText(getApplicationContext(), getString(R.string.finish), Toast.LENGTH_SHORT).show();
            mBars[count].setProgress(0);
        }
    }
}

