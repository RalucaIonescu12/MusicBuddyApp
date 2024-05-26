package com.example.music_buddy_app2.ACTIVITIES.OTHERS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.TokenManager;

public class AnalysisActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
    }
    @Override
    protected void onResume() {
        super.onResume();
        TokenManager.initialize(this);
    }
}