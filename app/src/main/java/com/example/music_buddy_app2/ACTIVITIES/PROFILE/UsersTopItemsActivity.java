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

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
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

public class UsersTopItemsActivity extends BaseActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private UserApiManager userApiManager;
    TokenRefreshServiceInterface tokenRefreshServiceInterface;
    int limit;  //the limit number of songs
    int offset;  //The index of the first item to return
    Spinner spinner, spinnerLimit;


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
                    Log.e("MY_LOGS",errorMessage);
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
}