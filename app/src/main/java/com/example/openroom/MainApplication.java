package com.example.openroom;

import android.app.Application;

import com.example.openroom.api.ApiManager;

public class MainApplication extends Application {
    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
    }
}
