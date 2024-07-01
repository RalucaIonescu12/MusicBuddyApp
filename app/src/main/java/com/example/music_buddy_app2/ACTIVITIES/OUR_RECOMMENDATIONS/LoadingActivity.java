package com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.R;

public class LoadingActivity extends BaseActivity {

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
        this.genre= ChooseContextDetailsManager.getGenre();
        Log.e("MY_LOGS","SENT GENRE: "+ this.genre);
        manager.getRecommendations(genre ,new ChooseContextDetailsManager.RecommendationOperationsCompleteCallback() {
            @Override
            public void onComplete() {

                Intent intent = new Intent(LoadingActivity.this, SeeOurRecommendationsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Log.e("MY_LOGS", "ERROR MESSAGE LA GET RECS" +message);
//                Toast.makeText(LoadingActivity.this, message, Toast.LENGTH_LONG).show();
                Toast.makeText(LoadingActivity.this, "Sorry! Try again!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoadingActivity.this, ChooseContextDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}