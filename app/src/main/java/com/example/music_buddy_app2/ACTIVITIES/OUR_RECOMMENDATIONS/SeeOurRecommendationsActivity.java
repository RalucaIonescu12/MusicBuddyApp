package com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ADAPTERS.OUR_RECOMMENDATIONS.OurRecommendationsAdapter;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;
import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class SeeOurRecommendationsActivity extends AppCompatActivity {

    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    String playlistId="";
    static List<Track> recommendationTracks;
    private UserApiManager userApiManager;
    ImageView playlistImage;
    ImageView plusIconImage;
    TextView tv_addplaylist;
    ImageView plusIconImageQueue;
    TextView tv_addplaylistQueue;
    RecyclerView recyclerView;
    private PlaylistsApiManager playlistsApiManager;
    EditText playlistNameET;
    private OurRecommendationsAdapter adapter;
    private CardView cvButtonAddQueue;
    private CardView cvAddPlaylist;
    private TextView descriptionTV;
    private List<String> urisForAddToPlaylist=new ArrayList<>();
    SpotifyApiRecommendationsManager manager;
    private String spotifyUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations_made_by_us);
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

        recyclerView = findViewById(R.id.rv_tracks_our);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(manager==null)
            manager= SpotifyApiRecommendationsManager.getInstance();
        initiateSpotifyApiService();
        updateRecyclerView();
        if(recommendationTracks.isEmpty()) Toast.makeText(SeeOurRecommendationsActivity.this, "No such recommendations found!" , Toast.LENGTH_SHORT).show();
        else {
            Picasso.get().load(recommendationTracks.get(0).getImageResourceId()).into(playlistImage);
        }
        playlistNameET.setText("Recommendations");


    }
    private void updatePlaylistButtonToOpenInSpotify(String playlistId) {
        String url = "https://open.spotify.com/playlist/" + playlistId;
        tv_addplaylist.setText("Open in Spotify");
        cvAddPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    public static void setRecommendations()
    {
        recommendationTracks=new ArrayList<>();
        recommendationTracks.addAll(ChooseContextDetailsManager.getRecs());

    }
    private void updateRecyclerView() {
        setRecommendations();
        if (adapter == null) {
            adapter = new OurRecommendationsAdapter(this, recommendationTracks, new OurRecommendationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Track item) {

                }
            }
            );
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void addPlaylistItemsInQueue()
    {
        playlistsApiManager.addTracksToPlaybackQueue(recommendationTracks, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded() {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Done!" , Toast.LENGTH_SHORT).show();
                cvButtonAddQueue.setClickable(false);
                cvButtonAddQueue.setEnabled(false);
                tv_addplaylistQueue.setText("Added");
                plusIconImageQueue.setImageResource(R.drawable.baseline_check_24);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to add to queue!" , Toast.LENGTH_SHORT).show();
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
               Toast.makeText(SeeOurRecommendationsActivity.this, "You need to reauthorize!" , Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(String errorMessage) {
               Toast.makeText(SeeOurRecommendationsActivity.this, "Failed profile info request!" , Toast.LENGTH_SHORT).show();
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
            public void onAllItemsAdded() {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Playlist created." , Toast.LENGTH_SHORT).show();
                getPlaylistId(playlistName,description,spotifyUserId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to create playlist." , Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SeeOurRecommendationsActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
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
            public void onAllItemsAdded() {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Added tracks to playlist.", Toast.LENGTH_SHORT).show();
                plusIconImage.setImageResource(R.drawable.baseline_check_24);
                updatePlaylistButtonToOpenInSpotify(playlistId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to add tracks to playlist.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}