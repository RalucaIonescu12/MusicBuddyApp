package com.example.music_buddy_app2.SERVICES;

import com.example.music_buddy_app2.API_RESPONSES.AudioFeaturesObjectResponse;
import com.example.music_buddy_app2.API_RESPONSES.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
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

    @GET("v1/audio-features")
    Call<TopTracksResponse> getSeveralTracksAudioFeatures(
            @Query("ids") String ids
    );

    @GET("v1/search")
    Call<SearchTrackResponse> searchTracks(
            @Header("Authorization") String token,
            @Query("q") String query,
            @Query("type") String type,   //"track"
            @Query("limit") int limit, //Range: 0 - 50
            @Query("offset") int offset //The index of the first result to return Range: 0 - 1000
            // client can play externally hosted audio content, and marks the content as playable in the response.
            // default hosted audio content is marked as unplayable in the response.
    );

    @GET("v1/audio-features/{id}")
    Call<AudioFeaturesObjectResponse> getTracksFeatures(
            @Header("Authorization") String token,
            @Path("id") String id
    );

}
