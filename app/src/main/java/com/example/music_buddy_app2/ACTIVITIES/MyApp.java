package com.example.music_buddy_app2.ACTIVITIES;

import android.app.Application;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.music_buddy_app2.SERVICES.API.TokenManager;

import java.util.concurrent.TimeUnit;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.initialize(this);

    }
}