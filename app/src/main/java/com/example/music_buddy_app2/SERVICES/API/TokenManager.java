package com.example.music_buddy_app2.SERVICES.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.LOGIN.LoginActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.SpotifyAuthorizationActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.API_RESPONSES.USERS.AccessTokenResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.CodeRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.USERS.RefreshTokenRequestBody;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TokenManager {
    private static TokenManager instance;
    private static Context applicationContext;
    private CustomRecommendationsApiInterface customRecommendationsApiInterface;
    Retrofit retrofit;
    private TokenManager() {
        if (applicationContext != null) {
            retrofit = RetrofitClient.getMyApiRetrofit();
            customRecommendationsApiInterface = retrofit.create(CustomRecommendationsApiInterface.class);
        }
    }
    public static synchronized void initialize(Context context) {
        if (instance == null) {
            applicationContext = context.getApplicationContext();
            instance = new TokenManager();
        }
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TokenManager is not initialized, call initialize() first");
        }
        return instance;
    }
    public synchronized String getAccessToken() {
        long currentTime = System.currentTimeMillis();
        long expiryTime = SharedPreferencesManager.getExpiryTime(applicationContext);
        if (currentTime >= expiryTime) {
            Log.e("CODE_RECEIVED","WILL REFrESH");
            refreshTokenSync();
        }
        return SharedPreferencesManager.getToken(applicationContext);
    }
    private void refreshTokenSync() {

        String refreshToken = SharedPreferencesManager.getRefreshToken(applicationContext);
        if (refreshToken == null) {
            Log.e("CODE_RECEIVED","Refresh token is null");
            Intent intent = new Intent(applicationContext,LoginActivity.class);
            applicationContext.startActivity(intent);
        }
        else {
            RefreshTokenRequestBody requestBody = new RefreshTokenRequestBody(refreshToken);
            Call<AccessTokenResponse> call = customRecommendationsApiInterface.refreshToken(requestBody);
            try {
                Response<AccessTokenResponse> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    AccessTokenResponse tokenResponse = response.body();
                    long newExpiryTime = System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000);
                    SharedPreferencesManager.saveToken(applicationContext, tokenResponse.getAccessToken());
                    SharedPreferencesManager.saveExpiryTime(applicationContext, newExpiryTime);
                    if (!tokenResponse.getRefreshToken().equals(""))
                        SharedPreferencesManager.saveRefreshToken(applicationContext, tokenResponse.getRefreshToken());
                    Log.e("CODE_RECEIVED","UPDATE Refresh Token: "+ SharedPreferencesManager.getRefreshToken(applicationContext));
                    Log.e("CODE_RECEIVED","UPDATE Access token: "+ SharedPreferencesManager.getToken(applicationContext));
                    Log.e("CODE_RECEIVED","UPDATE Expires at: "+ SharedPreferencesManager.getExpiryTime(applicationContext));
                } else {
                    Log.e("CODE_RECEIVED", "failed to refresh token: " + response.code()+ " "+response.message());
                    Log.e("CODE_RECEIVED", "Failed to refresh token" + response.message()+ response.body()+response.headers());
                    Intent intent = new Intent(applicationContext,LoginActivity.class);
                    applicationContext.startActivity(intent);
                }
            } catch (IOException e) {
                Log.e("CODE_RECEIVED", "failed to refresh token: " + e.getMessage());
                Intent intent = new Intent(applicationContext,LoginActivity.class);
                applicationContext.startActivity(intent);
            }
        }
    }
//    public interface TokenRefreshCallback {
//        void onTokenRefreshed();
//        void onTokenRefreshFailed();
//    }
//    public void refreshAccessTokenIfNeeded() {
//        long currentTime = System.currentTimeMillis();
//        long expiryTime = SharedPreferencesManager.getExpiryTime(applicationContext);
//
//        if (currentTime < expiryTime) {
//            Log.e("CODE_RECEIVED", "In token manager: refresh token called");
//            refreshToken(new TokenRefreshCallback() {
//                @Override
//                public void onTokenRefreshed() {
//                    Log.e("CODE_RECEIVED", "Token refreshed successfully");
//                    Log.e("CODE_RECEIVED","In token manager: "+"updated"+ SharedPreferencesManager.getToken(applicationContext) );
//
//                }
//
//                @Override
//                public void onTokenRefreshFailed() {
//                    Log.e("CODE_RECEIVED", "Failed to refresh token");
//                    Intent intent=new Intent(applicationContext, LoginActivity.class);
//                    applicationContext.startActivity(intent);
//                }
//            });
//        }
//
//    }
//    public String getAccessToken()
//    {
//        Log.e("CODE_RECEIVED","s a luat din token manager " +  SharedPreferencesManager.getToken(applicationContext));
//        return  SharedPreferencesManager.getToken(applicationContext);
//    }
//    public synchronized void refreshToken(TokenRefreshCallback callback) {
//        Log.d("CODE_RECEIVED", "Attempting to refresh token");
//        String refreshToken = SharedPreferencesManager.getRefreshToken(applicationContext);
//        if (refreshToken == null) {
//            Log.e("CODE_RECEIVED","Refresh token is null");
//            callback.onTokenRefreshFailed();
//        }
//        else {
//            RefreshTokenRequestBody requestBody = new RefreshTokenRequestBody(refreshToken);
//            Call<AccessTokenResponse> call =  customRecommendationsApiInterface.refreshToken(requestBody);
//            call.enqueue(new Callback<AccessTokenResponse>() {
//                        @Override
//                        public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
//                            if (response.isSuccessful() && response.body() != null) {
//                                AccessTokenResponse tokenResponse = response.body();
//                                Log.e("CODE_RECEIVED","Response body refreshtoken: "+ tokenResponse);
//
//                                long expiryTime = System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000);
//
//                                SharedPreferencesManager.saveToken(applicationContext, tokenResponse.getAccessToken());
//                                SharedPreferencesManager.saveExpiryTime(applicationContext, expiryTime);
//                                Log.e("CODE_RECEIVED","In token manager: "+ "s-a luat refresh tokenul.");
//
//                                if (!tokenResponse.equals(""))
//                                    SharedPreferencesManager.saveRefreshToken(applicationContext, tokenResponse.getRefreshToken());
//                                callback.onTokenRefreshed();
//                            } else {
//                                Log.e("CODE_RECEIVED", "failed to refresh token: " + response.code()+ " "+response.message());
//                                Toast.makeText(applicationContext, "Failed to refresh token", Toast.LENGTH_SHORT).show();
//
//                                callback.onTokenRefreshFailed();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
//                            Log.e("CODE_RECEIVED", "Failure at refresh token: " + t.getMessage());
//
//                            callback.onTokenRefreshFailed();
//                        }
//                    });
//        }
//    }


}
