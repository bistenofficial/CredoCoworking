package com.example.openroom.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.openroom.R;

import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferencesPhone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesPhone = this.getSharedPreferences("Phone", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_splash);
        setStatusBarTranslucent(true);
        Waiting();
    }

    private void Waiting() {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
                if (sharedPreferencesPhone.getString("Phone", "").length() != 0) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LogRegActivity.class);
                }
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}