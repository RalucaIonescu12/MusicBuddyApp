package com.example.music_buddy_app2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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
        ImageView topSongs = findViewById(R.id.show_top_songs_image);

        username.setText(user.getUsername());
        totalWins.setText(user.getTotalWins().toString());
        totalGamesPlayed.setText(user.getTotalGamesPlayed().toString());
        totalScore.setText(user.getTotalScore().toString());
        Picasso.get().load(user.getProfileImageUrl()).into(profilePicture);
        Picasso.get().load(user.getProfileImageUrl()).into(topArtists);
        Picasso.get().load(user.getProfileImageUrl()).into(topSongs);
    }
}