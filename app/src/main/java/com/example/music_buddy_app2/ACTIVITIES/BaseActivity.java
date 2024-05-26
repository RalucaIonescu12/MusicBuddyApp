package com.example.music_buddy_app2.ACTIVITIES;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.TokenManager;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {
    private UserManager manager;
    @Override
    protected void onResume()
    {
        super.onResume();
        TokenManager.initialize(this);
        try {
            String accessToken=SharedPreferencesManager.getToken(this);
            if (accessToken != null && !accessToken.isEmpty())
            {
                Log.e("MY_LOGS","Exista shared preferences");
                if(manager==null)
                    manager=UserManager.getInstance(BaseActivity.this);
                manager.loginUser(
//                        new UserManager.LoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        Log.e("CODE_RECEIVED","logged in correctly");
//                    }
//
//                    @Override
//                    public void onLoginFailure(String errorMessage) {
//                        Log.e("CODE_RECEIVED",errorMessage);
//                    }
//                }
                );

            }
        }
        catch (Exception e)
        {
            Log.e("MY_LOGS",e.toString());
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
