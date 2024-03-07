package com.example.music_buddy_app2.SERVICES;

import com.example.music_buddy_app2.API_RESPONSES.AccessTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenRefreshServiceInterface {

//    @FormUrlEncoded
    @POST("api/token")
    Call<AccessTokenResponse> refreshAccessToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Header("Content-Type") String contentType, // "application/x-www-form-urlencoded";
            @Header("Authorization") String clientCredentials, ///the format: Authorization: Basic <base64 encoded client_id:client_secret
            @Header("client_id") String clientId
    );

}
