package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.INTERFACES.TopItemInterface;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TopTracksResponse;

import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.ADAPTERS.USERS.TopItemsAdapter;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.TokenRefreshServiceInterface;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersTopItemsActivity extends AppCompatActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private UserApiManager userApiManager;
    TokenRefreshServiceInterface tokenRefreshServiceInterface;
    int limit;  //the limit number of songs
    int offset;  //The index of the first item to return
    Spinner spinner, spinnerLimit;

//    Call<TopArtistsResponse> getUserTopTracks(
//            @Header("Authorization") String token,
//            @Query("limit") int limit,
//            @Query("offset") int offset,
//            @Query("time_range") String timeRange
    String timeRange; //long_term = all time, medium_term = last 6 months, short_term = 4 weeks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_top_items);

        userApiManager=UserApiManager.getInstance(this);
        initiateSpotifyApiService();
        setupSpinner();

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerLimit = findViewById(R.id.limit);
                limit = Integer.parseInt(spinnerLimit.getSelectedItem().toString());

                String selectedTimeRange = spinner.getSelectedItem().toString();

                switch (selectedTimeRange) {
                    case "1 month":
                        timeRange = "short_term";
                        break;
                    case "all time":
                        timeRange = "long_term";
                        break;
                    default:
                        timeRange = "medium_term";
                }

                if (limit < 1 || limit > 50) {
                    Toast.makeText(UsersTopItemsActivity.this, "Limit must be between 1 and 50", Toast.LENGTH_SHORT).show();
                } else {
                    getTopItems();
                }
            }
        });
        getTopItems();
    }
    private void setupSpinner() {
        spinner = findViewById(R.id.spinnerTimeRange);
        spinnerLimit = findViewById(R.id.limit);

        String[] timeRangeOptions = {"1 month", "6 months", "all time"};
        String[] limitOptions = {"5", "10", "15", "20", "30", "40", "50"};

        ArrayAdapter<String> timeRangeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeRangeOptions);
        ArrayAdapter<String> limitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, limitOptions);

        timeRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        limitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(timeRangeAdapter);
        spinnerLimit.setAdapter(limitAdapter);
    }
    public void initiateSpotifyApiService() {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }


    private void getTopItems() {
        String accessToken = SharedPreferencesManager.getToken(this);
        String authorization = "Bearer " + accessToken;
        this.offset = 0;

        limit = Integer.parseInt(spinnerLimit.getSelectedItem().toString());

        String selectedTimeRange = spinner.getSelectedItem().toString();

        switch (selectedTimeRange) {
            case "1 month":
                timeRange = "short_term";
                break;
            case "all time":
                timeRange = "long_term";
                break;
            default:
                timeRange = "medium_term";
        }
        if (limit < 1 || limit > 50) {
            Toast.makeText(this, "Limit must be between 1 and 50", Toast.LENGTH_SHORT).show();
            return;
        }
        SwitchMaterial switchArtistsTracks = ((Activity) UsersTopItemsActivity.this).findViewById(R.id.switchArtistsTracks);
        if(switchArtistsTracks.isChecked())
        {
            userApiManager.getUsersTopTracks( this.limit, this.offset, this.timeRange,new UserApiManager.TopTracksApiListener() {
                @Override
                public void onItemsReceived(List<TopTracksResponse.TopItem> receivedTopItems) {
                    updateAdapter(receivedTopItems);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("FIREBASE_LOGS",errorMessage);
                    Toast.makeText(UsersTopItemsActivity.this, "Failed to get items" + errorMessage, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onAuthorizationError()
                {
                    Toast.makeText(UsersTopItemsActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            userApiManager.getUsersTopArtists( this.limit, this.offset, this.timeRange,new UserApiManager.TopItemsApiListener() {
                @Override
                public void onItemsReceived(List<TopArtistsResponse.TopItem> receivedTopItems) {
                    updateAdapter(receivedTopItems);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("FIREBASE_LOGS",errorMessage);
                    Toast.makeText(UsersTopItemsActivity.this, "Failed to get items" + errorMessage, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onAuthorizationError()
                {
                    Toast.makeText(UsersTopItemsActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateAdapter(final List<? extends TopItemInterface> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GridView gridView = findViewById(R.id.gridView);
                TopItemsAdapter adapter = new TopItemsAdapter(UsersTopItemsActivity.this, items);
                gridView.setAdapter(adapter);
            }
        });
    }
//
// private void getRetrofitRefreshToken()
//    {
//// @POST("api/token")
////     Call<AccessTokenResponse> refreshAccessToken(
////         @Field("grant_type") String grantType,
////         @Field("refresh_token") String refreshToken,
////         @Header("Content-Type") String contentType,
////         @Header("Authorization") String clientCredentials, ///the format: Authorization: Basic <base64 encoded client_id:client_secret
////         @Header("client_id") String clientId
////    );
//
//     String refreshToken = SharedPreferencesManager.getToken(this);
////     String clientCredentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET);
//     String clientCredentials = "Basic " + Base64.encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.DEFAULT);
//
//     String contentType = "application/x-www-form-urlencoded";
//
//     Call<AccessTokenResponse> callback= tokenRefreshServiceInterface.refreshAccessToken("refresh_token", refreshToken ,contentType,clientCredentials,CLIENT_ID );
//     Log.d("TokenRefresh", "call  " + callback.isExecuted()) ;
//     callback.enqueue(new Callback<AccessTokenResponse>() {
//         @Override
//         public void onResponse(Call<AccessTokenResponse> call, retrofit2.Response<AccessTokenResponse> response) {
//             Log.d("TokenRefresh", "Response Code: " + response.code());
//             Log.d("TokenRefresh", "Response Body: " + response.body());
//             if (response.isSuccessful()) {
//
//                 AccessTokenResponse accessTokenResponse = response.body();
//                 if (accessTokenResponse != null) {
//                     String newAccessToken = accessTokenResponse.getAccessToken();
//                     Log.d("TokenRefresh", "Response Body:  is successfulkl" + response.body());
//                     // Save the new access token using SharedPreferencesManager
//                     SharedPreferencesManager.saveToken(UsersTopItemsActivity.this, newAccessToken);
//                     Toast.makeText(UsersTopItemsActivity.this, "Refreshed!",Toast.LENGTH_SHORT).show();
//                     getTopItems();
//                 }
//             } else {
//                 Log.d("TokenRefresh", "Response Body: else " + response.body());
//                 Toast.makeText(UsersTopItemsActivity.this, "Not successful at refreshing token! ",Toast.LENGTH_SHORT).show();
////                 Intent intent = new Intent(UsersTopItemsActivity.this, WelcomeActivity.class);
////                       startActivity(intent);
//             }
//         }
//
//         @Override
//         public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
//             Toast.makeText(UsersTopItemsActivity.this, "tried but Failed to refresh token: " + t.getMessage(), Toast.LENGTH_SHORT).show();
////             Intent intent = new Intent(UsersTopItemsActivity.this, WelcomeActivity.class);
////             startActivity(intent);
//         }
//     });
//
// }
}