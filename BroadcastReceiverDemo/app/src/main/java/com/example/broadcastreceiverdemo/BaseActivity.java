package com.example.broadcastreceiverdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sz132 on 2017/6/8.
 */

public class BaseActivity extends AppCompatActivity {

    private LocalBroadcastManager manager;
    private MyReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager = LocalBroadcastManager.getInstance(this);
        receiver = new MyReceiver();
        intentFilter = new IntentFilter("..............");
        manager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityList.removeActivity(this);
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this)
                    .setTitle("")
                    .setMessage("退出")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityList.finishAllActivity();
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    });
            dialog.show();
        }
    }
}
