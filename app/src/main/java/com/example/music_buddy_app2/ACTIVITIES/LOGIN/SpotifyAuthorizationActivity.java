package com.example.music_buddy_app2.ACTIVITIES.LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.API_RESPONSES.USERS.AccessTokenResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.CodeRequestBody;
import com.example.music_buddy_app2.BuildConfig;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.CustomRecommendationsApiInterface;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpotifyAuthorizationActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;/// used to verify if a result comes back
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    private static final String REDIRECT_URI = "http://www.music_buddy_app2/callback";
    Retrofit retrofit;
    private CustomRecommendationsApiInterface customRecommendationsApiInterface;
    ImageView vinylImage;
    Animation rotationAnimation;
    private UserManager manager;
   @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_authorization);

       vinylImage = findViewById(R.id.vinyl_image_auth);
       rotateAnimation();

        if(manager==null)
            manager=UserManager.getInstance(SpotifyAuthorizationActivity.this);
        initiateApiService();
        initiateSpotifyLogin();
    }
    private void rotateAnimation() {
        rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_vynil);
        vinylImage.startAnimation(rotationAnimation);
    }
    public void initiateApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getMyTokenApiRetrofit();
        }
        customRecommendationsApiInterface = retrofit.create(CustomRecommendationsApiInterface.class);
    }
    private void initiateSpotifyLogin() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-read-email", "streaming","user-top-read",
                "user-modify-playback-state","playlist-modify-public",
                "playlist-read-private","playlist-read-collaborative",
                "playlist-modify-private"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent); //fetches the code if result is correct
            if (response.getType() == AuthorizationResponse.Type.CODE) {
                manager.authenticateAnonymously(new UserManager.OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {
                        CodeRequestBody requestBody = new CodeRequestBody(response.getCode());
                        Call<AccessTokenResponse> call = customRecommendationsApiInterface.exchangeCode(requestBody);
                        call.enqueue(new Callback<AccessTokenResponse>() {
                            @Override
                            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                                if (response.isSuccessful()) {
                                    AccessTokenResponse tokenResponse = response.body();
                                    long expiryTime = System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000);
                                    SharedPreferencesManager.saveToken(SpotifyAuthorizationActivity.this, tokenResponse.getAccessToken());
                                    SharedPreferencesManager.saveRefreshToken(SpotifyAuthorizationActivity.this, tokenResponse.getRefreshToken());
                                    SharedPreferencesManager.saveExpiryTime(SpotifyAuthorizationActivity.this, expiryTime);
                                    SharedPreferencesManager.updateNbrPlaylists(SpotifyAuthorizationActivity.this,1);
                                    manager.addUserToDatabase(new UserManager.OnTaskCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            goToMenu();
                                        }
                                        @Override
                                        public void onFailure(String errorMessage) {
                                            goBackToLogin();
                                        }
                                    });

                                } else {
                                    Toast.makeText(SpotifyAuthorizationActivity.this, "Error exchanging code", Toast.LENGTH_SHORT).show();
                                    Log.e("MY_LOGS","In spotify authorization: Error exchanging code in server");
                                    goBackToLogin();
                                }
                            }

                            @Override
                            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                                Log.e("MY_LOGS","In spotify authorization: failure");
                                goBackToLogin();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {goBackToLogin();}
                });


            }
        }
    }
    public void goToMenu()
    {
        Log.e("MY_LOGS","Refresh Token: "+ SharedPreferencesManager.getRefreshToken(SpotifyAuthorizationActivity.this));
        Log.e("MY_LOGS","Access token: "+ SharedPreferencesManager.getToken(SpotifyAuthorizationActivity.this));
        Log.e("MY_LOGS","Expires at: "+ SharedPreferencesManager.getExpiryTime(SpotifyAuthorizationActivity.this));
        Intent intent = new Intent(SpotifyAuthorizationActivity.this, MenuActivity.class);
        startActivity(intent);
    }
    public void goBackToLogin()
    {
        Log.e("MY_LOGS","Eroare login in user manager -> go back to login activity");
        Intent intent = new Intent(SpotifyAuthorizationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        // Using auth lib
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent); //fetches the access token if result is correct
//
//            switch (response.getType()) {
//                // Response was successful and contains auth token
//                case TOKEN:
//                    SharedPreferencesManager.saveToken(SpotifyAuthorizationActivity.this,response.getAccessToken());
//                    manager.setAccessToken(response.getAccessToken());
//                    manager.loginUser();
//                    Intent intentt = new Intent(SpotifyAuthorizationActivity.this, MenuActivity.class);
//                    startActivity(intentt);
//                    break;
//
//                // Auth flow returned an error
//                case ERROR:
//                    // Handle error response
//                    Toast.makeText(SpotifyAuthorizationActivity.this, "Error at request code! ",Toast.LENGTH_SHORT).show();
//                    break;
//
//                // Most likely auth flow was cancelled
//                default:
//                    Toast.makeText(SpotifyAuthorizationActivity.this, "Other case at request code! ",Toast.LENGTH_SHORT).show();
//                    // Handle other cases
//            }
//        }
//    }  //OLD VERSION
}