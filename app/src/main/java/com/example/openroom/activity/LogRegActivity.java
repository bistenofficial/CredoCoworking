package com.example.openroom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.openroom.R;
import com.example.openroom.fragment.AuthFragment;

public class LogRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);
        AuthFragment authFragment = new AuthFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fr_place, authFragment)
                .commit();
        setStatusBarTranslucent(true);
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