package com.example.music_buddy_app2.SERVICES.AUTHORIZATION;

import android.content.Context;

import com.example.music_buddy_app2.API_RESPONSES.USERS.AccessTokenResponse;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TokenRefreshService {

    private static final String CLIENT_ID = "ed05ab2bfe8843b7ad314fa1fc2eafc6";
    private static final String CLIENT_SECRET = "47504e30b0ac436a965a60a0ee68d071";

    private static Retrofit retrofit;
    private static final SpotifyApiServiceInterface apiTokenService = createApiInterfaceService();

    public static SpotifyApiServiceInterface getApiServiceTokenInterfaceInstance() {
        return apiTokenService;
    }
    private static SpotifyApiServiceInterface createApiInterfaceService() {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitTokenInstance();
        }
        return retrofit.create(SpotifyApiServiceInterface.class);
    }

    // Executed asynchronously

    public static void refreshAccessTokenAsynchronous(Context context, Callback<AccessTokenResponse> callback) {
//        String clientCredentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET);
        String refreshToken = SharedPreferencesManager.getToken(context);

        // Access token refresh call
        Call<AccessTokenResponse> call = apiTokenService.refreshAccessToken(
                "refresh_token",
                refreshToken,
                CLIENT_ID
        );


        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful()) {
                    AccessTokenResponse accessTokenResponse = response.body();
                    if (accessTokenResponse != null) {
                        String newAccessToken = accessTokenResponse.getAccessToken();
                        // save the new access token using SharedPreferencesManager
                        SharedPreferencesManager.saveToken(context, newAccessToken);
                    }
                } else {
                    // Handle error
                    // You might want to notify the user or take appropriate actions
                }
                // Pass the response to the original callback
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                // Handle failure
                // You might want to notify the user or take appropriate actions
                callback.onFailure(call, t);
            }
        });
    }
}
