package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.SearchTracksAdapter;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.FRAGMENTS.AudioFeaturesDialogFragment;
import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseTracksWithAudioFeaturesForSpotifyRecActivity extends AppCompatActivity implements SearchTracksAdapter.OnItemClickListener  {
    private EditText inputSearchSongTitle;
    private CardView buttonSearchSong;
    private PlaylistsApiManager playlistsApiManager;
    private CardView buttonNextStep;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private RecyclerView recyclerView;
    private SpotifyApiRecommendationsManager manager;
    private SearchTracksAdapter adapter;
    private List<TrackSearchItem> searchResults;
    private List<String> songSeedsForRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_track_with_audio_features_for_spotify_rec);
        playlistsApiManager=PlaylistsApiManager.getInstance(this);
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
    private void performSearch(String songTitle)
    {
        String q;
        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;
        q= "track:"+ songTitle;

        String type = "track";
        int limit = 50;
        int offset = 0;
        searchResults.clear();
        playlistsApiManager.searchArtist(q,  type,  limit, offset, new PlaylistsApiManager.SearchTracksListener() {
            @Override
            public void onSuccess(List<TrackSearchItem> searchResults) {
                setSearchResults(searchResults);
            }
            @Override
            public void onFailure(String errorMessage) {
                Log.e("FIREBASE_LOGS",errorMessage);
                Toast.makeText(ChooseTracksWithAudioFeaturesForSpotifyRecActivity.this, "Failed search" + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void setSearchResults(List<TrackSearchItem> searchResults)
    {
        this.searchResults.addAll(searchResults);
        updateRecyclerView();
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