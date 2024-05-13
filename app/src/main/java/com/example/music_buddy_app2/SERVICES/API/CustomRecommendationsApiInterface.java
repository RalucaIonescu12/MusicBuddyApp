package com.example.music_buddy_app2.SERVICES.API;

import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.RecommendationsRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.MyApiRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.AccessTokenResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.CodeRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.USERS.RefreshTokenRequestBody;

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

    @POST("/exchange_code")
    Call<AccessTokenResponse> exchangeCode(@Body CodeRequestBody codeRequestBody);

    @POST("/refresh_token")
    Call<AccessTokenResponse> refreshToken(@Body RefreshTokenRequestBody codeRequestBody);
}
