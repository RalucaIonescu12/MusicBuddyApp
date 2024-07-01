package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ADAPTERS.USERS.ManageFriendsAdapter;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindFriendsActivity extends BaseActivity
{
    UserManager manager;
    ManageFriendsAdapter adapter;
    CardView allUsersBtn,followingBtn,followersBtn;
    RecyclerView usersRV;
    TextView title;
    List<User> allUsers, followingUsers, followerUsers;
    public static List<String> friendsUsers=new ArrayList<>();
    public static String listName= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        if(manager==null)
            manager= UserManager.getInstance(FindFriendsActivity.this);
        usersRV = findViewById(R.id.users_rv);
        adapter = new ManageFriendsAdapter(this);
        usersRV.setAdapter(adapter);
        allUsersBtn = findViewById(R.id.allUsersBtn);
        followingBtn = findViewById(R.id.followingBtn);
        followersBtn = findViewById(R.id.followersBtn);
        title=findViewById(R.id.title);
//        Log.d("MY")
        if(listName!=null && !listName.equals(""))
        {
            if(listName.equals("followers")){
                clickForFollowers();
                getFriends();
            }
            else if(listName.equals("following"))
            {
                clickForFollowing();
                getFriends();
            }
            else{ clickForAllUsers();
                getFriends();}
        }
        else {
            getFollowingUsers();
            getFriends();
            listName = "following";
        }
        allUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickForAllUsers();

            }
        });
        followersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              clickForFollowers();
            }
        });
        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              clickForFollowing();
            }
        });

    }
    private void clickForAllUsers()
    {
        manager.getAllUsersCall(new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
                allUsers=users;
                Log.d("MY_APP","all users fetched" + allUsers.size());
                Log.e("MY_LOGS","Users received.");
                title.setText("Find friends");
                listName="all";
                getFriends();
                adapter.setUsers(users);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clickForFollowing()
    {
        manager.getFollowingUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
//                        Log.d("MY_LOGS","Users received for following users.");
//                        Log.d("MY_APP","following users fetched" + users.size());

                followingUsers=users;
                Log.d("MY_APP","following users" + followingUsers.size());
                getFriends();
                title.setText("People you follow");
                listName="following";
                adapter.setUsers(users);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve following users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clickForFollowers()
    {
        manager.getFollowerUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
                Log.e("MY_LOGS","Users received.");

                listName="followers";
                followerUsers=users;
                Log.d("MY_APP","follower users fetched" + followerUsers.size());
                getFriends();
                title.setText("People that follow you");
                adapter.setUsers(users);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve follower users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getFollowingUsers() {
        manager.getFollowingUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
//                Log.d("MY_APP","following users fetched" + users.size());

                adapter.setUsers(users);
                listName="following";
                followingUsers=users;
                Log.d("MY_APP","following users " + followingUsers.size());
                getFriends();
                title.setText("People you follow");
//                getFollowingUsers();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve follower users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getFriends(){
        manager.getFollowerUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> usersss) {
                Log.e("MY_LOGS","Users received.");

                followerUsers=new ArrayList<>(usersss);
                manager.getFollowingUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
                    @Override
                    public void onUsersReceived(List<User> userss) {
                        Log.e("MY_LOGS","Users received.");
                        followingUsers= new ArrayList<>(userss);
                        List<User> friends=new ArrayList<>(followerUsers);
                        friends.retainAll(followingUsers);
                        for(User friend: friends)
                            friendsUsers.add(friend.getSpotifyId());
                        Log.e("MY_LOGS", "FRIENDAS" +followerUsers.size()+ " "+ followingUsers.size()+ " " +friendsUsers.toString());
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(FindFriendsActivity.this, "Failed to retrieve following users: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve follower users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


}