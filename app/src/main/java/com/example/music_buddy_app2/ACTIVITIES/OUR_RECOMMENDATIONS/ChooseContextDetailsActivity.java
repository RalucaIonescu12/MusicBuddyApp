package com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.PROFILE.FindFriendsActivity;
import com.example.music_buddy_app2.ADAPTERS.CONTEXT_RECS.FriendsPlaylistsAdapter;
import com.example.music_buddy_app2.ADAPTERS.CONTEXT_RECS.MyPlaylistsAdapter;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.FRAGMENTS.MyFriendsMultiChoiceFragment;
import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;

import java.util.ArrayList;
import java.util.List;

public class ChooseContextDetailsActivity extends AppCompatActivity implements MyFriendsMultiChoiceFragment.OnFriendsSelectedListener,MyPlaylistsAdapter.OnPlaylistSelectedListener, FriendsPlaylistsAdapter.OnFriendsPlaylistSelectedListener  {
    Spinner genreSpinner;
    private List<String> genres;
    private String  selectedGenre;
    private TextView tvMyPlaylists;

//TODO: fetch the genres when you login the app now at every page.Minimize api calls
    private TextView tvFriendsPlaylists;
    private String selectedOption;
    private String selectedMood;
    private ArrayAdapter<String> genreAdapter;
    private PlaylistsApiManager playlistsApiManager;
    private List<User> friends = new ArrayList<>();
    private UserManager userManager;
    RecyclerView myPlaylistsRV;
    RecyclerView myFriendsPlaylistsRV;
    CardView done;
    CardView myFriendsPlaylistsCV;
    CardView myPlaylistsCV;
    RadioGroup moodsRG;
    RadioGroup optionsRG;
    CardView getRecsButton;
    TextView nbrOfSelectedSongs;
    private MyPlaylistsAdapter myPlaylistsAdapter;
    private ChooseContextDetailsManager manager;
    private FriendsPlaylistsAdapter friendsPlaylistsAdapter;
    private List<SimplifiedPlaylistObject> friendsPlaylists = new ArrayList<>();
    private List<SimplifiedPlaylistObject> currentUsersPlaylists=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_context_details);

        List<String> options = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        genres=new ArrayList<>();
        playlistsApiManager=PlaylistsApiManager.getInstance(this);
        userManager=UserManager.getInstance(this);
        setViews();

        options.add("my playlists");
        options.add("mine and my friends' playlists");

        moods.add("Energetic");
        moods.add("Sad");
        moods.add("Calm");
        moods.add("Mad");

        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setId(View.generateViewId());
            optionsRG.addView(radioButton);
        }
// TODO: constrint to be able to add the playlist only once if nothing changed from the previous playlist
//TODO: la fel si pt add to queue
//TODO: refresh token cumva
// TODO: deelte the "clicked song" la recommendations

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedOption != null) {
                    switch (selectedOption) {
                        case "mine and my friends' playlists":
                            handleMineAndMyFriendsOption();
                            break;
                        case "my playlists":
                            myFriendsPlaylistsCV.setVisibility(View.GONE);
                            myPlaylistsCV.setVisibility(View.VISIBLE);
                        default:
                            break;
                    }
                }
            }
        });

        optionsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                RadioButton button = findViewById(id);
                selectedOption = button.getText().toString();
            }
        });

        for (String mood : moods) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(mood);
            radioButton.setId(View.generateViewId());
            moodsRG.addView(radioButton);
        }

        moodsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                RadioButton button = findViewById(id);
                setSelectedMood(button.getText().toString());
            }
        });

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSelectedGenre(parentView.getItemAtPosition(position).toString());

           }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        getRecsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseContextDetailsActivity.this,LoadingActivity.class);
                startActivity(intent);
                LoadingActivity.genre=selectedGenre;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        manager.reset();
        nbrOfSelectedSongs.setText(ChooseContextDetailsManager.nbrOfSongsAdded.toString() + " songs selected");
        if (friendsPlaylistsAdapter != null) {
            friendsPlaylistsAdapter.reset();
        }
        if (myPlaylistsAdapter != null) {
            myPlaylistsAdapter.reset();
        }
    }
    @Override
    public void onFriendsLimitExceeded() {
       Toast.makeText(ChooseContextDetailsActivity.this,"Can't select more than 300 songs.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLimitExceeded() {
        Toast.makeText(ChooseContextDetailsActivity.this,"Can't select more than 300 songs.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPlaylistSelected() {
        nbrOfSelectedSongs.setText(ChooseContextDetailsManager.nbrOfSongsAdded.toString() + " songs selected");
    }

    @Override
    public void onPlaylistUncheck() {
        nbrOfSelectedSongs.setText(ChooseContextDetailsManager.nbrOfSongsAdded.toString() + " songs selected");

    }
    @Override
    public void onFriendsPlaylistSelected() {

        nbrOfSelectedSongs.setText(ChooseContextDetailsManager.nbrOfSongsAdded.toString()+ " songs selected");
    }

    @Override
    public void onFriendsPlaylistUncheck() {
        nbrOfSelectedSongs.setText(ChooseContextDetailsManager.nbrOfSongsAdded.toString()+ " songs selected");
    }


    public void setSelectedGenre(String selectedGenre)
    {
        this.selectedGenre = selectedGenre;
    }
    public void setSelectedMood(String selectedMood)
    {
        this.selectedMood=selectedMood;
    }
    private void handleMineAndMyFriendsOption()
    {
        userManager.getFriends(new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
                friends.clear();
                friends.addAll(users);
                MyFriendsMultiChoiceFragment dialogFragment = new MyFriendsMultiChoiceFragment(friends,ChooseContextDetailsActivity.this);
                dialogFragment.show(getSupportFragmentManager(), "friends_multichoice_fragment");
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChooseContextDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setViews() {
        genreSpinner = findViewById(R.id.genresSpinneContext);
        genreAdapter = new ArrayAdapter<>(ChooseContextDetailsActivity.this, android.R.layout.simple_spinner_item, genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);
        tvFriendsPlaylists=findViewById(R.id.friendsPlaylistsTV);
        tvMyPlaylists=findViewById(R.id.myPlaylistsTV);

        myFriendsPlaylistsCV=findViewById(R.id.cvFriendsPlaylists);
        myPlaylistsCV=findViewById(R.id.cvMyPlaylists);

        myPlaylistsRV = findViewById(R.id.myPlaylistsRv);
        myPlaylistsRV.setLayoutManager(new LinearLayoutManager(this));
        myPlaylistsAdapter = new MyPlaylistsAdapter(ChooseContextDetailsActivity.this, currentUsersPlaylists,this);
        myPlaylistsRV.setAdapter(myPlaylistsAdapter);

        myFriendsPlaylistsRV = findViewById(R.id.myFriendsPlaylistsRv);
        myFriendsPlaylistsRV.setLayoutManager(new LinearLayoutManager(this));
        friendsPlaylistsAdapter = new FriendsPlaylistsAdapter(this,friendsPlaylists,this);
        myFriendsPlaylistsRV.setAdapter(friendsPlaylistsAdapter);

        done=findViewById(R.id.doneSelectignBtn);
        moodsRG= findViewById(R.id.radioGroupMoodContextDetails);
        optionsRG = findViewById(R.id.radioGrouptContextDetails);

        getRecsButton=findViewById(R.id.getRecs);
        nbrOfSelectedSongs=findViewById(R.id.nbrofSongsSelected);
        nbrOfSelectedSongs.setText("0 songs selected");
        this.manager=ChooseContextDetailsManager.getInstance(this);
        setSpinnerGenres();
        getUsersPlaylists();
    }
    @Override
    public void onFriendsSelected(List<User> selectedFriends) {
        int offsets[]={0,50,100,150};
        friendsPlaylists.clear();
        for (User friend : selectedFriends) {
            for(int offset:offsets) {
                playlistsApiManager.getPlaylistsForUser(friend.getSpotifyId(), offset, new PlaylistsApiManager.PlaylistsCallback() {
                    @Override
                    public void onSuccess(List<SimplifiedPlaylistObject> playlists) {
                        for (SimplifiedPlaylistObject playlist:playlists)
                        {
                            playlist.setDisplayName(friend.getUsername());
                            friendsPlaylists.add(playlist);
                        }

                        friendsPlaylistsAdapter.notifyDataSetChanged();
                        runOnUiThread(() -> {
                            myFriendsPlaylistsCV.setVisibility(View.VISIBLE);
                            myPlaylistsCV.setVisibility(View.VISIBLE);
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ChooseContextDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    private void getUsersPlaylists() {
        int offsets[]={0,50,100};
        currentUsersPlaylists.clear();
        for(int offset:offsets)
        {
            playlistsApiManager.getPlaylistsForCurrentUser(offset,
            new PlaylistsApiManager.PlaylistsCallback() {
                @Override
                public void onSuccess(List<SimplifiedPlaylistObject> playlists) {

                    currentUsersPlaylists.addAll(playlists);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myPlaylistsAdapter.notifyDataSetChanged();
                        }
                    });

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ChooseContextDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void setSpinnerGenres() {
        playlistsApiManager.getAllGenres(new PlaylistsApiManager.GenresCallback() {
            @Override
            public void onSuccess(List<String> genres_) {
                genres.clear();
                genres.addAll(genres_);
                genreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ChooseContextDetailsActivity.this,"Couldn't get genres",Toast.LENGTH_SHORT).show();
            }
        });


    }
}