package com.example.music_buddy_app2.ACTIVITIES.LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.TokenManager;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

public class WelcomeActivity extends BaseActivity {


    private TextView helloTextView;
    private TextView doyouknowTextView;
    private TextView illuseTextView;
    Button loginButton;
    private String[] fullTexts;
    private int currentLength = 0;
    private UserManager manager;
    private static final int DELAY_MILLIS = 30;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        TokenManager.initialize(this);
//        try {
//            String accessToken = TokenManager.getInstance().getAccessToken();
//            if (accessToken != null && !accessToken.isEmpty()) {
//
//                if(manager==null)
//                    manager=UserManager.getInstance(WelcomeActivity.this);
////            manager.setAccessToken(accessToken);
////            manager.setRefreshToken(SharedPreferencesManager.getRefreshToken(WelcomeActivity.this));
//                manager.loginUser();
//                Intent intent = new Intent(this, MenuActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            else
//            {
//
//                helloTextView = findViewById(R.id.hello);
//                doyouknowTextView = findViewById(R.id.do_you_know);
//                illuseTextView = findViewById(R.id.ill_use);
//                fullTexts = new String[]{
//                        "Hello!",
//                        "Do you know that friend that knows everything about music? That's me!",
//                        "I'll use your Spotify account for that!"
//                };
//                loginButton = findViewById(R.id.login_spotify);
//                loginButton.setVisibility(View.INVISIBLE);
//
//                startTypingAnimation(0);
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e("CODE_RECEIVED",e.toString());
//        }


        try {
            String accessToken = SharedPreferencesManager.getToken(this);
                if (accessToken != null && !accessToken.isEmpty()) {
                    Log.e("MY_LOGS","Exista shared preferences.");
                if(manager==null)
                    manager=UserManager.getInstance(WelcomeActivity.this);
                manager.loginUser(
//                        new UserManager.LoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        goToMenu();
//                    }
//
//                    @Override
//                    public void onLoginFailure(String errorMessage) {
//                        Log.e("CODE_RECEIVED",errorMessage);
//                    }
//                }
                );
                goToMenu();

            }
            else
            {
                helloTextView = findViewById(R.id.hello);
                doyouknowTextView = findViewById(R.id.do_you_know);
                illuseTextView = findViewById(R.id.ill_use);
                fullTexts = new String[]{
                        "Hello!",
                        "Do you know that friend that knows everything about music? That's me!",
                        "I'll use your Spotify account for that!"
                };
                loginButton = findViewById(R.id.login_spotify);
                loginButton.setVisibility(View.INVISIBLE);

                startTypingAnimation(0);
            }
        }
        catch (Exception e)
        {
            Log.e("MY_LOGS",e.toString());
        }

    }
    private void goToMenu()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
    private void startTypingAnimation(final int textViewIndex) {
        if (textViewIndex >= fullTexts.length) {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            });
            return;
        }

        final TextView currentTextView;
        switch (textViewIndex) {
            case 0:
                currentTextView = helloTextView;
                break;
            case 1:
                currentTextView = doyouknowTextView;
                break;
            case 2:
                currentTextView = illuseTextView;
                break;
            default:
                return;
        }

        handler.postDelayed(() -> {
            if (currentLength <= fullTexts[textViewIndex].length()) {
                String partialText = fullTexts[textViewIndex].substring(0, currentLength);
                currentTextView.setText(Html.fromHtml(partialText));
                currentLength++;
                startTypingAnimation(textViewIndex);
            } else {
                currentLength = 0;
                startTypingAnimation(textViewIndex + 1);
            }
        }, DELAY_MILLIS);

    }
}