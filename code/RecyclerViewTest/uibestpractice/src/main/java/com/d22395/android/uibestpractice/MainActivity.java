package com.d22395.android.uibestpractice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "msgAdapter";

    private List<Msg> mMsgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    public static int randomNum;

    Handler handler;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "--onCreate--");
        initMsg();
        Random rand = new Random();
        randomNum = rand.nextInt(1000000);
        Log.i("Cherry", "randomNum: " + randomNum);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MsgAdapter(mMsgList);
        msgRecyclerView.setAdapter(adapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    if (!msg.obj.toString().equals(inputText.getText().toString())) {
                        Msg msg1 = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED);
                        mMsgList.add(msg1);
                    }
//                    Msg msg2 = new Msg(inputText.getText().toString(), Msg.TYPE_RECEIVED);
//                    mMsgList.remove(msg2);
                }
                inputText.setText("");
                adapter.notifyDataSetChanged();
                msgRecyclerView.getKeepScreenOn();
                msgRecyclerView.scrollToPosition(mMsgList.size() - 1);
            }
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj = inputText.getText().toString();
//                    Msg msg1 = new Msg(inputText.getText().toString(), Msg.TYPE_SEND);
                    clientThread.revHandler.sendMessage(msg);
                    Msg msg1 = new Msg(inputText.getText().toString(), Msg.TYPE_SEND);
                    mMsgList.add(msg1);
                    adapter.notifyDataSetChanged();
                    msgRecyclerView.getKeepScreenOn();
                    msgRecyclerView.scrollToPosition(mMsgList.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMsg() {

        Msg msg1 = new Msg("Hello guy", Msg.TYPE_RECEIVED);
        mMsgList.add(msg1);
        Msg msg2 = new Msg("Hello, who is that", Msg.TYPE_SEND);
        mMsgList.add(msg2);
        Msg msg3 = new Msg("this is Tom, Nice talking to you", Msg.TYPE_RECEIVED);
        mMsgList.add(msg3);


    }
}
