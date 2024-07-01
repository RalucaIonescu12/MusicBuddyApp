package com.example.music_buddy_app2.ACTIVITIES;

import android.app.Application;
import android.util.Log;

import com.example.music_buddy_app2.SERVICES.API.TokenManager;
import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.initialize(this);
        FirebaseApp.initializeApp(this);
        Log.d("MY_LOGS", "Firebase initialized");
    }
}