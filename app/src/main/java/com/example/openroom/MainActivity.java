package com.example.openroom;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    private TextView textViewDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarTranslucent(true);
        textViewDecode = findViewById(R.id.textView);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        askPermission();
        if (CameraPermission) {

            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codeScanner.startPreview();
                }
            });

            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            textViewDecode.setText(result.getText());
                        }
                    });

                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERM){


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                codeScanner.startPreview();
                CameraPermission = true;
            }else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    }).create().show();

                }else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [Settings] > [Permissions]")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package",getPackageName(),null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                            }).setNegativeButton("No, Exit app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            finish();

                        }
                    }).create().show();



                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void askPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);
            } else {
                codeScanner.startPreview();
                CameraPermission = true;
            }
        }
    }


    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}