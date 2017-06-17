package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.notification).setOnClickListener(this);
        findViewById(R.id.notification_two).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notification:
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this).
                        setSmallIcon(R.mipmap.ic_launcher).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentText("this is notification")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setContentTitle("notification")
//                        .setFullScreenIntent(pendingIntent, false)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(1, notification);
                break;
            case R.id.notification_two:
                //1.获取通知管理器
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //2.实例化通知栏构造器
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                //3. 在builder中配置通知的标题内容等

                //**最为重要的一个参数，如果不设置，通知不会出现在状态栏中。**
                builder.setSmallIcon(R.mipmap.ic_launcher);

                builder.setContentTitle("这是通知");
                builder.setContentText("这是通知的内容");
                builder.setTicker("通知来了");
                //通知产生的时间，一般是获取系统的时间
                builder.setWhen(System.currentTimeMillis());
                //设置通知的优先级
                builder.setPriority(Notification.PRIORITY_MAX);
                //设置通知的铃声 震动等 最简单是设置系统默认
                builder.setDefaults(Notification.DEFAULT_VIBRATE);

                //4. 发出通知
                notificationManager.notify(2,builder.build());
        }
    }
}
