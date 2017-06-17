package com.example.rememberpassword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by sz132 on 2017/6/8.
 */

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private String name;
    private boolean isChecked;

    private EditText edit;
    private CheckBox box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = (EditText) findViewById(R.id.edit);
        box = (CheckBox) findViewById(R.id.box);

        isChecked = preferences.getBoolean("isChecked", false);
        box.setChecked(isChecked);

        if (isChecked){
            edit.setText(preferences.getString("name", null));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preferences.edit().putString("name", edit.getText().toString()).apply();
        preferences.edit().putBoolean("isChecked", box.isChecked()).apply();
    }
}
