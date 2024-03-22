package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.SearchTracksAdapter;
import com.example.music_buddy_app2.API_RESPONSES.ImageObject;
import com.example.music_buddy_app2.API_RESPONSES.SpotifyRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.SpotifyRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TrackObject;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeeSpotifyRecommendationsActivity extends AppCompatActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    List<TrackSearchItem> recommendationTracks;
    ImageView playlistImage;
    RecyclerView recyclerView;
    private SearchTracksAdapter adapter;
    private TextView descriptionTV;
    SpotifyApiRecommendationsManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations);
        recommendationTracks=new ArrayList<>();
        playlistImage=findViewById(R.id.playlistImage);
        descriptionTV=findViewById(R.id.playlistDesc);
        recyclerView = findViewById(R.id.rv_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(manager==null)
            manager= SpotifyApiRecommendationsManager.getInstance();
        initiateSpotifyApiService();
        getRecommendations();
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    private void getRecommendations()
    {
       // api call for recommendations
        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;
        Map<String,Double> audioFeaturesFields=manager.getAudioFeatureFields();
        String seed_artists="";
        String description="Playlist generated based on the artists: ";

        if(manager.getRecFilters().get("seed_artists")!=null)
            for(String title:  manager.getRecFilters().get("seed_artists").keySet())
            {
                description+=title+", ";
                for(String artistId:  manager.getRecFilters().get("seed_artists").get(title).keySet())
                    seed_artists+=artistId+",";
            }
        else { Log.e("NULL_VERIFICATION", "artists null"+ manager.getRecFilters().get("seed_artists")); return;}

        String seed_genres="";

        if(manager.getRecFilters().get("seed_genres")!=null)
        {
            description+="genres: ";
            for(String title:  manager.getRecFilters().get("seed_genres").keySet())
            {
                description+=title+", ";
                seed_genres+=title+",";
            }
        }
        else { Log.e("NULL_VERIFICATION", "genres null" +  manager.getRecFilters().get("seed_genres")); return;}

        String seed_tracks= "";

        if(manager.getRecFilters().get("seed_tracks")!=null) {
            description+="tracks: ";
            for (String title : manager.getRecFilters().get("seed_tracks").keySet()) {
                description += title + ", ";
                for (String trackId : manager.getRecFilters().get("seed_tracks").get(title).keySet())
                    seed_tracks += trackId + ",";
            }
        }
        else { Log.e("NULLVERIFICATION", "tracks = null" + manager.getRecFilters().get("seed_tracks")); return;}
        if(!seed_tracks.equals(""))seed_tracks=seed_tracks.substring(0,seed_tracks.length()-1);
        if(!seed_artists.equals(""))seed_artists=seed_artists.substring(0,seed_artists.length()-1);
        if(!seed_genres.equals("")) seed_genres=seed_genres.substring(0,seed_genres.length()-1);
        if(!description.equals("")) {
            description = description.substring(0, description.length() - 2);
            description+=".";
            descriptionTV.setText(description);
        }
//        Log.d("NULLVERIFICATION", "Authorization: " + autorization);
//        Log.d("NULLVERIFICATION", "Nbr Tracks: " + manager.getNbrTracks());
//        Log.d("NULLVERIFICATION", "Seed Artists: " + seed_artists);
//        Log.d("NULLVERIFICATION", "Seed Genres: " + seed_genres);
//        Log.d("NULLVERIFICATION", "Seed Tracks: " + seed_tracks);
//        Log.d("NULLVERIFICATION", "Min Acousticness: " + audioFeaturesFields.get("min_acousticness"));
//        Log.d("NULLVERIFICATION", "Max Acousticness: " + audioFeaturesFields.get("max_acousticness"));
//        Log.d("NULLVERIFICATION", "Target Acousticness: " + audioFeaturesFields.get("target_acousticness"));
//        Log.d("NULLVERIFICATION", "Min Danceability: " + audioFeaturesFields.get("min_danceability"));
//        Log.d("NULLVERIFICATION", "Max Danceability: " + audioFeaturesFields.get("max_danceability"));
//        Log.d("NULLVERIFICATION", "Target Danceability: " + audioFeaturesFields.get("target_danceability"));
//        Log.d("NULLVERIFICATION", "Min Duration MS: " + audioFeaturesFields.get("min_duration_ms").intValue());
//        Log.d("NULLVERIFICATION", "Max Duration MS: " + audioFeaturesFields.get("max_duration_ms").intValue());
//        Log.d("NULLVERIFICATION", "Target Duration MS: " + audioFeaturesFields.get("target_duration_ms").intValue());
//        Log.d("NULLVERIFICATION", "Min Energy: " + audioFeaturesFields.get("min_energy"));
//        Log.d("NULLVERIFICATION", "Max Energy: " + audioFeaturesFields.get("max_energy"));
//        Log.d("NULLVERIFICATION", "Target Energy: " + audioFeaturesFields.get("target_energy"));
//        Log.d("NULLVERIFICATION", "Min Instrumentalness: " + audioFeaturesFields.get("min_instrumentalness"));
//        Log.d("NULLVERIFICATION", "Max Instrumentalness: " + audioFeaturesFields.get("max_instrumentalness"));
//        Log.d("NULLVERIFICATION", "Target Instrumentalness: " + audioFeaturesFields.get("target_instrumentalness"));
//        Log.d("NULLVERIFICATION", "Min Key: " + audioFeaturesFields.get("min_key").intValue());
//        Log.d("NULLVERIFICATION", "Max Key: " + audioFeaturesFields.get("max_key").intValue());
//        Log.d("NULLVERIFICATION", "Target Key: " + audioFeaturesFields.get("target_key").intValue());
//        Log.d("NULLVERIFICATION", "Min Liveness: " + audioFeaturesFields.get("min_liveness"));
//        Log.d("NULLVERIFICATION", "Max Liveness: " + audioFeaturesFields.get("max_liveness"));
//        Log.d("NULLVERIFICATION", "Target Liveness: " + audioFeaturesFields.get("target_liveness"));
//        Log.d("NULLVERIFICATION", "Min Loudness: " + audioFeaturesFields.get("min_loudness"));
//        Log.d("NULLVERIFICATION", "Max Loudness: " + audioFeaturesFields.get("max_loudness"));
//        Log.d("NULLVERIFICATION", "Target Loudness: " + audioFeaturesFields.get("target_loudness"));
//        Log.d("NULLVERIFICATION", "Min Mode: " + audioFeaturesFields.get("min_mode").intValue());
//        Log.d("NULLVERIFICATION", "Max Mode: " + audioFeaturesFields.get("max_mode").intValue());
//        Log.d("NULLVERIFICATION", "Target Mode: " + audioFeaturesFields.get("target_mode").intValue());
//        Log.d("NULLVERIFICATION", "Min Popularity: " + audioFeaturesFields.get("min_popularity").intValue());
//        Log.d("NULLVERIFICATION", "Max Popularity: " + audioFeaturesFields.get("max_popularity").intValue());
//        Log.d("NULLVERIFICATION", "Target Popularity: " + audioFeaturesFields.get("target_popularity").intValue());
//        Log.d("NULLVERIFICATION", "Min Speechiness: " + audioFeaturesFields.get("min_speechiness"));
//        Log.d("NULLVERIFICATION", "Max Speechiness: " + audioFeaturesFields.get("max_speechiness"));
//        Log.d("NULLVERIFICATION", "Target Speechiness: " + audioFeaturesFields.get("target_speechiness"));
//        Log.d("NULLVERIFICATION", "Min Tempo: " + audioFeaturesFields.get("min_tempo"));
//        Log.d("NULLVERIFICATION", "Max Tempo: " + audioFeaturesFields.get("max_tempo"));
//        Log.d("NULLVERIFICATION", "Target Tempo: " + audioFeaturesFields.get("target_tempo"));
//        Log.d("NULLVERIFICATION", "Min Time Signature: " + audioFeaturesFields.get("min_time_signature").intValue());
//        Log.d("NULLVERIFICATION", "Max Time Signature: " + audioFeaturesFields.get("max_time_signature").intValue());
//        Log.d("NULLVERIFICATION", "Target Time Signature: " + audioFeaturesFields.get("target_time_signature").intValue());
//        Log.d("NULLVERIFICATION", "Min Valence: " + audioFeaturesFields.get("min_valence"));
//        Log.d("NULLVERIFICATION", "Max Valence: " + audioFeaturesFields.get("max_valence"));

        Call<SpotifyRecommendationsResponse> call= spotifyApiServiceInterface.getRecommendations(
                autorization,
                manager.getNbrTracks(),
                seed_artists,
                seed_genres,
                seed_tracks,
                audioFeaturesFields.get("min_acousticness"),
                audioFeaturesFields.get("max_acousticness"),
                audioFeaturesFields.get("target_acousticness"),
                audioFeaturesFields.get("min_danceability"),
                audioFeaturesFields.get("max_danceability"),
                audioFeaturesFields.get("target_danceability"),
                audioFeaturesFields.get("min_duration_ms").intValue(),
                audioFeaturesFields.get("max_duration_ms").intValue(),
                audioFeaturesFields.get("target_duration_ms").intValue(),
                audioFeaturesFields.get("min_energy"),
                audioFeaturesFields.get("max_energy"),
                audioFeaturesFields.get("target_energy"),
                audioFeaturesFields.get("min_instrumentalness"),
                audioFeaturesFields.get("max_instrumentalness"),
                audioFeaturesFields.get("target_instrumentalness"),
                audioFeaturesFields.get("min_key").intValue(),
                audioFeaturesFields.get("max_key").intValue(),
                audioFeaturesFields.get("target_key").intValue(),
                audioFeaturesFields.get("min_liveness"),
                audioFeaturesFields.get("max_liveness"),
                audioFeaturesFields.get("target_liveness"),
                audioFeaturesFields.get("min_loudness"),
                audioFeaturesFields.get("max_loudness"),
                audioFeaturesFields.get("target_loudness"),
                audioFeaturesFields.get("min_mode").intValue(),
                audioFeaturesFields.get("max_mode").intValue(),
                audioFeaturesFields.get("target_mode").intValue(),
                audioFeaturesFields.get("min_popularity").intValue(),
                audioFeaturesFields.get("max_popularity").intValue(),
                audioFeaturesFields.get("target_popularity").intValue(),
                audioFeaturesFields.get("min_speechiness"),
                audioFeaturesFields.get("max_speechiness"),
                audioFeaturesFields.get("target_speechiness"),
                audioFeaturesFields.get("min_tempo"),
                audioFeaturesFields.get("max_tempo"),
                audioFeaturesFields.get("target_tempo"),
                audioFeaturesFields.get("min_time_signature").intValue(),
                audioFeaturesFields.get("max_time_signature").intValue(),
                audioFeaturesFields.get("target_time_signature").intValue(),
                audioFeaturesFields.get("min_valence"),
                audioFeaturesFields.get("max_valence"),
                audioFeaturesFields.get("target_valence")
                );
        call.enqueue(new Callback<SpotifyRecommendationsResponse>() {
            @Override
            public void onResponse(Call<SpotifyRecommendationsResponse> call, Response<SpotifyRecommendationsResponse> response) {

                if (response.isSuccessful()) {

                    SpotifyRecommendationsResponse SpotifyRecommendationsResponse = response.body();
                    List<TrackObject> trackItems = SpotifyRecommendationsResponse.getTracks();

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
                        recommendationTracks.add(trackForSearch);
                        Log.e("NULLVERIFICATION",recommendationTracks.toString());
                    }
                    if(recommendationTracks.isEmpty()) Toast.makeText(SeeSpotifyRecommendationsActivity.this, "No such recommendations found!" , Toast.LENGTH_SHORT).show();
                    else{Picasso.get().load(trackItems.get(0).getAlbum().getImages().get(0).getUrl()).into(playlistImage);
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Got recs!" , Toast.LENGTH_SHORT).show();
                    updateRecyclerView();}
                }
                else {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Api response not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("Response",response.toString());
                }
            }

            @Override
            public void onFailure(Call<SpotifyRecommendationsResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed recommendation request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });
    }
    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new SearchTracksAdapter(this, recommendationTracks,  new SearchTracksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(TrackSearchItem item) {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this,"clicked song",Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }
}