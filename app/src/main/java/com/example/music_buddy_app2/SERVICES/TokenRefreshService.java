package com.example.music_buddy_app2.SERVICES;

public class TokenRefreshService {

//    private static final String CLIENT_ID = "ed05ab2bfe8843b7ad314fa1fc2eafc6";
//    private static final String CLIENT_SECRET = "47504e30b0ac436a965a60a0ee68d071";
//
//    private static Retrofit retrofit;
//    private static final TokenRefreshServiceInterface apiTokenService = createApiTokenInterfaceService();
//
//    public static TokenRefreshServiceInterface getApiServiceTokenInterfaceInstance() {
//        return apiTokenService;
//    }
//    private static TokenRefreshServiceInterface createApiTokenInterfaceService() {
//        if (retrofit == null) {
//            retrofit = RetrofitClient.getRetrofitTokenInstance();
//        }
//        return retrofit.create(TokenRefreshServiceInterface.class);
//    }
//
//    // Executed asynchronously
//
//    public static void refreshAccessTokenAsynchronous(Context context, Callback<AccessTokenResponse> callback) {
////        String clientCredentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET);
//        String refreshToken = SharedPreferencesManager.getToken(context);
//
//        // Access token refresh call
//        Call<AccessTokenResponse> call = apiTokenService.refreshAccessToken(
//                "refresh_token",
//                refreshToken,
////                clientCredentials,
//                CLIENT_ID
//        );
//
//
//        call.enqueue(new Callback<AccessTokenResponse>() {
//            @Override
//            public void onResponse(Call<AccessTokenResponse> call, retrofit2.Response<AccessTokenResponse> response) {
//                if (response.isSuccessful()) {
//                    AccessTokenResponse accessTokenResponse = response.body();
//                    if (accessTokenResponse != null) {
//                        String newAccessToken = accessTokenResponse.getAccessToken();
//                        // Save the new access token using SharedPreferencesManager
//                        SharedPreferencesManager.saveToken(context, newAccessToken);
//                    }
//                } else {
//                    // Handle error
//                    // You might want to notify the user or take appropriate actions
//                }
//                // Pass the response to the original callback
//                callback.onResponse(call, response);
//            }
//
//            @Override
//            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
//                // Handle failure
//                // You might want to notify the user or take appropriate actions
//                callback.onFailure(call, t);
//            }
//        });
//    }
}
