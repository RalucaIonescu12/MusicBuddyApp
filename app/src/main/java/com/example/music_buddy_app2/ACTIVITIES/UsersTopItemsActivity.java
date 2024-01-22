package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopItemInterface;
import com.example.music_buddy_app2.API_RESPONSES.TopTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.UserResponse;

import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.ADAPTERS.TopItemsAdapter;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class UsersTopItemsActivity extends AppCompatActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;

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

        initiateSpotifyApiService();
        setupSpinner();

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerLimit = findViewById(R.id.limit);
                limit = Integer.parseInt(spinnerLimit.getSelectedItem().toString()); // Updated line

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
        int offset = 0;

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
        // Ensure the limit is between 1 and 50
        if (limit < 1 || limit > 50) {
            Toast.makeText(this, "Limit must be between 1 and 50", Toast.LENGTH_SHORT).show();
            return;
        }
        SwitchMaterial switchArtistsTracks = ((Activity) UsersTopItemsActivity.this).findViewById(R.id.switchArtistsTracks);
        if(switchArtistsTracks.isChecked()) {
            Call<TopTracksResponse> call = spotifyApiServiceInterface.getUserTopTracks(authorization, limit, offset, timeRange);
            call.enqueue(new Callback<TopTracksResponse>() {
                @Override
                public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
                    if (response.isSuccessful()) {
                        TopTracksResponse toptracksResponse = response.body();
                        if (toptracksResponse != null) {
                            List<TopTracksResponse.TopItem > receivedTopItems = toptracksResponse.getItems();

                            // Update the adapter after getting the response
                            updateAdapter(receivedTopItems);
                        }
                    } else {
                        Toast.makeText(UsersTopItemsActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UsersTopItemsActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<TopTracksResponse> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(UsersTopItemsActivity.this, "Failed top items request!", Toast.LENGTH_SHORT).show();
                    Log.e("API_FAILURE", "API call failed", t);
                    t.printStackTrace();
                }
            });
        }
        else {
            Call<TopArtistsResponse> call = spotifyApiServiceInterface.getUserTopArtists(authorization, limit, offset, timeRange);
            call.enqueue(new Callback<TopArtistsResponse>() {
                @Override
                public void onResponse(Call<TopArtistsResponse> call, Response<TopArtistsResponse> response) {
                    if (response.isSuccessful()) {
                        TopArtistsResponse TopArtistsResponse = response.body();
                        if (TopArtistsResponse != null) {
                            List< TopArtistsResponse.TopItem > receivedTopItems = TopArtistsResponse.getItems();

                            // Update the adapter after getting the response
                            updateAdapter(receivedTopItems);
                        }
                    } else {
                        Toast.makeText(UsersTopItemsActivity.this, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UsersTopItemsActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<TopArtistsResponse> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(UsersTopItemsActivity.this, "Failed top items request!", Toast.LENGTH_SHORT).show();
                    Log.e("API_FAILURE", "API call failed", t);
                    t.printStackTrace();
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
}