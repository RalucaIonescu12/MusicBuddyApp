package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.SearchTracksAdapter;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.AddTracksToPlaylistResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.PlaylistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SpotifyRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeeSpotifyRecommendationsActivity extends AppCompatActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    String playlistId="";
    List<TrackSearchItem> recommendationTracks;
    ImageView playlistImage;
    RecyclerView recyclerView;
    private PlaylistsApiManager playlistsApiManager;
    EditText playlistNameET;

    private SearchTracksAdapter adapter;
    private CardView cvButtonAddQueue;
    private CardView cvAddPlaylist;
    private TextView descriptionTV;
    private List<String> urisForAddToPlaylist=new ArrayList<>();
    SpotifyApiRecommendationsManager manager;
    private String spotifyUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations);
        playlistsApiManager=PlaylistsApiManager.getInstance(this);
        recommendationTracks=new ArrayList<>();
        playlistImage=findViewById(R.id.playlistImage);
        descriptionTV=findViewById(R.id.playlistDesc);
        playlistNameET=findViewById(R.id.playlistTitle);
        cvButtonAddQueue= findViewById(R.id.cvButtonAddQueue);
        cvAddPlaylist=findViewById(R.id.cvButtonAddPlaylist);

        cvButtonAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylistItemsInQueue();
            }
        });
        cvAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylistToLibrary();
            }
        });

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
        String description="Playlist generated based on ";

        if(!manager.getRecFilters().get("seed_artists").isEmpty() && manager.getRecFilters().get("seed_artists")!=null)
            for(String title:  manager.getRecFilters().get("seed_artists").keySet())
            {
                description+=title+", ";
                for(String artistId:  manager.getRecFilters().get("seed_artists").get(title).keySet())
                    seed_artists+=artistId+",";
            }
        else { Log.e("NULL_VERIFICATION", "artists null");}

        String seed_genres="";

        if(!manager.getRecFilters().get("seed_genres").isEmpty() && manager.getRecFilters().get("seed_genres")!=null)
        {
            description+="genres: ";
            for(String title:  manager.getRecFilters().get("seed_genres").keySet())
            {
                description+=title+", ";
                seed_genres+=title+",";
            }
        }
        else { Log.e("NULL_VERIFICATION", "genres null");}

        String seed_tracks= "";

        if(!manager.getRecFilters().get("seed_tracks").isEmpty() && manager.getRecFilters().get("seed_tracks")!=null) {
            description+="tracks: ";
            for (String title : manager.getRecFilters().get("seed_tracks").keySet()) {
                description += title + ", ";
                for (String trackId : manager.getRecFilters().get("seed_tracks").get(title).keySet())
                    seed_tracks += trackId + ",";
            }
        }
        else { Log.e("NULLVERIFICATION", "tracks = null"); }
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
        call.enqueue(new Callback<SpotifyRecommendationsResponse>()
        {
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
                    }
                    if(recommendationTracks.isEmpty()) Toast.makeText(SeeSpotifyRecommendationsActivity.this, "No such recommendations found!" , Toast.LENGTH_SHORT).show();
                    else{Picasso.get().load(trackItems.get(0).getAlbum().getImages().get(0).getUrl()).into(playlistImage);
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Got recs!" , Toast.LENGTH_SHORT).show();
                    updateRecyclerView();}
                }
                else {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Api response not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("AddPlaylist",String.valueOf(response.code()));
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
    private void addPlaylistItemsInQueue()
    {

        playlistsApiManager.addItemToPlaybackQueue(recommendationTracks, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded() {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Done!" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to add to queue!" , Toast.LENGTH_SHORT).show();
            }
        });

    }

   private void addPlaylistToLibrary()
   {
       //get the user id
       String accessToken = SharedPreferencesManager.getToken(this);
       String authorization = "Bearer "+ accessToken;
       Call<UserResponse> callForId= spotifyApiServiceInterface.getMyProfile(authorization);
       
       callForId.enqueue(new Callback<UserResponse>() {
           @Override
           public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

               if (response.isSuccessful()) {
                   UserResponse userResponse = response.body();
                   spotifyUserId = userResponse.getId();
                   createPlaylist(spotifyUserId);
               }
               else {
                   Toast.makeText(SeeSpotifyRecommendationsActivity.this, "You need to reauthorize!" , Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<UserResponse> call, Throwable t) {
               // Handle failure
               Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed profile info request!" , Toast.LENGTH_SHORT).show();
               t.printStackTrace();
           }
       });

   }
    public void createPlaylist(String spotifyUserId)
    {
        // create the playlist
        String accessToken = SharedPreferencesManager.getToken(this);
        String authorization = "Bearer "+ accessToken;

        String playlistName = playlistNameET.getText().toString();
        boolean _public = false;
        boolean collaborative = false;
        String description = descriptionTV.getText().toString();
        PlaylistRequestBody playlistRequestBody = new PlaylistRequestBody(playlistName,_public, collaborative,description);

        Call<Void> callForCreatePlaylist= spotifyApiServiceInterface.createPlaylistForUser(authorization, spotifyUserId,playlistRequestBody);
        callForCreatePlaylist.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful())
                {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to create playlist." , Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Playlist created." , Toast.LENGTH_SHORT).show();
                    getPlaylistId(playlistName,description,spotifyUserId);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed create playlist request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });


    }
    public void getPlaylistId(String playlistName, String description,String spotifyUserId)
    {
        //get playlist id
        String accessToken = SharedPreferencesManager.getToken(this);
        String authorization = "Bearer "+ accessToken;
        Call<PlaylistsResponse> callForGetPlaylists = spotifyApiServiceInterface.getUserPlaylists(authorization,spotifyUserId, 50, 0);
        callForGetPlaylists.enqueue(new Callback<PlaylistsResponse>() {
            @Override
            public void onResponse(Call<PlaylistsResponse> call, Response<PlaylistsResponse> response) {
                if (response.isSuccessful()) {
                    PlaylistsResponse playlistsResponse = response.body();
                    List<SimplifiedPlaylistObject> playlists = playlistsResponse.getItems();
                    for (SimplifiedPlaylistObject playlist : playlists)
                    {
                        if(playlist.getName().equals(playlistName) && playlist.getDescription().equals(description)) {
                            playlistId = playlist.getId();
                            addTracksToPlaylist(spotifyUserId);
                            break;
                        }
                    }
                } else
                {
                    int statusCode = response.code();
                    Log.e("AddPlaylist", "Failed to fetch user playlists. Status code: " + statusCode);

                }
            }
            @Override
            public void onFailure(Call<PlaylistsResponse> call, Throwable t) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed get playlist id!" , Toast.LENGTH_SHORT).show();
                Log.e("AddPlaylist", "API call failed", t);
                t.printStackTrace();
            }
        });

    }
    public void addTracksToPlaylist(String spotifyUserId)
    {
        String accessToken = SharedPreferencesManager.getToken(this);
        String authorization = "Bearer "+ accessToken;
        String prefix="spotify:track:";

        for (int i = 0; i < recommendationTracks.size(); i++)
            urisForAddToPlaylist.add(prefix + recommendationTracks.get(i).getId());

        PlaylistTracksRequest request = new PlaylistTracksRequest();
        request.setUris(urisForAddToPlaylist);
        request.setPosition(0);

        Call<AddTracksToPlaylistResponse> callForAddToPlaylist = spotifyApiServiceInterface.addTracksToPlaylist(authorization, playlistId,  request);
        callForAddToPlaylist.enqueue(new Callback<AddTracksToPlaylistResponse>() {
            @Override
            public void onResponse(Call<AddTracksToPlaylistResponse> call, Response<AddTracksToPlaylistResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Added tracks to playlist.", Toast.LENGTH_SHORT).show();
                    Log.e("AddPlaylist", "add tracks to playlist :  " + response.code() + " " + response.message());
                } else {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to add tracks to playlist.", Toast.LENGTH_SHORT).show();
                    Log.e("AddPlaylist", "Didn't work" + String.valueOf(response.code()) + response.message());
                }
            }

            @Override
            public void onFailure(Call<AddTracksToPlaylistResponse> call, Throwable t) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed put songs in playlist request!", Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });
//        urisForAddToPlaylist="";
//        for (int i = recommendationTracks.size()/2; i <recommendationTracks.size() ; i++)
//            urisForAddToPlaylist += prefix + recommendationTracks.get(i).getId() + ",";

//        urisForAddToPlaylist = urisForAddToPlaylist.substring(0, urisForAddToPlaylist.length() - 1);
//        PlaylistTracksRequest request2 = new PlaylistTracksRequest();
//        request2.setUris(urisForAddToPlaylist);
//        request2.setPosition(0);
//
//        Call<AddTracksToPlaylistResponse> callForAddToPlaylist2 = spotifyApiServiceInterface.addTracksToPlaylist(authorization, playlistId, request2.getUris(), request2);
//        callForAddToPlaylist2.enqueue(new Callback<AddTracksToPlaylistResponse>() {
//            @Override
//            public void onResponse(Call<AddTracksToPlaylistResponse> call, Response<AddTracksToPlaylistResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Added tracks to playlist.", Toast.LENGTH_SHORT).show();
//                    Log.e("AddPlaylist", "add tracks to playlist :  " + response.code() + " " + response.message());
//                } else {
//                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to add tracks to playlist.", Toast.LENGTH_SHORT).show();
//                    Log.e("AddPlaylist", "Didn't work" + String.valueOf(response.code()) + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AddTracksToPlaylistResponse> call, Throwable t) {
//                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed put songs in playlist request!", Toast.LENGTH_SHORT).show();
//                Log.e("API_FAILURE", "API call failed", t);
//                t.printStackTrace();
//            }
//        });
    }
}