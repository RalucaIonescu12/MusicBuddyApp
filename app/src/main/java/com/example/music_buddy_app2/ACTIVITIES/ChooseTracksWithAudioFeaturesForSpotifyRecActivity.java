package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.SearchTracksAdapter;
import com.example.music_buddy_app2.API_RESPONSES.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TrackObject;
import com.example.music_buddy_app2.FRAGMENTS.AudioFeaturesDialogFragment;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseTracksWithAudioFeaturesForSpotifyRecActivity extends AppCompatActivity implements SearchTracksAdapter.OnItemClickListener  {
    private EditText inputSearchSongTitle;
    private Button buttonSearchSong;
    private Button buttonNextStep;

    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private RecyclerView recyclerView;
    private SpotifyApiRecommendationsManager manager;
    private SearchTracksAdapter adapter;
    private List<TrackSearchItem> searchResults;
    private List<String> songSeedsForRec;

//    TODO: ADD POP UP FOR THE FIRST TIME THE USER USES RECOMMENDATIONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_track_with_audio_features_for_spotify_rec);

        inputSearchSongTitle = findViewById(R.id.input_search_song_title);
        buttonSearchSong = findViewById(R.id.button_search_song);
        buttonNextStep =findViewById(R.id.button_next_step);
        recyclerView = findViewById(R.id.recyclerView_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();
        songSeedsForRec = new ArrayList<>();
        initiateSpotifyApiService();
        if(manager==null)
            manager=SpotifyApiRecommendationsManager.getInstance();
        buttonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.setTargetAudioFeatures();
                Intent intent = new Intent(ChooseTracksWithAudioFeaturesForSpotifyRecActivity.this, ChooseArtistForSpotifyRecActivity.class);
                startActivity(intent);
            }
        });
        buttonSearchSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songTitleNew = inputSearchSongTitle.getText().toString().trim();

                if (songTitleNew.isEmpty()) {
                    Toast.makeText(ChooseTracksWithAudioFeaturesForSpotifyRecActivity.this, "Forgot to mention a title", Toast.LENGTH_SHORT).show();
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
                    List<TrackObject> trackItems = searchTrackResponse.getTracks().getItems();

                    for (TrackObject trackItem : trackItems) {
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
                    updateRecyclerView();
                }
                else {
                    Toast.makeText(ChooseTracksWithAudioFeaturesForSpotifyRecActivity.this, "Api response for search song not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("Response",response.toString());
                }
            }

            @Override
            public void onFailure(Call<SearchTrackResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseTracksWithAudioFeaturesForSpotifyRecActivity.this, "Failed search song request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call for song search failed", t);
                t.printStackTrace();
            }
        });

    }
    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new SearchTracksAdapter(this, searchResults, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}