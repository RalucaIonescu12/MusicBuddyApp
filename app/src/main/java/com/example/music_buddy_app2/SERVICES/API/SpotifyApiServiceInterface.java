package com.example.music_buddy_app2.SERVICES.API;

import com.example.music_buddy_app2.API_RESPONSES.USERS.AccessTokenResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.AddTracksToPlaylistResponse;
import com.example.music_buddy_app2.API_RESPONSES.AUDIO_FEATURES.AudioFeaturesObjectResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.GenresResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.PlaylistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.SearchArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SpotifyRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TopTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("v1/search")
    Call<SearchArtistsResponse> searchArtists(
            @Header("Authorization") String token,
            @Query("q") String query,
            @Query("type") String type,   //"artist"
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
    @GET("v1/me/playlists")
    Call<PlaylistsResponse> getMyPlaylists(
            @Header("Authorization") String token,
            @Query("limit") int limit, //0-50
            @Query("offset") int offset //max offset=100.000
    );

    @GET("v1/recommendations/available-genre-seeds")
    Call<GenresResponse> getAvailableGenres(
            @Header("Authorization") String token
    );
    @GET("v1/recommendations")
    Call<SpotifyRecommendationsResponse> getRecommendations(
            @Header("Authorization") String token,
            @Query("limit") int limit,
            @Query("seed_artists") String seedArtists,
            @Query("seed_genres") String seedGenres,
            @Query("seed_tracks") String seedTracks,
            @Query("min_acousticness") Double minAcousticness,
            @Query("max_acousticness") Double maxAcousticness,
            @Query("target_acousticness") Double targetAcousticness,
            @Query("min_danceability") Double minDanceability,
            @Query("max_danceability") Double maxDanceability,
            @Query("target_danceability") Double targetDanceability,
            @Query("min_duration_ms") int minDurationMs,
            @Query("max_duration_ms") int maxDurationMs,
            @Query("target_duration_ms") int targetDurationMs,
            @Query("min_energy") Double minEnergy,
            @Query("max_energy") Double maxEnergy,
            @Query("target_energy") Double targetEnergy,
            @Query("min_instrumentalness") Double minInstrumentalness,
            @Query("max_instrumentalness") Double maxInstrumentalness,
            @Query("target_instrumentalness") Double targetInstrumentalness,
            @Query("min_key") int minKey,
            @Query("max_key") int maxKey,
            @Query("target_key") int targetKey,
            @Query("min_liveness") Double minLiveness,
            @Query("max_liveness") Double maxLiveness,
            @Query("target_liveness") Double targetLiveness,
            @Query("min_loudness") Double minLoudness,
            @Query("max_loudness") Double maxLoudness,
            @Query("target_loudness") Double targetLoudness,
            @Query("min_mode") int minMode,
            @Query("max_mode") int maxMode,
            @Query("target_mode") int targetMode,
            @Query("min_popularity") int minPopularity,
            @Query("max_popularity") int maxPopularity,
            @Query("target_popularity") int targetPopularity,
            @Query("min_speechiness") Double minSpeechiness,
            @Query("max_speechiness") Double maxSpeechiness,
            @Query("target_speechiness") Double targetSpeechiness,
            @Query("min_tempo") Double minTempo,
            @Query("max_tempo") Double maxTempo,
            @Query("target_tempo") Double targetTempo,
            @Query("min_time_signature") int minTimeSignature,
            @Query("max_time_signature") int maxTimeSignature,
            @Query("target_time_signature") int targetTimeSignature,
            @Query("min_valence") Double minValence,
            @Query("max_valence") Double maxValence,
            @Query("target_valence") Double targetValence
    );

    @POST("v1/me/player/queue")
    Call<Void> addItemToPlaybackQueue(
            @Header("Authorization") String token,
            @Query("uri") String uri  //format: spotify:track:4iV5W9uYEdYUVa79Axb7Rh
    );
    @POST("v1/users/{user_id}/playlists")
    Call<Void> createPlaylistForUser(
            @Header("Authorization") String token,
            @Path("user_id") String userId,
            @Body PlaylistRequestBody playlistRequestBody
    );
    @POST("v1/playlists/{playlist_id}/tracks")
    Call<AddTracksToPlaylistResponse> addTracksToPlaylist(
            @Header("Authorization") String token,
            @Path("playlist_id") String playlistId,
            @Body PlaylistTracksRequest tracksRequest
    );
    @GET("v1/users/{user_id}/playlists")
    Call<PlaylistsResponse> getUserPlaylists(
            @Header("Authorization") String token,
            @Path("user_id") String userId,
            @Query("limit") int limit,
            @Query("offset") int offset
    );
    @FormUrlEncoded
    @POST("api/token")
    Call<AccessTokenResponse> refreshAccessToken(
//            @Header("Authorization") String token, ///the format: Authorization: Basic <base64 encoded client_id:client_secret
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
//            @Header("Content-Type") String contentType, // "application/x-www-form-urlencoded";
            @Header("client_id") String clientId
    );
}
