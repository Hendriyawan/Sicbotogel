package com.hdev.sicbotogel.view;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.hdev.sicbotogel.utils.AppPreferences;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        AppPreferences.getInstance(this);
    }
}
