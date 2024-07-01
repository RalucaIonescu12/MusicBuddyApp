package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class UserProfileActivity  extends BaseActivity {
    private ImageView profilePicture;
    private TextView username;
    private CardView spotifyIcon;
    private String currentUserId;
    private UserManager userManager;
    String userId;
    List<SimplifiedPlaylistObject> allPlaylistsCurrentUser,allPlaylistsOtherUser;
    private PlaylistsApiManager playlistsApiManager;
    private Set<TrackObject> commonTracks;
    private  Set<TrackObject> otherTracks;
    private Set<TrackObject> currentdTracks;
    private CardView allCommonSongsCV;
    private static TextView friendStatus;
    private CardView followButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        friendStatus = findViewById(R.id.follow_status);
        profilePicture = findViewById(R.id.profile_picture);
        username = findViewById(R.id.username);
        spotifyIcon = findViewById(R.id.goToSpotifyCV);
        allPlaylistsCurrentUser = new ArrayList<>();
        allPlaylistsOtherUser  =new ArrayList<>();
        followButton=findViewById(R.id.card_top_items);
        currentdTracks = new HashSet<>();
        otherTracks = new HashSet<>();
        commonTracks = new HashSet<>();
        userId = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");
        String userProfileImage = getIntent().getStringExtra("user_profile_image");
        String uri=getIntent().getStringExtra("uri");
        String nbr_playlists=getIntent().getStringExtra("nbr_playlists");
        String status=getIntent().getStringExtra("friendStatus");
        playlistsApiManager = PlaylistsApiManager.getInstance(this);
        currentUserId = SharedPreferencesManager.getUserId(this);
        username.setText(userName);
        userManager=UserManager.getInstance(UserProfileActivity.this);
        Picasso.get().load(userProfileImage).into(profilePicture);

        if(FindFriendsActivity.friendsUsers.contains(userId))
            friendStatus.setText("Friends");
        else if(status.equals("all"))
                friendStatus.setText("Follow");
        else if(status.equals("following"))
            friendStatus.setText("Following");
        else if(status.equals("followers"))
            friendStatus.setText("Follows you");

        allCommonSongsCV=findViewById(R.id.allCommonSongsCV);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userr=new User();
                userr.setSpotifyId(userId);
                String s= friendStatus.getText().toString();
                if ( s.equals("Follow"))
                {
                    userManager.followUser(userr);
                    friendStatus.setText("Following");
                }
                else if(s.equals("Follows you"))
                {
                    userManager.followUser(userr);
                    friendStatus.setText("Friends");
                }
            }
        });

        spotifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.spotify.music");
                startActivity(intent);
            }
        });
        allCommonSongsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                getUsersPlaylists();
//                getOtherUsersPlaylists();
            }
        });
    }
    private void getUsersPlaylists() {
        int offsets[]={0,50,100};
        Log.e("MY_LOGS","FETCH CURRENT USER PLAYLISTS");
        AtomicInteger remainingOffsets = new AtomicInteger(offsets.length);
        allPlaylistsCurrentUser.clear();
        for(int offset:offsets)
        {
            playlistsApiManager.getPlaylistsForCurrentUser(offset,
                    new PlaylistsApiManager.PlaylistsCallback() {
                        @Override
                        public void onSuccess(List<SimplifiedPlaylistObject> playlists) {
                            Log.e("MY_LOGS", "FETCHED AGAIN:" +  String.valueOf(playlists.size()));
                            allPlaylistsCurrentUser.addAll(playlists);

                            if (remainingOffsets.decrementAndGet() == 0) {
                                getOtherUsersPlaylists();
                                Log.e("MY_LOGS", "GOT ALL FOR CURRENT :" +allPlaylistsCurrentUser.size());
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(UserProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.e("MY_LOGS", "ERROR LA _PLAHYLISTS:" );
                        }
                    });
        }

    }

    public void getOtherUsersPlaylists() {
        int offsets[]={0,50,100,150};
        allPlaylistsOtherUser.clear();
        AtomicInteger remainingOffsets = new AtomicInteger(offsets.length);
        Log.e("MY_LOGS","FETCH OTHER USER PLAYLISTS");
        for(int offset:offsets) {
            playlistsApiManager.getPlaylistsForUser(userId, offset, new PlaylistsApiManager.PlaylistsCallback() {
                @Override
                public void onSuccess(List<SimplifiedPlaylistObject> playlists) {
                    allPlaylistsOtherUser.addAll(playlists);
                        Log.e("MY_LOGS", "FETCHED AGAIN:" +  String.valueOf(playlists.size()));
                        if (remainingOffsets.decrementAndGet() == 0) {
                            getAllItemsCurrent();
                            Log.e("MY_LOGS", "GOT ALL FOR OTHER. TOTAL: "+ allPlaylistsOtherUser.size() );
                        }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(UserProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("MY_LOGS", "ERROR LA _PLAHYLISTS:" );
                }
            });
        }

    }

    static void setFriendshipStatus(String status)
    {
        friendStatus.setText(status);
    }
    public void getAllItemsCurrent()
    {
        //get playlist items for each playlist
        currentdTracks.clear();
        AtomicInteger processedPlaylists = new AtomicInteger(0);
        Log.e("MY_LOGS", "GETY SONGS " );
        for(SimplifiedPlaylistObject playlistObject: allPlaylistsCurrentUser) {
            Integer nbrSongsInPlaylist = playlistObject.getTracks().getTotal();

            int offset = 0;
            int limit = 50;
            String id = playlistObject.getId();
            do
            {
                playlistsApiManager.getSimplePlaylistItems(offset, limit, id, new PlaylistsApiManager.GetPlaylistItemsSimpleListener() {
                    @Override
                    public void onSimpleItemsReceived(Set<TrackObject> receivedTracks) {
                        currentdTracks.addAll(receivedTracks);
                        Log.e("MY_LOGS", " added " + String.valueOf(receivedTracks.size())+" songs");
                        if (processedPlaylists.incrementAndGet() == allPlaylistsCurrentUser.size()) {
//                            listener.onAllSongsReceived(commonTracks);
//                            showSongs();
                            getAllItemsOther();
                            Log.e("MY_LOGS", "GOT  SONGS ALL FOR OTHER. TOTAL: "+ currentdTracks.size() );
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(UserProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                        listener.onError("Failed to fetch songs for playlist "+ playlistObject.getName());

                    }
                });

                offset+=50;
                nbrSongsInPlaylist-=50;
                Log.e("MY_LOGS", String.valueOf(nbrSongsInPlaylist));

            } while (nbrSongsInPlaylist>0);

        }
    }
    public void getAllItemsOther()
    {
        //get playlist items for each playlist
        otherTracks.clear();
        AtomicInteger processedPlaylists = new AtomicInteger(0);
        Log.e("MY_LOGS", "GET SONGS " );
        for(SimplifiedPlaylistObject playlistObject: allPlaylistsOtherUser) {
            Integer nbrSongsInPlaylist = playlistObject.getTracks().getTotal();

            int offset = 0;
            int limit = 50;
            String id = playlistObject.getId();
            do
            {
                playlistsApiManager.getSimplePlaylistItems(offset, limit, id, new PlaylistsApiManager.GetPlaylistItemsSimpleListener() {
                    @Override
                    public void onSimpleItemsReceived(Set<TrackObject> receivedTracks) {
                        otherTracks.addAll(receivedTracks);
                        Log.e("MY_LOGS", " added " + String.valueOf(receivedTracks.size())+" songs");
                        if (processedPlaylists.incrementAndGet() == allPlaylistsOtherUser.size()) {
//                            listener.onAllSongsReceived(commonTracks);
//                            showSongs();
                            otherTracks.retainAll(currentdTracks);
                            showSongs();
                            Log.e("MY_LOGS", "GOT  COMMON . TOTAL: "+ otherTracks.size() );
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(UserProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                        listener.onError("Failed to fetch songs for playlist "+ playlistObject.getName());

                    }
                });

                offset+=50;
                nbrSongsInPlaylist-=50;
                Log.e("MY_LOGS", String.valueOf(nbrSongsInPlaylist));

            } while (nbrSongsInPlaylist>0);

        }
    }
    private void showSongs() {
        List<Track> commonTrackList = new ArrayList<>();
        for (TrackObject trackObject : commonTracks) {
            List<String> artists= new ArrayList<>();
            for(int i =0 ; i<trackObject.getArtists().size();i++)
                artists.add(trackObject.getArtists().get(i).getName());
            commonTrackList.add(new Track(trackObject.getId(), trackObject.getName(),artists,trackObject.getAlbum().getImages().get(0).getUrl(),trackObject.getAlbum().getName(),trackObject.getUri()));
        }
        Intent intent = new Intent(UserProfileActivity.this, SeeCommonSongsActivity.class);
        startActivity(intent);
        SeeCommonSongsActivity.setCommonTracks(commonTrackList);

    }
}