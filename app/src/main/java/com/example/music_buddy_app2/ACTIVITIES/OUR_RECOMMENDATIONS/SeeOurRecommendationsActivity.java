package com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.ADAPTERS.OUR_RECOMMENDATIONS.OurRecommendationsAdapter;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;
import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;

public class SeeOurRecommendationsActivity extends BaseActivity {

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
    private FloatingActionButton butonHome;
    private List<String> urisForAddToPlaylist=new ArrayList<>();
    SpotifyApiRecommendationsManager manager;
    private String spotifyUserId;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)  {
//        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
//                && keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            Log.d("MY_LOGS", "onKeyDown Called");
//            backPressed();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public void backPressed() {
//        Log.d("MY_LOGS", "onBackPressed Called");
//        startActivity(new Intent(SeeOurRecommendationsActivity.this, ChooseContextDetailsActivity.class));
//        return;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations_made_by_us);
        playlistsApiManager=PlaylistsApiManager.getInstance(this);
        userApiManager=UserApiManager.getInstance(this);
        recommendationTracks=new ArrayList<>();
        playlistImage=findViewById(R.id.playlistImage);
        descriptionTV=findViewById(R.id.playlistDesc);
        playlistNameET=findViewById(R.id.playlistTitleOurs);
        cvButtonAddQueue= findViewById(R.id.cvButtonAddQueue);
        cvAddPlaylist=findViewById(R.id.cvButtonAddPlaylist);
        plusIconImage=findViewById(R.id.plusIcon2);
        tv_addplaylist=findViewById(R.id.addplyalist_tv);
        plusIconImageQueue=findViewById(R.id.plusIcon);
        tv_addplaylistQueue=findViewById(R.id.addToQueueTV);
        descriptionTV=findViewById(R.id.playlistDesc);
        String date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        descriptionTV.setText(descriptionTV.getText());

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("MY_LOGS", "Back button pressed!");
                Intent intent = new Intent(SeeOurRecommendationsActivity.this, ChooseContextDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        butonHome=findViewById(R.id.floatingActionButton);
        butonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeeOurRecommendationsActivity.this, MenuActivity.class));
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
        int nbr = SharedPreferencesManager.getNbrGeneratedPlaylists(this);
        playlistNameET.setText("Recommendations #"+nbr);
        nbr += 1;
        SharedPreferencesManager.updateNbrPlaylists(this, nbr);
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
                    String url = "https://open.spotify.com/track/" + item.getId();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
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
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeOurRecommendationsActivity.this, "Done!" , Toast.LENGTH_SHORT).show();
                cvButtonAddQueue.setClickable(false);
                cvButtonAddQueue.setEnabled(false);
                tv_addplaylistQueue.setText("Added");
                plusIconImageQueue.setImageResource(R.drawable.baseline_check_24);
            }

            @Override
            public void onFailure(Integer number) {
                if(number==0)
                    Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to add to queue!" , Toast.LENGTH_SHORT).show();
                else {
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
               Log.e("MY_LOGS", "GOT PROFILE"+ spotifyUserId);
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
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeOurRecommendationsActivity.this, "Playlist created." , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "CREATED PLAYLIST"+ playlistName + " " + description + " "+ spotifyUserId);
                getPlaylistId(playlistName,description,spotifyUserId);
            }

            @Override
            public void onFailure(Integer number) {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to create playlist." , Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getPlaylistId(String playlistName, String description,String spotifyUserId)
    {
        int offset=0;
        int limit=50;
        Log.e("MY_LOGS", "In get playlist id: " + playlistId);
        playlistsApiManager.getPlaylistIdForUserByPlaylistNameAndDescription(spotifyUserId, offset, limit, playlistName, description, new PlaylistsApiManager.GetPlaylistIdForUserByNameAndDescriptionListener() {
            @Override
            public void onIdFound(String playlistId) {
                setPlaylistId(playlistId);
                addTracksToPlaylist(spotifyUserId);
                Log.e("MY_LOGS", "GOT PLAYOLIST ID: " + playlistId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MY_LOGS", "EROOAREEE get id for playlist"+ errorMessage);
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
        Log.e("MY_APP", "Nbr of songs: "+ urisForAddToPlaylist.size());
        for (int i = 0; i < recommendationTracks.size(); i++)
            urisForAddToPlaylist.add(prefix + recommendationTracks.get(i).getId());

        PlaylistTracksRequest request = new PlaylistTracksRequest();
        request.setUris(urisForAddToPlaylist);
        request.setPosition(0);
        playlistsApiManager.addTracksToPlaylist( playlistId, request, new PlaylistsApiManager.AddItemToQueueListener() {
            @Override
            public void onAllItemsAdded(Integer number) {
//                Toast.makeText(SeeOurRecommendationsActivity.this, "Added tracks to playlist.", Toast.LENGTH_SHORT).show();
                plusIconImage.setImageResource(R.drawable.baseline_check_24);
                updateNbrPlaylistsInFirebase();
                Log.e("MY_LOGS", "ADDED them "+ spotifyUserId);
                updatePlaylistButtonToOpenInSpotify(playlistId);

            }

            @Override
            public void onFailure(Integer number) {
                Toast.makeText(SeeOurRecommendationsActivity.this, "Failed to add tracks to playlist.", Toast.LENGTH_SHORT).show();
            }
        });

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
                } else {
                    userRef.setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to increase the value", databaseError.toException());
            }
        });
    }
}