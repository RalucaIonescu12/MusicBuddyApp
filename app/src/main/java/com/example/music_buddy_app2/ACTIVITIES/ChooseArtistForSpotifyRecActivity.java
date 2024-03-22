package com.example.music_buddy_app2.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ADAPTERS.SearchArtistsAdapter;
import com.example.music_buddy_app2.API_RESPONSES.ArtistSearchObject;
import com.example.music_buddy_app2.API_RESPONSES.SearchArtistsResponse;
import com.example.music_buddy_app2.FRAGMENTS.ArtistDialogFragment;
import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
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

public class ChooseArtistForSpotifyRecActivity extends AppCompatActivity implements SearchArtistsAdapter.OnItemClickListener {
    private EditText inputSearchArtist;
    private Button buttonSearchArtist;
    private Button buttonNextStep;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private RecyclerView recyclerView;
    private SearchArtistsAdapter adapter;
    private List<ArtistSearchItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_artist_for_spotify_rec);

        inputSearchArtist = findViewById(R.id.input_search_artist);
        buttonSearchArtist = findViewById(R.id.button_search_artist);
//        buttonNextStep =findViewById(R.id.button_artists_next_step);
        recyclerView = findViewById(R.id.recyclerView_search_results_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonNextStep =findViewById(R.id.button_next_step_artists);
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
        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;
        q= "artist:"+ artistName;

        String type = "artist";
        int limit = 50;
        int offset = 0;

        Call<SearchArtistsResponse> call= spotifyApiServiceInterface.searchArtists(autorization,q,type,limit,offset);
        searchResults.clear();
        call.enqueue(new Callback<SearchArtistsResponse>() {
            @Override
            public void onResponse(Call<SearchArtistsResponse> call, Response<SearchArtistsResponse> response) {

                if (response.isSuccessful()) {
                    SearchArtistsResponse searchArtistsResponse = response.body();
                    List<ArtistSearchObject> artistItems = searchArtistsResponse.getArtists().getItems();

                    for (ArtistSearchObject artistForSearch:artistItems)
                    {
                        ArtistSearchItem artistItem = new ArtistSearchItem();

                        artistItem.setArtistName(artistForSearch.getName());
                        artistItem.setId(artistForSearch.getId());
                        if(!artistForSearch.getImages().isEmpty())
                            artistItem.setImageResourceId(artistForSearch.getImages().get(0).getUrl());
                        else artistItem.setNoPhoto();
                        if(artistForSearch.getGenres().size()>0)
                            artistItem.setGenres(String.join(", ",artistForSearch.getGenres()));
                        else artistItem.setGenres("No information");
                        searchResults.add(artistItem);

                    }
                    updateRecyclerView();
                }
                else {
                    Toast.makeText(ChooseArtistForSpotifyRecActivity.this, "Api response for search artist not successful!" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchArtistsResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseArtistForSpotifyRecActivity.this, "Failed search artist request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call for search artists failed", t);
                t.printStackTrace();
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
            Log.e("RecyclerViewError", "Error updating RecyclerView: " + e.getMessage(), e);
            Toast.makeText(this, "Error updating RecyclerView.", Toast.LENGTH_SHORT).show();
        }
    }
}