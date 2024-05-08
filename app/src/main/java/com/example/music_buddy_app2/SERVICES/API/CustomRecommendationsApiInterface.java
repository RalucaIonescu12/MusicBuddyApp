package com.example.music_buddy_app2.SERVICES.API;

import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.RecommendationsRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.MyApiRecommendationsResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CustomRecommendationsApiInterface {

    @POST("/recommend")
    Call<MyApiRecommendationsResponse> getRecommendations
            ( @Query("genre") String genre,
              @Body RecommendationsRequestBody body);

}
