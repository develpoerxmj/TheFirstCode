package com.example.myfragmenttestactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sz132 on 2017/6/7.
 */

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ((ContentFragment)getSupportFragmentManager().findFragmentById(R.id.content)).refresh(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"));
    }

    public static void startIntent(Activity context, String title, String content){
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }
}
