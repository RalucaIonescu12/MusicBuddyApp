package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class SpotifyAuthorizationActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;/// used to verify if a result comes back
    private static final String CLIENT_ID = "ed05ab2bfe8843b7ad314fa1fc2eafc6";
    private static final String REDIRECT_URI = "http://www.music_buddy_app2/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_authorization);
        initiateSpotifyLogin();
    }
    private void initiateSpotifyLogin() {

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-read-email", "streaming","user-top-read"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Using auth lib
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent); //fetches the access token if result is correct

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Toast.makeText(SpotifyAuthorizationActivity.this, "Bingo! ",Toast.LENGTH_SHORT).show();
                    SharedPreferencesManager.saveToken(SpotifyAuthorizationActivity.this,response.getAccessToken());
                    Toast.makeText(SpotifyAuthorizationActivity.this, SharedPreferencesManager.getToken(this),Toast.LENGTH_SHORT).show();
                    Intent intentt = new Intent(SpotifyAuthorizationActivity.this, MenuActivity.class);
                    startActivity(intentt);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(SpotifyAuthorizationActivity.this, "Error at request code! ",Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(SpotifyAuthorizationActivity.this, "Other case at request code! ",Toast.LENGTH_SHORT).show();
                    // Handle other cases
            }
        }
    }


}