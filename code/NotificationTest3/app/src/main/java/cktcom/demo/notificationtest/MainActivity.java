package cktcom.demo.notificationtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private NotificationManager  notificationManager;
    private Notification notify1;
    Bitmap LargeBitmap = null;
    private static final int NOTIFYID_1 = 1;
    String chanelId = "my_channel_01";
    String chanelName="我是渠道名字";

    private Button btn_show_normal;
    private Button btn_close_normal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        Button button= (Button) findViewById(R.id.button2);
        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(getResources(), android.R.mipmap.sym_def_app_icon);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        bindView();

    }

    private void bindView() {
        btn_show_normal = (Button) findViewById(R.id.btn_show_normal);
        btn_close_normal = (Button) findViewById(R.id.btn_close_normal);
        btn_show_normal.setOnClickListener(this);
        btn_close_normal.setOnClickListener(this);
        Button btn_photo=(Button) findViewById(R.id.button);
        btn_photo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_normal:
                Intent it=new Intent(mContext,Main2Activity.class);

                PendingIntent pit =PendingIntent.getActivity(mContext,0,it,0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_LOW);
                    // mChannel.enableLights(true);
                    // mChannel.setLightColor(Color.RED);
                    // mChannel.setShowBadge(true);
                    notificationManager.createNotificationChannel(mChannel);
                    notify1=new Notification.Builder(this)
                            .setChannelId(chanelId)
                            .setContentTitle("这个是标题")
                            .setContentText("这个是内容1")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(LargeBitmap)
                            .setAutoCancel(true)
                            .setContentIntent(pit).build();
                } else {
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setContentTitle("这个是标题")
                            .setContentText("这个是内容2")
                            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                            .setLargeIcon(LargeBitmap)
                            .setAutoCancel(true)
                            .setContentIntent(pit)
                            .setOngoing(true);
                    notify1 = builder.build();
                }
                notificationManager.notify(NOTIFYID_1, notify1);

                break;

            case R.id.btn_close_normal:
                //除了可以根据ID来取消Notification外,还可以调用cancelAll();关闭该应用产生的所有通知
                notificationManager.cancel(NOTIFYID_1);                          //取消Notification
                break;
            case R.id.button:
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);

        }
    }

}
