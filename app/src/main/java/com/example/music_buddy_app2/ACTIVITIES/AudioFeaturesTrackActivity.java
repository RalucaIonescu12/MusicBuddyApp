package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.SearchResultsAdapter;
import com.example.music_buddy_app2.API_RESPONSES.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.UserResponse;
import com.example.music_buddy_app2.FRAGMENTS.AudioFeaturesDialogFragment;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class AudioFeaturesTrackActivity extends AppCompatActivity implements SearchResultsAdapter.OnItemClickListener  {
    private EditText inputSearchSongTitle;
    private Button buttonSearchSong;


    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;
    private List<TrackSearchItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_features_track);

        inputSearchSongTitle = findViewById(R.id.input_search_song_title);
        buttonSearchSong = findViewById(R.id.button_search_song);

        recyclerView = findViewById(R.id.recyclerView_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();
        initiateSpotifyApiService();

        buttonSearchSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songTitleNew = inputSearchSongTitle.getText().toString().trim();
                //check if the song title is empty
                if (songTitleNew.isEmpty()) {
                    Toast.makeText(AudioFeaturesTrackActivity.this, "Please enter a song title", Toast.LENGTH_SHORT).show();
                    return;
                }

                performSearch(songTitleNew);
            }
        });
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    @Override
    public void onItemClick(TrackSearchItem item) {
        AudioFeaturesDialogFragment dialogFragment = new AudioFeaturesDialogFragment(item, this);
        dialogFragment.show(getSupportFragmentManager(), "audio_features_dialog");
    }
    private void performSearch(String songTitle) {
        // API call and update the RecyclerView with search results
        String q;
        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;
        q= "track:"+ songTitle;

        String type = "track";
        int limit = 50;
        int offset = 0;

        Call<SearchTrackResponse> call= spotifyApiServiceInterface.searchTracks(autorization,q,type,limit,offset);
        searchResults.clear();
        call.enqueue(new Callback<SearchTrackResponse>() {
            @Override
            public void onResponse(Call<SearchTrackResponse> call, Response<SearchTrackResponse> response) {

                if (response.isSuccessful()) {

                    SearchTrackResponse searchTrackResponse = response.body();
                    List<SearchTrackResponse.TrackItem> trackItems = searchTrackResponse.getTracks().getItems();

                    for (SearchTrackResponse.TrackItem trackItem : trackItems) {
                        TrackSearchItem trackForSearch = new TrackSearchItem();
                        trackForSearch.setSongName(trackItem.getName());
                        trackForSearch.setId(trackItem.getId());
                        trackForSearch.setImageResourceId(trackItem.getAlbum().getImages().get(0).getUrl());
                        StringBuilder artistsNames = new StringBuilder();
                        List<TopArtistsResponse.TopItem> artists = trackItem.getArtists();

                        for (int i = 0; i < artists.size(); i++) {
                            artistsNames.append(artists.get(i).getName());
                            if (i < artists.size() - 1) {
                                artistsNames.append(", ");
                            }
                        }
                        trackForSearch.setArtistName(artistsNames.toString());
                        searchResults.add(trackForSearch);
                    }
                    Toast.makeText(AudioFeaturesTrackActivity.this, "worked!" , Toast.LENGTH_SHORT).show();
                    updateRecyclerView();
                }
                else {
                    Toast.makeText(AudioFeaturesTrackActivity.this, "Api response not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("Response",response.toString());
                }
            }

            @Override
            public void onFailure(Call<SearchTrackResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(AudioFeaturesTrackActivity.this, "Failed search request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });

    }
    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new SearchResultsAdapter(this, searchResults, this);

            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}