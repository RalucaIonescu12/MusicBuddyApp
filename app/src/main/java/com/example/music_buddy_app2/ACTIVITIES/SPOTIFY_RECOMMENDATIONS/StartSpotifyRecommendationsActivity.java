package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.music_buddy_app2.R;

public class StartSpotifyRecommendationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_spotify_recommendations);

        CardView getSpotifyRec = findViewById(R.id.button_start);
        getSpotifyRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartSpotifyRecommendationsActivity.this, ChooseTracksWithAudioFeaturesForSpotifyRecActivity.class));
            }
        });
    }
}