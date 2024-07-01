package com.example.music_buddy_app2.ACTIVITIES;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.SERVICES.API.TokenManager;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {
    private UserManager manager;
    @Override
    protected void onResume()
    {

        super.onResume();
        Log.d("AHAHA","on resume called");
        try {
            String firebaseToken=SharedPreferencesManager.getCustomToken(this);
            if (firebaseToken != null && !firebaseToken.isEmpty())
            {
                Log.e("MY_LOGS","Refresh toke: " + SharedPreferencesManager.getRefreshToken(this));
                Log.e("MY_LOGS","Custom token: " + SharedPreferencesManager.getCustomToken(this));
                if(manager==null)
                    manager=UserManager.getInstance(BaseActivity.this);
                manager.setCurrentUser(new UserManager.OnUserReceivedListener() {
                    @Override
                    public void onUserReceived(User user) {
                        manager.addUserToDatabase(new UserManager.OnTaskCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Log.e("MY_LOGS","Base activity: Added user to database:");
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                goToWelcomeActivity();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        goToWelcomeActivity();
                    }
                });


            }
            else{
                Log.e("MY_LOGS"," error in on resume in base activity, back to welcome");
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        catch (Exception e)
        {
            Log.e("MY_LOGS",e.toString());
            Log.e("MY_LOGS"," error in on resume in base activity, back to welcome");
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void goToWelcomeActivity(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
