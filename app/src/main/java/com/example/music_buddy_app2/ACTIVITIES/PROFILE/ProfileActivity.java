package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.MODELS.User;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class ProfileActivity extends BaseActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    public User user;
    private UserApiManager userApiManager;
    private UserManager managerFirebase;
    Retrofit retrofit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userApiManager=UserApiManager.getInstance(this);
        initiateSpotifyApiService();
        fetchUserProfileData();
        managerFirebase= UserManager.getInstance(this);
        CardView topArtists = findViewById(R.id.card_top_items);
        CardView manageFriends= findViewById(R.id.manageFriends);
        CardView logoutCV =findViewById(R.id.logoutCV);
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
        logoutCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                managerFirebase.logout();
                showLogoutConfirmationDialog();
            }
        });
        Log.e("MY_LOGS","AM INTART IN PROFILE");
    }
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
//                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        managerFirebase.logout();
                        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void setUser(User user)
    {
        this.user=managerFirebase.getUser();
        Log.e("MY_LOGS",this.user.toString());
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
                Log.e("MY_LOGS","Profile: "+ user);
                setUser(user);

                //set the other info from firebase, playlists etc
                updateUI();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MY_LOGS",errorMessage);
                Toast.makeText(ProfileActivity.this, "Failed to retrieve profile" + errorMessage, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthorizationError()
            {
                Log.e("MY_LOGS","Need to reauthorize");
                Toast.makeText(ProfileActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI()
    {
        TextView username = findViewById(R.id.username);
        TextView playlists = findViewById(R.id.playlists_count );
        ImageView profilePicture = findViewById(R.id.profile_picture);
        ImageView topTracks = findViewById(R.id.show_top_songs_image);

        user.setPlaylistsCreatedWithTheApp(SharedPreferencesManager.getNbrGeneratedPlaylists(ProfileActivity.this));
        SharedPreferencesManager.updateNbrPlaylists(ProfileActivity.this,user.getPlaylistsCreatedWithTheApp());
        playlists.setText(user.getPlaylistsCreatedWithTheApp().toString());
        username.setText(user.getUsername());
        Picasso.get().load(user.getProfileImageUrl()).into(profilePicture);
        topTracks.setImageResource(R.drawable.vinylss);
    }
}