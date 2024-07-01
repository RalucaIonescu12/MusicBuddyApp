package com.example.music_buddy_app2.MANAGERS;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.MODELS.UserFirebase;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;


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
    private FirebaseFunctions mFunctions;
    private FirebaseAuth mAuth;

    private UserManager(Context context)
    {
        this.context=context;
        this.allUsers=new ArrayList<>();
        this.followersUsers=new ArrayList<>();
        this.followingUsers=new ArrayList<>();
        initiateSpotifyApiService();
        this.userId= SharedPreferencesManager.getUserId(context);
        this.currentUser=new User();
        this.mFunctions = FirebaseFunctions.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
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
    public static synchronized UserManager getInstance(Context context) {
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
    public void setCurrentUser(final OnUserReceivedListener listener) {

//        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
//        currentUserRef.addListenerForSingleValueEvent (new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    currentUser = user;
//                    listener.onUserReceived(user);
//                } else {
//                    listener.onFailure("Current user data not found");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                listener.onFailure(databaseError.getMessage());
//            }
//        });
        Log.d("MY_LOGS","IN SET CURRENT USER");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        if(!idToken.equals(SharedPreferencesManager.getCustomToken(context))) {
                            Log.d("MY_LOGS", "refreshed the custom tokne");
                            SharedPreferencesManager.saveFirebaseCustomToken(context, idToken);
                        }
                        userId=SharedPreferencesManager.getUserId(context);
                        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    } else {
                        Log.e("MY_LOGS", "Error getting ID token", task.getException());
                        listener.onFailure("Error getting ID token");
                    }
                }
            });
        } else {
            listener.onFailure("No authenticated user");
        }

    }
    public User getCurrentUser()
    {
        return currentUser;
    }
    public void addFollowingForCurrentUser(User user)
    {
        List<String> following= new ArrayList<>(currentUser.getFollowingIds());
        following.add(user.getSpotifyId());
        currentUser.setFollowingIds(following);
    }
    public interface OnTaskCompleteListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public void loginUser(final OnTaskCompleteListener  listener)
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
                    listener.onSuccess();

                }
                else
                {
                    Log.e("MY_LOGS", "Response for get user not successfull.");
                    listener.onFailure("Response for get profile not successful.");

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
                listener.onFailure("API call failed: " + t.getMessage());

            }
        });
    }
    public void authenticateAnonymously(final OnTaskCompleteListener listener) {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("MY_LOGS", "signInAnonymously:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        authenticateUser(user.getUid(), listener);
                    }
                    else {
                        Log.d("MY_LOGS", "user is null");
                        listener.onFailure("User is null");
                    }
                } else {
                    Log.e("MY_LOGS", "signInAnonymously:failure", task.getException());
                    listener.onFailure("signInAnonymously:failure");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(mAuth.getCurrentUser()==null)
                    Log.e("MY_LOGS", "firebase user is null");
                else if(mAuth==null)
                    Log.e("MY_LOGS","mauth null");
                Log.e("MY_LOGS", "signInAnonymously: onFailure", e);
            }
        });

    }
    private Task<String> getCustomToken(String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        return mFunctions
                .getHttpsCallable("createCustomToken")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                Log.e("MY_LOGS", "Firebase Functions Exception: " + ffe.getMessage());
                                Log.e("MY_LOGS", "Details: " + ffe.getDetails());
                            }
                            throw e;
                        }
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        return (String) result.get("customToken");
                    }
                });
    }
    private void authenticateUser(String uid,final OnTaskCompleteListener listener) {
        getCustomToken(uid).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("MY_LOGS", "Error getting custom token", task.getException());
                    listener.onFailure("Error getting custom token");
                    return;
                }

                String token = task.getResult();
                mAuth.signInWithCustomToken(token).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MY_LOGS", "User authenticated");

                            FirebaseUser userr = mAuth.getCurrentUser();
                            if (userr != null) {
                                userr.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {

                                            String idToken = task.getResult().getToken();
                                            SharedPreferencesManager.saveFirebaseCustomToken(context, idToken);

                                            Log.d("MY_LOGS", "ID: :" +  SharedPreferencesManager.getCustomToken(context));
//                                            addUserToDatabase(new OnTaskCompleteListener() {
//                                                @Override
//                                                public void onSuccess() {
//
//                                                }
//
//                                                @Override
//                                                public void onFailure(String errorMessage) {
//                                                    listener.onFailure("Error add to database");
//                                                }
//                                            });
                                            listener.onSuccess();
                                        } else {
                                            Log.e("MY_LOGS", "Error getting ID token", task.getException());
                                            listener.onFailure("Error getting ID token");
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.e("MY_LOGS", "Authentication failed", task.getException());
                            listener.onFailure("Authentication failed");
                        }
                    }
                });
            }
        });
    }
    public synchronized void addUserToDatabase(final OnTaskCompleteListener listener) {

        loginUser(new OnTaskCompleteListener() {
            @Override
            public void onSuccess() {
                addUser(user, new OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {

                        listener.onSuccess();

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        listener.onFailure(errorMessage);
                    }
                });

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MY_LOGS", "Failed to login: " + errorMessage);
                listener.onFailure(errorMessage);
            }
        });
    }
    public String getCurrentUserId()
    {
        return userId;
    }
    public void addUser(User user, final OnTaskCompleteListener listener)
    {
        Log.d("MY_LOGS", "Username: " + user.getUsername());
        Log.d("MY_LOGS", "Email: " + user.getEmail());
        Log.d("MY_LOGS", "Friends IDs: " + user.getFollowingIds());
        Log.d("MY_LOGS", "Spotify URI: " + user.getUri());
        Log.d("MY_LOGS","Playlists created within the app: "+ user.getPlaylistsCreatedWithTheApp());
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.child(user.getSpotifyId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                listener.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MY_LOGS", "Database error");
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
    public interface OnUsersReceivedListener {
        void onUsersReceived(List<User> users);

        void onFailure(String errorMessage);
    }
    public User convertToUser(UserFirebase userFirebase) {
        User user = new User();
        user.setUsername(userFirebase.getUsername());
        user.setEmail(userFirebase.getEmail());
        user.setProfileImageUrl(userFirebase.getProfileImageUrl());
        user.setPlaylistsCreatedWithTheApp(userFirebase.getPlaylistsCreatedWithTheApp());
        user.setSpotifyId(userFirebase.getSpotifyId());
        user.setUri(userFirebase.getUri());
        user.setFollowingIds(userFirebase.getFollowingIds());
        user.setFollowersIds(userFirebase.getFollowerIds());
        return user;
    }
    public void getAllUsersCall(OnUsersReceivedListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers = new ArrayList<>();
                boolean ok;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserFirebase user = snapshot.getValue(UserFirebase.class);
                    Log.d("MY_LOGSs",user.toString());
                    Log.d("MY_LOGSs",user.getSpotifyId());
                    if (user != null && !user.getSpotifyId().equals(userId))
                    {
                        ok=true;
                        for(User foll: followingUsers)
                            if(foll.getSpotifyId().equals(user.getSpotifyId()))
                            {
                                ok=false;
                                break;
                            }
                        if(ok)
                            allUsers.add(convertToUser(user));
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
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserFirebase user = dataSnapshot.getValue(UserFirebase.class);
                    if (user != null) {
                        if(type.equals("following"))
                        {
                            followingUsers.add(convertToUser(user));
                            if (followingUsers.size() == userIds.size())
                                listener.onUsersReceived(followingUsers);

                        }
                        else {
                            followersUsers.add(convertToUser(user));
                            if (followersUsers.size() == userIds.size())
                                listener.onUsersReceived(followersUsers);
                            Log.e("MY_LOGS","users retrieved in user manager get followers" + followersUsers);
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
//    public void followUser(User user)
//    {
//
//        if (userId != null)
//        {
//            DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("followingIds");
//            followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    List<String> followingIds = new ArrayList<>();
//                    for (DataSnapshot followingSnapshot : dataSnapshot.getChildren()) {
//                        String followingId = followingSnapshot.getValue(String.class);
//                        if (followingId != null) {
//                            followingIds.add(followingId);
//                        }
//                    }
//                    followingIds.add(user.getSpotifyId());
//                    followingRef.setValue(followingIds);
//
//                    //add the current user id to the list of followersIDs for the user followed
//                    DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getSpotifyId()).child("followerIds");
//                    followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            List<String> followerIds = new ArrayList<>();
//                            for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
//                                String followerId = followerSnapshot.getValue(String.class);
//                                if (followerId != null) {
//                                    followerIds.add(followerId);
//                                }
//                            }
//                            followerIds.add(userId);
//                            followerRef.setValue(followerIds);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Log.e("MY_LOGS", "Error adding follower: " + databaseError.getMessage());
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e("MY_LOGS", "Error follow user: " + databaseError.getMessage());
//                }
//            });
//        } else {
//            Log.e("MY_LOGS", "Current user ID null.");
//        }
//    }
    public void followUser(User user) {
        if (userId != null) {
            addFollowingForCurrentUser(user);
            followUserInCurrentUser(user);
            addCurrentUserAsFollower(user);

        } else {
            Log.e("MY_LOGS", "Current user ID null.");
        }
    }

    private void followUserInCurrentUser(User user) {
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
                followingRef.setValue(followingIds)
                        .addOnSuccessListener(aVoid -> Log.d("MY_LOGS", "Following list updated successfully."))
                        .addOnFailureListener(e -> Log.e("MY_LOGS", "Failed to update following list: " + e.getMessage()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MY_LOGS", "Error follow user: " + databaseError.getMessage());
            }
        });
    }

    private void addCurrentUserAsFollower(User user) {
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
                followerRef.setValue(followerIds)
                        .addOnSuccessListener(aVoid -> Log.d("MY_LOGS", "Follower list updated successfully."))
                        .addOnFailureListener(e -> Log.e("MY_LOGS", "Failed to update follower list: " + e.getMessage()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MY_LOGS", "Error adding follower: " + databaseError.getMessage());
            }
        });
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
    public void logout()
    {
        SharedPreferencesManager.clearToken(context);
        SharedPreferencesManager.clearRefreshToken(context);
        SharedPreferencesManager.clearExpiryTime(context);
        SharedPreferencesManager.clearCustomToken(context);
        SharedPreferencesManager.clearUserId(context);

    }
}
