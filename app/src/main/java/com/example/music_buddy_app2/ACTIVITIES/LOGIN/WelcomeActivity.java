package com.example.music_buddy_app2.ACTIVITIES.LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.music_buddy_app2.R;

public class WelcomeActivity extends AppCompatActivity {


    private TextView helloTextView;
    private TextView doyouknowTextView;
    private TextView illuseTextView;
    Button loginButton;
    private String[] fullTexts;
    private int currentLength = 0;
    private static final int DELAY_MILLIS = 30;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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