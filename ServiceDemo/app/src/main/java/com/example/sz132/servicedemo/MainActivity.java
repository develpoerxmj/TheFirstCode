package com.example.sz132.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_end).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.btn_intent).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                start();
                break;
            case R.id.btn_end:
                end();
                break;
            case R.id.btn_bind:
                bind();
                break;
            case R.id.btn_unbind:
                unbind();
                break;
            case R.id.btn_intent:
                intent();
                break;
        }
    }

    private void intent() {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    private static final String TAG = "MyService";
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBind bind = (MyService.MyBind) service;
            bind.show();
            Log.i(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bind() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void start() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    private void end(){
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void unbind() {
        unbindService(connection);
    }
}
