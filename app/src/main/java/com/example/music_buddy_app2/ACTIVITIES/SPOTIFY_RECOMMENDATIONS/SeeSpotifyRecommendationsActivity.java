package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS.SeeOurRecommendationsActivity;
import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.SearchTracksAdapter;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SpotifyRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeeSpotifyRecommendationsActivity extends BaseActivity {
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    String playlistId="";
    List<TrackSearchItem> recommendationTracks;
    private UserApiManager userApiManager;

    ImageView playlistImage;
    ImageView plusIconImage;
    TextView tv_addplaylist;
    ImageView plusIconImageQueue;
    TextView tv_addplaylistQueue;
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
    private FloatingActionButton butonHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations);
        playlistsApiManager=PlaylistsApiManager.getInstance(this);
        userApiManager=UserApiManager.getInstance(this);
        recommendationTracks=new ArrayList<>();
        playlistImage=findViewById(R.id.playlistImage);
        descriptionTV=findViewById(R.id.playlistDesc);
        playlistNameET=findViewById(R.id.playlistTitle);
        cvButtonAddQueue= findViewById(R.id.cvButtonAddQueue);
        cvAddPlaylist=findViewById(R.id.cvButtonAddPlaylist);
        plusIconImage=findViewById(R.id.plusIcon2);
        tv_addplaylist=findViewById(R.id.addplyalist_tv);
        plusIconImageQueue=findViewById(R.id.plusIcon);
        tv_addplaylistQueue=findViewById(R.id.addToQueueTV);

        butonHome=findViewById(R.id.floatingActionButton);
        butonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeeSpotifyRecommendationsActivity.this, MenuActivity.class));
            }
        });

        cvButtonAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_addplaylistQueue.setText("Loading...");
                cvButtonAddQueue.setEnabled(false);
                cvButtonAddQueue.setClickable(false);
                addPlaylistItemsInQueue();
            }
        });
        cvAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_addplaylist.setText("Loading...");
                cvAddPlaylist.setEnabled(false);
                cvAddPlaylist.setClickable(false);
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
    private void updatePlaylistButtonToOpenInSpotify(String playlistId) {
        String url = "https://open.spotify.com/playlist/" + playlistId;
        tv_addplaylist.setText("Open in Spotify");
        cvAddPlaylist.setEnabled(true);
        cvAddPlaylist.setClickable(true);
        cvAddPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
    private void getRecommendations()
    {
       // api call for recommendations
//        Log.e("MY_APP", String.valueOf(SharedPreferencesManager.getNbrGeneratedPlaylists(this)));
        int nbr = SharedPreferencesManager.getNbrGeneratedPlaylists(this)+1;
        playlistNameET.setText("Recommendations #"+nbr);
        nbr += 1;


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
        else { Log.e("MY_LOGS", "See recs : artists null");}

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
        else { Log.e("MY_LOGS", "See recs : genres null");}

        String seed_tracks= "";

        if(!manager.getRecFilters().get("seed_tracks").isEmpty() && manager.getRecFilters().get("seed_tracks")!=null) {
            description+="tracks: ";
            for (String title : manager.getRecFilters().get("seed_tracks").keySet()) {
                description += title + ", ";
                for (String trackId : manager.getRecFilters().get("seed_tracks").get(title).keySet())
                    seed_tracks += trackId + ",";
            }
        }
        else { Log.e("MY_LOGS", " See recs : tracks = null"); }
        if(!seed_tracks.equals(""))seed_tracks=seed_tracks.substring(0,seed_tracks.length()-1);
        if(!seed_artists.equals(""))seed_artists=seed_artists.substring(0,seed_artists.length()-1);
        if(!seed_genres.equals("")) seed_genres=seed_genres.substring(0,seed_genres.length()-1);
        if(!description.equals("")) {
            description = description.substring(0, description.length() - 2);
            description+=".";
            descriptionTV.setText(description);
        }

        Call<SpotifyRecommendationsResponse> call= spotifyApiServiceInterface.getRecommendations(
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
//                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Got recs!" , Toast.LENGTH_SHORT).show();
                    updateRecyclerView();}
                }
                else {
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Api response not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("MY_LOGS",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SpotifyRecommendationsResponse> call, Throwable t) {

                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed recommendation request!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed for recs request from spotify api", t);
                t.printStackTrace();
            }
        });
    }
    private void updateRecyclerView() {
        if (adapter == null) {

            adapter = new SearchTracksAdapter(this, recommendationTracks,  new SearchTracksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(TrackSearchItem item) {
                    String url = "https://open.spotify.com/track/" + item.getId();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void addPlaylistItemsInQueue()
    {

        playlistsApiManager.addItemsToPlaybackQueue(recommendationTracks, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Done!" , Toast.LENGTH_SHORT).show();
                cvButtonAddQueue.setClickable(false);
                cvButtonAddQueue.setEnabled(false);
                tv_addplaylistQueue.setText("Added");
                plusIconImageQueue.setImageResource(R.drawable.baseline_check_24);
            }

            @Override
            public void onFailure(Integer number) {
                if(number==0){
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to add to queue! Try later" , Toast.LENGTH_SHORT).show();
                    cvButtonAddQueue.setClickable(false);
                    cvButtonAddQueue.setEnabled(false);
                    tv_addplaylistQueue.setText("Retry later");}
                else{
                    Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Done!" , Toast.LENGTH_SHORT).show();
                    cvButtonAddQueue.setClickable(false);
                    cvButtonAddQueue.setEnabled(false);
                    tv_addplaylistQueue.setText("Added");
                    plusIconImageQueue.setImageResource(R.drawable.baseline_check_24);
                }
            }
        });

    }

   private void addPlaylistToLibrary()
   {
       //get the user id
       userApiManager.getProfile(new UserApiManager.UserApiListener() {
           @Override
           public void onProfileReceived(User user) {
               spotifyUserId = user.getSpotifyId();
               createPlaylist(spotifyUserId);
           }

           @Override
           public void onAuthorizationError() {
               Toast.makeText(SeeSpotifyRecommendationsActivity.this, "You need to reauthorize!" , Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(String errorMessage) {
               Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed profile info request!" , Toast.LENGTH_SHORT).show();
           }
       });

   }
    public void createPlaylist(String spotifyUserId)
    {
        // create the playlist

        String playlistName = playlistNameET.getText().toString();
        boolean _public = false;
        boolean collaborative = false;
        String description = descriptionTV.getText().toString();
        playlistsApiManager.createPlaylistForUser(spotifyUserId, playlistName, _public, collaborative, description, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Playlist created." , Toast.LENGTH_SHORT).show();
                getPlaylistId(playlistName,description,spotifyUserId);
            }

            @Override
            public void onFailure(Integer number) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to create playlist." , Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getPlaylistId(String playlistName, String description,String spotifyUserId)
    {
        int offset=0;
        int limit=50;

        playlistsApiManager.getPlaylistIdForUserByPlaylistNameAndDescription(spotifyUserId, offset, limit, playlistName, description, new PlaylistsApiManager.GetPlaylistIdForUserByNameAndDescriptionListener() {
            @Override
            public void onIdFound(String playlistId) {
                setPlaylistId(playlistId);
                addTracksToPlaylist(spotifyUserId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void setPlaylistId(String playlistId)
    {
        this.playlistId=playlistId;
    }
    public void addTracksToPlaylist(String spotifyUserId)
    {
        String prefix="spotify:track:";

        for (int i = 0; i < recommendationTracks.size(); i++)
            urisForAddToPlaylist.add(prefix + recommendationTracks.get(i).getId());


        PlaylistTracksRequest request = new PlaylistTracksRequest();
        request.setUris(urisForAddToPlaylist);
        request.setPosition(0);
        playlistsApiManager.addTracksToPlaylist( playlistId, request, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Added tracks to playlist.", Toast.LENGTH_SHORT).show();
//                cvAddPlaylist.setClickable(false);
//                cvAddPlaylist.setEnabled(false);
//                tv_addplaylist.setText("Added");
                updateNbrPlaylistsInFirebase();

            }

            @Override
            public void onFailure(Integer number) {
                Toast.makeText(SeeSpotifyRecommendationsActivity.this, "Failed to add tracks to playlist.", Toast.LENGTH_SHORT).show();
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
    public void updateNbrPlaylistsInFirebase(){
        String userId = SharedPreferencesManager.getUserId(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(userId).child("playlistsCreatedWithTheApp");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long currentCount = dataSnapshot.getValue(Long.class);
                    userRef.setValue(currentCount + 1);
                    SharedPreferencesManager.updateNbrPlaylists(SeeSpotifyRecommendationsActivity.this,(int)currentCount+1);
                    plusIconImage.setImageResource(R.drawable.baseline_check_24);
                    updatePlaylistButtonToOpenInSpotify(playlistId);
                } else {
                    userRef.setValue(1);
                    plusIconImage.setImageResource(R.drawable.baseline_check_24);
                    updatePlaylistButtonToOpenInSpotify(playlistId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to increase the value", databaseError.toException());
            }
        });
    }
}