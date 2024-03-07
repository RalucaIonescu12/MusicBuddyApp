package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.music_buddy_app2.R;

public class RecommendationsBySpotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_recommendations);

        CardView testItOutRecCardView = findViewById(R.id.test_it_out_rec);
        testItOutRecCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
            }
        });
        CardView getSpotifyRec = findViewById(R.id.get_recs);
        getSpotifyRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendationsBySpotifyActivity.this, RecFromSpotify.class));
            }
        });


        CardView testTrackFeatures = findViewById(R.id.test_it_out_rec);
        testTrackFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendationsBySpotifyActivity.this, AudioFeaturesTrackActivity.class));
            }
        });
    }
}