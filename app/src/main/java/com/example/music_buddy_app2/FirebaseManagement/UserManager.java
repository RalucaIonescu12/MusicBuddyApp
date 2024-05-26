package com.example.music_buddy_app2.FirebaseManagement;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.UserApiManager;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserManager extends AppCompatActivity {
    private static UserManager instance;
    private Context context;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    public User user;
    Retrofit retrofit;
    private User currentUser;
    private List<User> allUsers;
    private List<User> followingUsers;
    private List<User> followersUsers;
    String userId;

    private UserManager(Context context)
    {
        this.context=context;
        this.allUsers=new ArrayList<>();
        this.followersUsers=new ArrayList<>();
        this.followingUsers=new ArrayList<>();
        initiateSpotifyApiService();
        this.userId= SharedPreferencesManager.getUserId(context);
        this.currentUser=new User();
    }
//    public void setAccessToken(String token)
//    {
//        this.token=token;
//        Log.e("FIREBASE_LOGS", "1. S a setat access token in manager: " + this.token);
//    }
//    public void setRefreshToken(String token)
//    {
//        this.refreshToken=token;
//        Log.e("FIREBASE_LOGS", "1. S a setat refresh token in manager: " + this.token);
//    }
    public static UserManager getInstance(Context context) {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager(context);
                }
            }
        }
        return instance;
    }
    public User getUser()
    {
        return user;
    }
    public interface OnUserReceivedListener {
        void onUserReceived(User user);
        void onFailure(String errorMessage);
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    public void setCurrentUser(OnUserReceivedListener listener) {

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        currentUserRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    currentUser = user;
                    listener.onUserReceived(user);
                } else {
                    listener.onFailure("Current user data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });

    }
    public User getCurrentUser()
    {
        return currentUser;
    }
//    public void loginUser(final LoginListener loginListener)
//    {
//        userApiManager.getProfile(new UserApiManager.UserApiListener() {
//            @Override
//            public void onProfileReceived(User user) {
//                Log.e("CODE_RECEIVED", "login user");
//                SharedPreferencesManager.saveUserId(context, user.getSpotifyId());
//                Log.e("FIREBASE_LOGS", "2. S a setat id ul in shared preferences " + SharedPreferencesManager.getUserId(context));
//                addUser(user);
//                setCurrentUser(new OnUserReceivedListener() {
//                    @Override
//                    public void onUserReceived(User user) {
//                        Log.e("FIREBASE_LOGS", "set the current user from the database");
//                        if (loginListener != null) {
//                            loginListener.onLoginSuccess();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String errorMessage) {
//                        Log.e("FIREBASE_LOGS", "failed to set the current user from the database");
//                        if (loginListener != null) {
//                            loginListener.onLoginFailure(errorMessage);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onAuthorizationError() {
//                Log.e("FIREBASE_LOGS", "Response for get user not successful.");
//                if (loginListener != null) {
//                    loginListener.onLoginFailure("Authorization error");
//                }
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Log.e("FIREBASE_LOGS", "API call failed: " + errorMessage);
//                if (loginListener != null) {
//                    loginListener.onLoginFailure(errorMessage);
//                }
//            }
//        });
//    }
    public void loginUser()
    {
        Call<UserResponse> call= spotifyApiServiceInterface.getMyProfile();

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful()) {
                    Log.e("MY_LOGS","login user");

                    UserResponse userResponse = response.body();
                    user = new User(userResponse.getDisplayName(), userResponse.getEmail(),  userResponse.getImages().get(0).getUrl(),userResponse.getId(),userResponse.getUri());
                    SharedPreferencesManager.saveUserId(context,user.getSpotifyId());

                    Log.e("MY_LOGS", "2. S a setat id ul in shared preferences " + SharedPreferencesManager.getUserId(context));
                    addUser(user);
                    setCurrentUser(new OnUserReceivedListener() {
                        @Override
                        public void onUserReceived(User user) {
                            Log.e("MY_LOGS","set the current user from the database");
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("MY_LOGS","failed to set the current user from the database");
                        }

                    });
                }
                else
                {
                    Log.e("MY_LOGS", "Response for get user not successfull.");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });
    }
    public String getCurrentUserId()
    {
        return userId;
    }
    public void addUser(User user)
    {
        Log.d("MY_LOGS", "Username: " + user.getUsername());
        Log.d("MY_LOGS", "Email: " + user.getEmail());
        Log.d("MY_LOGS", "Friends IDs: " + user.getFollowingIds());
        Log.d("MY_LOGS", "Spotify URI: " + user.getUri());
        Log.d("MY_LOGS","Playlists created within the app: "+ user.getPlaylistsCreatedWithTheApp());
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.child(user.getSpotifyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    //the user already exists
                    user.setPlaylistsCreatedWithTheApp(dataSnapshot.child("playlistsCreatedWithTheApp").getValue(Integer.class));
                    Log.e("MY_LOGS", "Already exists");
                }
                else
                {
                    Map<String, Object> userFields = new HashMap<>();
                    userFields.put("username", user.getUsername());
                    userFields.put("email", user.getEmail());
                    userFields.put("profileImageUrl",user.getProfileImageUrl());
                    userFields.put("playlistsCreatedWithTheApp", user.getPlaylistsCreatedWithTheApp());
                    userFields.put("spotifyId",user.getSpotifyId());
                    List<String> following = new ArrayList<>();
                    List<String> followers = new ArrayList<>();
                    following.add("31jflcl7sia3xvoe7elq3uziufzm");
                    followers.add("31jflcl7sia3xvoe7elq3uziufzm");
                    userFields.put("followingIds", following);
                    userFields.put("followerIds", followers);
                    userFields.put("uri", user.getUri());

                    usersRef.child(user.getSpotifyId()).setValue(userFields);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MY_LOGS", "Database error");
            }
        });
    }
    public interface OnUsersReceivedListener {
        void onUsersReceived(List<User> users);

        void onFailure(String errorMessage);
    }
    public void getAllUsersCall(OnUsersReceivedListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers = new ArrayList<>();
                boolean ok;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && !user.getSpotifyId().equals(userId))
                    {
                        ok=true;
                        for(User foll: followingUsers )
                            if(foll.getSpotifyId().equals(user.getSpotifyId()))
                            {
                                ok=false;
                                break;
                            }
                        if(ok)
                            allUsers.add(user);
                    }
                }
                listener.onUsersReceived(allUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    public void getFollowingUsersForUserCall(String userId, OnUsersReceivedListener listener) {
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followingIds");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> followingIds = new ArrayList<>();
                for (DataSnapshot followingSnapshot : dataSnapshot.getChildren()) {
                    String followingId = followingSnapshot.getValue(String.class);
                    if (followingId != null)
                    {
                        followingIds.add(followingId);
                    }
                }
                followingUsers.clear();
                getUsersForIds(followingIds, "following", listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    public void getFollowerUsersForUserCall(String userId, OnUsersReceivedListener listener) {
        DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followerIds");
        followerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> followerIds = new ArrayList<>();
                for (DataSnapshot followingSnapshot : dataSnapshot.getChildren()) {
                    String followingId = followingSnapshot.getValue(String.class);
                    if (followingId != null) {
                        followerIds.add(followingId);
                    }
                }
                followersUsers.clear();
                getUsersForIds(followerIds, "followers", listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    private void getUsersForIds(List<String> userIds,String type, OnUsersReceivedListener listener) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        for (String userId : userIds) {
            usersRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if(type.equals("following"))
                        {
                            followingUsers.add(user);
                            if (followingUsers.size() == userIds.size())
                                listener.onUsersReceived(followingUsers);
                            Log.e("MY_LOGS","users retrieved" + followingUsers);
                        }
                        else {
                            followersUsers.add(user);
                            if (followersUsers.size() == userIds.size())
                                listener.onUsersReceived(followersUsers);
                            Log.e("MY_LOGS","users retrieved" + followersUsers);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onFailure(databaseError.getMessage());
                }
            });
        }
    }
    public void followUser(User user)
    {
        if (userId != null)
        {
            DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followingIds");
            followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> followingIds = new ArrayList<>();
                    for (DataSnapshot followingSnapshot : dataSnapshot.getChildren()) {
                        String followingId = followingSnapshot.getValue(String.class);
                        if (followingId != null) {
                            followingIds.add(followingId);
                        }
                    }
                    followingIds.add(user.getSpotifyId());
                    followingRef.setValue(followingIds);

                    //add the current user id to the list of followersIDs for the user followed
                    DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getSpotifyId()).child("followerIds");
                    followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> followerIds = new ArrayList<>();
                            for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
                                String followerId = followerSnapshot.getValue(String.class);
                                if (followerId != null) {
                                    followerIds.add(followerId);
                                }
                            }
                            followerIds.add(userId);
                            followerRef.setValue(followerIds);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("MY_LOGS", "Error adding follower: " + databaseError.getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MY_LOGS", "Error follow user: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("MY_LOGS", "Current user ID null.");
        }
    }
    public void getFriends(OnUsersReceivedListener listener) {
        getFollowingUsersForUserCall(userId, new OnUsersReceivedListener() {
            @Override
            public void onUsersReceived(List<User> following) {
                getFollowerUsersForUserCall(userId, new OnUsersReceivedListener() {
                    @Override
                    public void onUsersReceived(List<User> followers)
                    {
                        List<User> friends = new ArrayList<>();
                        for (User followingUser : following) {
                            for (User followerUser : followers) {
                                if (followingUser.getSpotifyId().equals(followerUser.getSpotifyId())) {
                                    friends.add(followingUser);
                                    break;
                                }
                            }
                        }
                        listener.onUsersReceived(friends);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        listener.onFailure(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

}
