package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.music_buddy_app2.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "ed05ab2bfe8843b7ad314fa1fc2eafc6";
    private static final String REDIRECT_URI = "http://www.music_buddy_app2/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final int REQUEST_CODE = 1337;//this request code is used for verifying if result
    // comes from the login activity and can be set to any interger

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_login);

//        initiateSpotifyLogin();
//        Button loginButton = findViewById(R.id.login_spotify);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        initiateSpotifyLogin();

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
                        Toast.makeText(LoginActivity.this, "Connected! Yay!",Toast.LENGTH_SHORT).show();

                        // Now you can start interacting with App Remote
                        connected();

                    }


                    public void onFailure(Throwable throwable) {
                        if (throwable instanceof NotLoggedInException || throwable instanceof UserNotAuthorizedException) {
                            Toast.makeText(LoginActivity.this, "Not connected! "+ throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            // Show login button and trigger the login flow from auth library when clicked
                        } else if (throwable instanceof CouldNotFindSpotifyApp) {
                            Toast.makeText(LoginActivity.this, "Download Spotify! ",Toast.LENGTH_SHORT).show();
                            //redirecting to Google Play Store
                            final String branchLink = Uri.encode("https://spotify.link/content_linking?~campaign=" + "com.example.music_buddy_app2");
                            final String appPackageName = "com.spotify.music";
                            final String referrer = "_branch_link=" + branchLink;

                            try {
                                Uri uri = Uri.parse("market://details")
                                        .buildUpon()
                                        .appendQueryParameter("id", appPackageName)
                                        .appendQueryParameter("referrer", referrer)
                                        .build();
                                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            } catch (android.content.ActivityNotFoundException ignored) {
                                Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                                        .buildUpon()
                                        .appendQueryParameter("id", appPackageName)
                                        .appendQueryParameter("referrer", referrer)
                                        .build();
                                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            }

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
//        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//
//        // Subscribe to PlayerState
//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final Track track = playerState.track;
//                    if (track != null) {
//
//                        Toast.makeText(LoginActivity.this, track.name + " by " + track.artist.name,Toast.LENGTH_SHORT).show();
//
//                    }
//                });
    }
}