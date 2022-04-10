package com.example.openroom.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.openroom.R;
import com.example.openroom.databinding.ActivityMainBinding;
import com.example.openroom.fragment.ProfileFragment;
import com.example.openroom.fragment.ScannerFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarTranslucent(true);
        binding.btnNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.goToProfile: {
                    ReplaceFragment(new ProfileFragment());
                    break;
                }
                case R.id.goToScanner: {
                    ReplaceFragment(new ScannerFragment());
                    break;
                }
            }
            return true;
        });
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.mainCont, profileFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
private void ReplaceFragment(Fragment fragment)
{
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.mainCont,fragment).commit();
}
}