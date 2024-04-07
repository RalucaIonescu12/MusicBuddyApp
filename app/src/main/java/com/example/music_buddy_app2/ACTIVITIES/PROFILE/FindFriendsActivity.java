package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.USERS.ManageFriendsAdapter;
import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

import java.util.List;

public class FindFriendsActivity extends AppCompatActivity
{
    UserManager manager;
    ManageFriendsAdapter adapter;
    Button allUsersBtn,followingBtn,followersBtn;
    RecyclerView usersRV;
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

        getFollowersUsers();

        allUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FIREBASE_LOGS","all users selected");
                manager.getAllUsersCall(new UserManager.OnUsersReceivedListener() {
                    @Override
                    public void onUsersReceived(List<User> users) {
                        Log.e("FIREBASE_LOGS","will display : "+ users);
                        adapter.setUsers(users);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(FindFriendsActivity.this, "Failed to retrieve users: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        followersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FIREBASE_LOGS","followers users selected");
                manager.getFollowerUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
                    @Override
                    public void onUsersReceived(List<User> users) {
                        Log.e("FIREBASE_LOGS","will display" + users);
                        adapter.setUsers(users);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(FindFriendsActivity.this, "Failed to retrieve followe users: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FIREBASE_LOGS","folowing users selected");
                manager.getFollowingUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
                    @Override
                    public void onUsersReceived(List<User> users) {
                        Log.e("FIREBASE_LOGS","will display: "+ users);
                        adapter.setUsers(users);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(FindFriendsActivity.this, "Failed to retrieve following users: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getFollowersUsers() {
        manager.getFollowerUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
                Log.e("FIREBASE_LOGS","will display" + users);
                getFollowingUsers();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve followe users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getAllUsers() {
        manager.getAllUsersCall(new UserManager.OnUsersReceivedListener() {
        @Override
        public void onUsersReceived(List<User> users) {
            adapter.setUsers(users);
        }

        @Override
        public void onFailure(String errorMessage) {
            Toast.makeText(FindFriendsActivity.this, "Failed to retrieve users: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    });}
    private void getFollowingUsers(){
        manager.getFollowingUsersForUserCall(SharedPreferencesManager.getUserId(FindFriendsActivity.this), new UserManager.OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> users) {
                Log.e("FIREBASE_LOGS","will display: "+ users);
                getAllUsers();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FindFriendsActivity.this, "Failed to retrieve following users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}