package com.ckt.test.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ckt.test.R;
import com.ckt.test.abclass.BaseFragment;
import com.ckt.test.dialogfragment.WarningDialogFragment;
import com.ckt.test.utils.SystemUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.ckt.test.utils.InputUtils.handleParams;

/**
 * Created by wgp on 2017/8/16.
 * 控制飞行模式开关
 */

public class APModeFragment extends BaseFragment {
    private static final String TAG = "APModeFragment";
    private static final int MSG = 110;
    private TextView title, tv_on, tv_off, tv_status, tv_conclusion;
    private EditText interval, count;
    private ImageButton start;
    //判断当前的飞行模式是否开启
    private boolean airPlaneIsOn;
    //输入的参数
    private int _interval, _count;
    //运行最大的次数
    private int MAX_PROGRESS;
    //记录运行多少次
    private int runningCount;
    //记录测试运行的结果
    private int on, off;
    //定义一个进度条
    private ProgressDialog mProgressDialog;
    //定义执行任务的对象
    private Timer timer;

    public static APModeFragment newInstance() {
        return new APModeFragment();
    }

    /**
     * 控制开始显示按钮的handler
     */
    private Handler myHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    start.setEnabled(false);
                    start.setImageResource(R.drawable.wait);
                    break;
                case 1:
                    start.setEnabled(true);
                    start.setImageResource(R.drawable.start);
                    if (isAirPlaneModeOn()) {
                        tv_status.setText(R.string.airplaneison);
                    } else {
                        tv_status.setText(R.string.airplaneisoff);
                    }
                    String onresult = "成功开启：<font color='green'>%d</font> 次";
                    onresult = String.format(onresult, on);
                    tv_on.setText(Html.fromHtml(onresult));
                    String offresult = "成功关闭：<font color='green'>%d</font> 次";
                    offresult = String.format(offresult, off);
                    tv_off.setText(Html.fromHtml(offresult));
//                    int _conclusion = (on + off) / MAX_PROGRESS * 100;
                    int _conclusion = ((on + off) / (on + off)) * 100;
                    String conresult = "测试成功率：<font color='red'>%d</font>";
                    conresult = String.format(conresult, (int) _conclusion);
                    SystemUtils.hideSoftKeyBorad(getActivity());
                    tv_conclusion.setText(Html.fromHtml(conresult) + "%");
                    on = 0;
                    off = 0;
                    runningCount = 0;
                    break;
            }
        }
    };
    private Handler DialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG:
                    if (runningCount >= MAX_PROGRESS) {
                        //重新设置
                        mProgressDialog.dismiss();//销毁对话框
                    } else {
                        mProgressDialog.incrementProgressBy(runningCount);
                        //延迟发送消息
                        DialogHandler.sendEmptyMessageDelayed(MSG, 100);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置打开或关闭飞行模式
     *
     * @param context  上下文
     * @param enabling 打开或关闭,true为打开，false为关闭
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void openAirplaneModeOn(Context context, boolean enabling) {
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,
                enabling ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra("state", enabling);
        context.sendBroadcast(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initView(), container, false);
        title = (TextView) view.findViewById(R.id.title);
        tv_on = (TextView) view.findViewById(R.id.tv_on);
        tv_off = (TextView) view.findViewById(R.id.tv_off);
        tv_status = (TextView) view.findViewById(R.id.last_status);
        tv_conclusion = (TextView) view.findViewById(R.id.tv_conclusion);
        String _title = "飞行模式开关测试";
        _title = String.format(_title);
        title.setText(Html.fromHtml(_title));
        interval = (EditText) view.findViewById(R.id.et_interval);
        count = (EditText) view.findViewById(R.id.et_count);
        start = (ImageButton) view.findViewById(R.id.ib_start);
        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                //让软键盘消失，提高用户体验
                SystemUtils.hideSoftKeyBorad(getActivity());
                String mInterval = interval.getText().toString().trim();
                String mCount = count.getText().toString().trim();
                //判断用户输入是否合法
                if (!handleParams(mInterval, mCount)) {
                    Log.d(TAG, "onClick: " + "runnnnnnnnnnn");
                    FragmentManager manager = getFragmentManager();
                    WarningDialogFragment.showDialog(manager, getActivity());
                } else {
                    _interval = Integer.parseInt(interval.getText().toString().trim());
                    _count = Integer.parseInt(count.getText().toString().trim());
                    Log.d(TAG, "onClick: " + _interval + "-----" + _count);
                    myHandler.sendEmptyMessage(0);
                    showDialogProgress(getActivity(), _count);
                    MyTimeTask(_interval, _count);
                }
            }
        });
        return view;
    }

    /**
     * 返回布局的控件
     *
     * @return
     */
    @Override
    public int initView() {
        return R.layout.test_fragment;
    }

    /**
     * 显示对话框进度条
     *
     * @param context 上下文
     * @param max     设置最大值
     */
    private void showDialogProgress(Context context, int max) {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(R.string.airplanedialogtitle);
        mProgressDialog.setIcon(R.drawable.wait);
        mProgressDialog.setMessage("Waitting .......");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）
        MAX_PROGRESS = max;
        //进度最大值
        mProgressDialog.setMax(max);
        mProgressDialog.setButton("Stop", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                myHandler.sendEmptyMessage(1);
                mProgressDialog.dismiss();
                timer.cancel();//取消时间任务
            }
        });
        //显示
        mProgressDialog.show();
        //必须设置点击空白处不消失
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 定义的一个执行任务类
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void MyTimeTask(final int inter, final int count) {
        airPlaneIsOn = isAirPlaneModeOn();
        Log.d(TAG, "MyTimeTask: " + airPlaneIsOn + "haoxiaoqian");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!airPlaneIsOn) {// 判断是否打开
                    //打开飞行模式
                    openAirplaneModeOn(getActivity(), true);
                    on++;
                    Log.d(TAG, "run: 开启飞行模式" + on);
                    airPlaneIsOn = (!airPlaneIsOn);
                } else {
                    openAirplaneModeOn(getActivity(), false);
                    off++;
                    Log.d(TAG, "run: 关闭飞行模式" + off);
                    airPlaneIsOn = (!airPlaneIsOn);
                }
                runningCount++;
                mProgressDialog.setProgress(runningCount);
                Log.d(TAG, "run: " + runningCount);
                if (runningCount >= count) {
                    myHandler.sendEmptyMessage(1);
                    mProgressDialog.dismiss();
                    this.cancel();//取消时间任务
                    Log.d(TAG, "MyTimeTask: ..." + "finsssssss");
                }
            }
        }, 0, inter * 1000);//转换成秒
    }

    /**
     * 判断是否打开飞行模式
     * true 表示打开飞行模式
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isAirPlaneModeOn() {
        int mode = 1;
        try {
            mode = Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode == 1;//为1的时候打开飞行模式
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

}
