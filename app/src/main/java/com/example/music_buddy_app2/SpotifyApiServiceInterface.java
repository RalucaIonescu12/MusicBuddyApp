package com.example.music_buddy_app2;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

//retrofit service interface to define api endpoints
public interface SpotifyApiServiceInterface {
    @GET("v1/me")
    Call<UserResponse> getMyProfile(@Header("Authorization") String token);


}
