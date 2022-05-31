package com.example.openroom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationBarView
import com.example.openroom.R
import com.example.openroom.fragment.ProfileFragment
import com.example.openroom.fragment.ScannerFragment
import android.view.WindowManager
import androidx.fragment.app.Fragment

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarTranslucent(true)

        /*btnNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.goToProfile -> {
                    replaceFragment(ProfileFragment())
                }
                R.id.goToScanner -> {
                    replaceFragment(ScannerFragment())
                }
            }
            true
        }*/
    }

    private fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainCont, fragment).commit()
    }
}