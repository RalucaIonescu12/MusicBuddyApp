package com.example.music_buddy_app2.SERVICES;

import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

//retrofit service interface to define api endpoints
public interface SpotifyApiServiceInterface {
    @GET("v1/me")
    Call<UserResponse> getMyProfile(@Header("Authorization") String token);
    @GET("v1/me/top/artists")
    Call<TopArtistsResponse> getUserTopArtists(@Header("Authorization") String token,
                                               @Query("limit") int limit,
                                               @Query("offset") int offset,
                                               @Query("time_range") String timeRange);

    @GET("v1/me/top/tracks")
    Call<TopTracksResponse> getUserTopTracks(
            @Header("Authorization") String token,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("time_range") String timeRange
    );

}
