package com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.R;

public class LoadingActivity extends AppCompatActivity {

    ImageView vinylImage;
    Animation rotationAnimation;
    static ChooseContextDetailsManager manager;
    static String genre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vinylImage = findViewById(R.id.vinyl_image);
        rotateAnimation();
        manager=ChooseContextDetailsManager.getInstance(this);
        request();
    }


    private void rotateAnimation() {
        rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_vynil);
        vinylImage.startAnimation(rotationAnimation);
    }

    public void finishLoading() {
        vinylImage.clearAnimation();
        finish();
    }
    public void request()
    {
        manager.getRecommendations(genre ,new ChooseContextDetailsManager.RecommendationOperationsCompleteCallback() {
            @Override
            public void onComplete() {
                Intent intent = new Intent(LoadingActivity.this, SeeOurRecommendationsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoadingActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}