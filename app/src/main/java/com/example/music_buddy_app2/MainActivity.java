package com.example.music_buddy_app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "ed05ab2bfe8843b7ad314fa1fc2eafc6";
    private static final String REDIRECT_URI = "http://www.music_buddy_app2/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final int REQUEST_CODE = 1337;//this request code is used for verifying if result
    // comes from the login activity and can be set to any interger

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initiateSpotifyLogin();
        Button loginButton = findViewById(R.id.login_spotify);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateSpotifyLogin();
            }
        });

    }
    private void initiateSpotifyLogin() {
//        AuthorizationRequest.Builder builder =
//                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
//
//        builder.setScopes(new String[]{"streaming"});
//        AuthorizationRequest request = builder.build();
//
//        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        Intent intent = new Intent(this, SpotifyAuthorizationActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Toast.makeText(MainActivity.this, "Connected! Yay!",Toast.LENGTH_SHORT).show();

                        // Now you can start interacting with App Remote
                        connected();

                    }


                    public void onFailure(Throwable throwable) {
                        if (throwable instanceof NotLoggedInException || throwable instanceof UserNotAuthorizedException) {
                            Toast.makeText(MainActivity.this, "Not connected! "+ throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            // Show login button and trigger the login flow from auth library when clicked
                        } else if (throwable instanceof CouldNotFindSpotifyApp) {
                            Toast.makeText(MainActivity.this, "Download Spotify! ",Toast.LENGTH_SHORT).show();
                            //redirecting to Google Play Store
                            

                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Toast.makeText(MainActivity.this, track.name + " by " + track.artist.name,Toast.LENGTH_SHORT).show();

                    }
                });
    }
}