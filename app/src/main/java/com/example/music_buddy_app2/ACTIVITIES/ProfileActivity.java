package com.example.music_buddy_app2.ACTIVITIES;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.API_RESPONSES.UserResponse;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
//    private SpotifyService spotifyService;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    public User user;
    Retrofit retrofit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initiateSpotifyApiService();
        fetchUserProfileData();

        CardView topArtists = findViewById(R.id.card_top_items);

        // Set OnClickListener on the topArtists ImageView
        topArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UsersTopItemsActivity when the topArtists image is clicked
                Intent intent = new Intent(ProfileActivity.this, UsersTopItemsActivity.class);
                startActivity(intent);
            }
        });

    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }


    private void fetchUserProfileData() {

        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;
        Call<UserResponse> call= spotifyApiServiceInterface.getMyProfile(autorization);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful()) {

                    UserResponse userResponse = response.body();
                    user = new User(userResponse.getDisplayName(), userResponse.getEmail(),  userResponse.getImages().get(0).getUrl(),userResponse.getId());
                    updateUI();
                }
                else {
                    Toast.makeText(ProfileActivity.this, "You need to reauthorize!" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(ProfileActivity.this, "Failed profile info request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
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