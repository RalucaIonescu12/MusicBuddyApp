package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
//    private SpotifyService spotifyService;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    public User user;
    private UserApiManager userApiManager;
    Retrofit retrofit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userApiManager=UserApiManager.getInstance(this);
        initiateSpotifyApiService();
        fetchUserProfileData();

        CardView topArtists = findViewById(R.id.card_top_items);
        CardView manageFriends= findViewById(R.id.manageFriends);
        user=new User();
        topArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UsersTopItemsActivity.class);
                startActivity(intent);
            }
        });
        manageFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FindFriendsActivity.class);
                startActivity(intent);
            }
        });

    }
    public void setUser(User user)
    {
        this.user=user;
        Log.e("FIREBASE_LOGS",this.user.toString());
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }


    private void fetchUserProfileData() {

        userApiManager.getProfile( new UserApiManager.UserApiListener() {
            @Override
            public void onProfileReceived(User user) {
                Log.e("FIREBASE_LOGS","Profile: "+ user);
                setUser(user);
                updateUI();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("FIREBASE_LOGS",errorMessage);
                Toast.makeText(ProfileActivity.this, "Failed to retrieve profile" + errorMessage, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthorizationError()
            {
                Toast.makeText(ProfileActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI()
    {
        TextView username = findViewById(R.id.username);
        TextView totalWins = findViewById(R.id.total_games_won_count );
        TextView totalGamesPlayed = findViewById(R.id.total_games_played_count);
        TextView totalScore = findViewById(R.id.total_score_count);
        ImageView profilePicture = findViewById(R.id.profile_picture);
        ImageView topArtists = findViewById(R.id.show_top_artists_image);
        ImageView topTracks = findViewById(R.id.show_top_songs_image);

        username.setText(user.getUsername());
        totalWins.setText(user.getTotalWins().toString());
        totalGamesPlayed.setText(user.getTotalGamesPlayed().toString());
        totalScore.setText(user.getTotalScore().toString());
        Picasso.get().load(user.getProfileImageUrl()).into(profilePicture);
        topArtists.setImageResource(R.drawable.top_artists);
        topTracks.setImageResource(R.drawable.top_tracks);
    }
}