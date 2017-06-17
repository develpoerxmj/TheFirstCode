package com.example.broadcastreceiverdemo;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends BaseActivity {

    private LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        manager = LocalBroadcastManager.getInstance(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.sendBroadcast(new Intent(".............."));
            }
        });
    }
}
