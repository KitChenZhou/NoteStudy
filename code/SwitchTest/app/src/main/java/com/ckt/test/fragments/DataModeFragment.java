package com.ckt.test.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ckt.test.R;
import com.ckt.test.abclass.BaseFragment;
import com.ckt.test.dialogfragment.WarningDialogFragment;
import com.ckt.test.utils.DataSwitch;
import com.ckt.test.utils.SystemUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.ckt.test.utils.InputUtils.handleParams;

/**
 * Created by wgp on 2017/8/25.
 * 控制数据业务开关
 */
public class DataModeFragment extends BaseFragment {
    private static final String TAG = "DataModeFragment";
    private static final int MSG = 120;
    private EditText interval, count;
    //要显示的标题
    private TextView title, tv_on, tv_off, tv_status, tv_conclusion;
    private ImageButton start;
    //判断当前的数据业务是否开启
    private boolean dataIsOn;
    //输入的参数
    private int _interval, _count;
    //运行最大的次数
    private int MAX_PROGRESS;
    //记录运行多少次
    private int runningCount;
    //记录测试运行的结果
    private int on, off, conclusion;
    //定义一个进度条
    private ProgressDialog mProgressDialog;
    private DataSwitch mDataSwitch;
    //定义执行任务的对象
    private Timer timer;

    //是否可以进行测试
    public static DataModeFragment newInstance() {
        return new DataModeFragment();
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
                    if (new DataSwitch(getActivity()).isDataOpen()) {
                        tv_status.setText(R.string.dataison);
                    } else {
                        tv_status.setText(R.string.dataisoff);
                    }
                    String onresult = "成功开启：<font color='green'>%d</font> 次";
                    onresult = String.format(onresult, on);
                    tv_on.setText(Html.fromHtml(onresult));
                    String offresult = "成功关闭：<font color='green'>%d</font> 次";
                    offresult = String.format(offresult, off);
                    tv_off.setText(Html.fromHtml(offresult));
                    int _conclusion = ((on + off) / (on + off)) * 100;
                    String conresult = "测试成功率：<font color='red'>%d</font>";
                    conresult = String.format(conresult, (int) _conclusion);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initView(), container, false);
        title = (TextView) view.findViewById(R.id.title);
        tv_on = (TextView) view.findViewById(R.id.tv_on);
        tv_off = (TextView) view.findViewById(R.id.tv_off);
        tv_conclusion = (TextView) view.findViewById(R.id.tv_conclusion);
        tv_status = (TextView) view.findViewById(R.id.last_status);
        String _title = "数据模式开关测试";
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
                    FragmentManager manager = getFragmentManager();
                    WarningDialogFragment.showDialog(manager, getActivity());
                } else {
                    _interval = Integer.parseInt(interval.getText().toString().trim());
                    _count = Integer.parseInt(count.getText().toString().trim());
                    Log.d(TAG, "onClick: " + _interval + "-----" + _count);
                    myHandler.sendEmptyMessage(0);
                    showDialogProgress(getActivity(), _count);
                    MyTimeDataTask(_interval, _count);
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
        MAX_PROGRESS = max;
        mProgressDialog.setTitle(R.string.datadialogtitle);
        mProgressDialog.setIcon(R.drawable.wait);
        mProgressDialog.setMessage("Waiting .......");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）
        //进度最大值
        mProgressDialog.setMax(MAX_PROGRESS);
        mProgressDialog.setButton2("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + on + "----" + off + "<><><<><<><><");
                myHandler.sendEmptyMessage(1);
                mProgressDialog.dismiss();
                timer.cancel();//取消时间任务
//                //删除消息队列
//                DialogHandler.removeMessages(MSG);
//                //任务暂停
//                timer.cancel();
//                //对话框消失
//                mProgressDialog.dismiss();
//                runningCount = 0;//将进度重新置为0
//                myHandler.sendEmptyMessage(1);
            }
        });
        //显示
        mProgressDialog.show();
        //必须设置点击空白处不消失
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 打开或关闭数据业务开关的工具类
     *
     * @param inter 设定的时间间隔
     * @param count 测试的总次数
     */
    public void MyTimeDataTask(final int inter, final int count) {
        mDataSwitch = new DataSwitch(getActivity());
        dataIsOn = mDataSwitch.isDataOpen();
        Log.d(TAG, "MyTimeDataTask: " + dataIsOn + "wanggongpeng");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!dataIsOn) {// 判断是否打开
                    mDataSwitch.dataSwitcher(true);
                    on++;
                    Log.d(TAG, "MyTimeDataTask run: ");
                    dataIsOn = (!dataIsOn);//置开关为关闭状态
                } else {
                    mDataSwitch.dataSwitcher(false);
                    off++;
                    Log.d(TAG, "MyTimeDataTask run: " + "false");
                    dataIsOn = (!dataIsOn);
                }
                runningCount++;
                mProgressDialog.setProgress(runningCount);
                Log.d(TAG, "run: " + runningCount);
                if (runningCount >= count) {
                    myHandler.sendEmptyMessage(1);
                    mProgressDialog.dismiss();
                    this.cancel();//取消时间任务
                }
            }
        }, 0, inter * 1000);//转换成秒
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }
}