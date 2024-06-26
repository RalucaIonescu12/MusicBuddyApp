package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.SearchArtistsAdapter;
import com.example.music_buddy_app2.FRAGMENTS.ArtistDialogFragment;
import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class ChooseArtistForSpotifyRecActivity extends BaseActivity implements SearchArtistsAdapter.OnItemClickListener {
    private EditText inputSearchArtist;
    private CardView buttonSearchArtist;
    private CardView buttonNextStep;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private RecyclerView recyclerView;
    private SearchArtistsAdapter adapter;
    PlaylistsApiManager playlistsApiManager;
    private List<ArtistSearchItem> searchResults;
    private TextView noResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_artist_for_spotify_rec);

        playlistsApiManager=PlaylistsApiManager.getInstance(this);
        inputSearchArtist = findViewById(R.id.input_search_artist);
        buttonSearchArtist = findViewById(R.id.button_search_artist);
//        buttonNextStep =findViewById(R.id.button_artists_next_step);
        recyclerView = findViewById(R.id.recyclerView_search_results_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonNextStep =findViewById(R.id.button_next_step_artists);
        noResults=findViewById(R.id.no_results_text);
        searchResults = new ArrayList<>();
        initiateSpotifyApiService();

        buttonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseArtistForSpotifyRecActivity.this, FinalChangesForSpotifyRecommendationsActivity.class);
                startActivity(intent);
            }
        });
        buttonSearchArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artistNew = inputSearchArtist.getText().toString().trim();

                if (artistNew.isEmpty()) {
                    Toast.makeText(ChooseArtistForSpotifyRecActivity.this, "Please select at least one song", Toast.LENGTH_SHORT).show();
                    return;
                }
                performSearch(artistNew);
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
    public void onItemClick(ArtistSearchItem item) {
        ArtistDialogFragment dialogFragment = new ArtistDialogFragment(item, this);
        dialogFragment.show(getSupportFragmentManager(), "artist_dialog");
    }
    private void performSearch(String artistName) {
        // API call and update the RecyclerView with search results
        String q;
        q= "artist:"+ artistName;

        String type = "artist";
        int limit = 50;
        int offset = 0;
        searchResults.clear();
        playlistsApiManager.searchArtist(q,  type,  limit, offset, new PlaylistsApiManager.SearchArtistListener() {
            @Override
            public void onSuccess(List<ArtistSearchItem> searchResults) {
//                setSearchResults(searchResults);
                if (searchResults.isEmpty()) {
                    noResults.setVisibility(View.VISIBLE);
                } else {
                    setSearchResults(searchResults);
                    noResults.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MY_LOGS",errorMessage);
                Toast.makeText(ChooseArtistForSpotifyRecActivity.this, "Failed search" + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void updateRecyclerView() {
        try {
            if (adapter == null) {
                adapter = new SearchArtistsAdapter(this, searchResults, this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("MY_LOGS", "Error updating RecyclerView: " + e.getMessage(), e);
        }
    }
    public void setSearchResults(List<ArtistSearchItem> searchResults)
    {
        this.searchResults.addAll(searchResults);
        updateRecyclerView();
    }
}