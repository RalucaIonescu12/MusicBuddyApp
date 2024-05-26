package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView username;
    private CardView spotifyIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilePicture = findViewById(R.id.profile_picture);
        username = findViewById(R.id.username);
        spotifyIcon = findViewById(R.id.goToSpotifyCV);

        String userId = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");
        String userProfileImage = getIntent().getStringExtra("user_profile_image");
        String uri=getIntent().getStringExtra("uri");
        String nbr_playlists=getIntent().getStringExtra("nbr_playlists");

        username.setText(userName);
        Picasso.get().load(userProfileImage).into(profilePicture);
        spotifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.spotify.music");
                startActivity(intent);
            }
        });

    }
}