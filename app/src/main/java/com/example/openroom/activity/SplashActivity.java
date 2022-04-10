package com.example.openroom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.openroom.R;

import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBarTranslucent(true);
        Waiting();
    }
    private void Waiting()
    {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
                Intent intent = new Intent(SplashActivity.this, LogRegActivity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}