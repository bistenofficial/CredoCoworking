package com.example.openroom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openroom.R
import com.example.openroom.fragment.AuthFragment
import android.view.WindowManager

class LogRegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)
        setStatusBarTranslucent(true)
    }

    protected fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}