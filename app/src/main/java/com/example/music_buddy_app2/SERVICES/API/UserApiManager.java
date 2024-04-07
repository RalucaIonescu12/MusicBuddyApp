package com.example.music_buddy_app2.SERVICES.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.PROFILE.ProfileActivity;
import com.example.music_buddy_app2.ACTIVITIES.PROFILE.UsersTopItemsActivity;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TopTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.USERS.UserResponse;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserApiManager {
    private static UserApiManager instance;
    private Context context;
    private UserApiListener listener;

    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private UserApiManager(Context context){
        this.context=context;
        initiateSpotifyApiService();
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    public static UserApiManager getInstance(Context context) {
        if (instance == null) {
            synchronized (UserApiManager.class) {
                if (instance == null) {
                    instance = new UserApiManager(context);
                }
            }
        }
        return instance;
    }
    public interface UserApiListener {
        void onProfileReceived(User user);

        void onAuthorizationError();

        void onFailure(String errorMessage);
    }
    public void setListener(UserApiListener listener) {
        this.listener = listener;
    }
    public void getProfile(UserApiListener listener)
    {
        String accessToken = SharedPreferencesManager.getToken(context);
        String authorization = "Bearer " + accessToken;
        Call<UserResponse> call = spotifyApiServiceInterface.getMyProfile(authorization);
        setListener(listener);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    User user = new User(userResponse.getDisplayName(), userResponse.getEmail(), userResponse.getImages().get(0).getUrl(), userResponse.getId(), userResponse.getUri());
                    if (listener != null) {
                        listener.onProfileReceived(user);
                    }
                } else {
                    if (listener != null) {
                        listener.onAuthorizationError();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFailure(t.getMessage());
                }
            }
        });

    }
    public interface TopItemsApiListener {
        void onItemsReceived(List<TopArtistsResponse.TopItem> receivedTopItems);
        void onAuthorizationError();
        void onFailure(String errorMessage);
    }
    public interface TopTracksApiListener {
        void onItemsReceived(List<TopTracksResponse.TopItem> receivedTopItems);
        void onAuthorizationError();
        void onFailure(String errorMessage);
    }

    public void getUsersTopTracks(int limit, int offset,String timeRange, TopTracksApiListener listener ) {
        String accessToken = SharedPreferencesManager.getToken(context);
        String authorization = "Bearer " + accessToken;
        Call<TopTracksResponse> call = spotifyApiServiceInterface.getUserTopTracks(authorization, limit, offset, timeRange);
        call.enqueue(new Callback<TopTracksResponse>() {
            @Override
            public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
                if (response.isSuccessful()) {
                    TopTracksResponse toptracksResponse = response.body();
                    if (toptracksResponse != null) {
                        List<TopTracksResponse.TopItem> receivedTopItems = toptracksResponse.getItems();
                        listener.onItemsReceived(receivedTopItems);
                    }
                } else {
                    Toast.makeText(context, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                    ///refresh token

//                        getRetrofitRefreshToken();
//                        Intent intent = new Intent(UsersTopItemsActivity.this, WelcomeActivity.class);
//                        startActivity(intent);
                    listener.onAuthorizationError();
                }

            }

            @Override
            public void onFailure(Call<TopTracksResponse> call, Throwable t) {
                Toast.makeText(context, "Failed top items request!", Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }
    public void getUsersTopArtists(int limit, int offset,String timeRange, TopItemsApiListener listener )
    {
        String accessToken = SharedPreferencesManager.getToken(context);
        String authorization = "Bearer " + accessToken;
        Call<TopArtistsResponse> call = spotifyApiServiceInterface.getUserTopArtists(authorization, limit, offset, timeRange);
        call.enqueue(new Callback<TopArtistsResponse>() {
            @Override
            public void onResponse(Call<TopArtistsResponse> call, Response<TopArtistsResponse> response) {
                if (response.isSuccessful()) {
                    TopArtistsResponse TopArtistsResponse = response.body();
                    if (TopArtistsResponse != null) {
                        List<TopArtistsResponse.TopItem> receivedTopItems = TopArtistsResponse.getItems();
                        if (listener != null) {
                            listener.onItemsReceived(receivedTopItems);
                        }

                    }
                } else {
                    Toast.makeText(context, "You need to reauthorize!", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onAuthorizationError();
                    }
//                        getRetrofitRefreshToken();
                }

            }

            @Override
            public void onFailure(Call<TopArtistsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
                Toast.makeText(context, "Failed top items request!", Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });

    }
}
