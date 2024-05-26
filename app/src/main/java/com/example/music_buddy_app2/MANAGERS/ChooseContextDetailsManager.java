package com.example.music_buddy_app2.MANAGERS;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS.SeeOurRecommendationsActivity;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.RecommendationsRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.MyApiRecommendationsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.SERVICES.API.CustomRecommendationsApiInterface;
import com.example.music_buddy_app2.SERVICES.API.PlaylistsApiManager;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseContextDetailsManager {
    private static ChooseContextDetailsManager instance;
    static public Integer nbrOfSongsAdded;
    private PlaylistsApiManager playlistsApiManager;
    private Context context;
    private HashMap<TrackObject,String> allSongs;
    private CustomRecommendationsApiInterface customRecommendationsApiInterface;
    Retrofit retrofit;
    private static String genre;
    private List<SimplifiedPlaylistObject> selectedPlaylists;
    private String selectedFriends;
    private static List<Track> recommendations=new ArrayList<>();
    private ChooseContextDetailsManager(Context context) {
        nbrOfSongsAdded=0;
        this.selectedPlaylists = new ArrayList<>();
        playlistsApiManager=PlaylistsApiManager.getInstance(context);
        this.context=context;
        initiateApiService();
        allSongs=new HashMap<>();
    }
    public void reset()
    {
        nbrOfSongsAdded=0;
        this.selectedPlaylists = new ArrayList<>();
        allSongs=new HashMap<>();
    }
    public static void setGenre(String genree)
    {
        genre=genree;
    }
    public static String getGenre()
    {
        return genre;
    }
    public void initiateApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getMyRecsApiRetrofit();
        }
        customRecommendationsApiInterface = retrofit.create(CustomRecommendationsApiInterface.class);
    }
    public static ChooseContextDetailsManager getInstance(Context context) {
        if (instance == null) {
            instance = new ChooseContextDetailsManager(context);
        }
        return instance;
    }

    public void addToNbrOfSongs(Integer nbr, SimplifiedPlaylistObject playlistObject) {
        nbrOfSongsAdded+=nbr;
        selectedPlaylists.add(playlistObject);
    }

    public void deleteFromNbrOfSongs(Integer nbr, SimplifiedPlaylistObject playlistObject) {
        nbrOfSongsAdded-=nbr;
        selectedPlaylists.remove(playlistObject);
    }
    //        FORMAT
//         "items": [
//    {
//      "artist": "Kavinsky",
//      "name": "Odd Look",
//      "id": "7xcqbjV2NfxlnJzqdRuO7E",
//      "url": "https://i.scdn.co/image/ab67616d00001e02b06645ac5eaf2249c4f5fac7",
//      "date_added": "2023-08-10T16:07:31+00:00"
//    },
//    {
//
//    },
    public interface OnAllSongsReceivedListener {
        void onAllSongsReceived(HashMap<TrackObject,String> allSongs);
        void onError(String error);
    }
    public interface RecommendationOperationsCompleteCallback {
        void onComplete();
        void onError(String message);
    }

    public void getAllItems(OnAllSongsReceivedListener listener)
    {
        //get playlist items for each playlist
        allSongs.clear();
        AtomicInteger processedPlaylists = new AtomicInteger(0);

        for(SimplifiedPlaylistObject playlistObject:selectedPlaylists) {
            Integer nbrSongsInPlaylist = playlistObject.getTracks().getTotal();

            int offset = 0;
            int limit = 50;
            String id = playlistObject.getId();
            do
            {
                playlistsApiManager.getPlaylistItems(offset, limit, id, new PlaylistsApiManager.GetPlaylistItemsListener() {
                    @Override
                    public void onItemsReceived(HashMap<TrackObject,String> receivedTracks) {
                        allSongs.putAll(receivedTracks);
                        Log.e("MY_LOGS", String.valueOf(allSongs.size()));
                        if (processedPlaylists.incrementAndGet() == selectedPlaylists.size()) {
                            listener.onAllSongsReceived(allSongs);
                        }
                   }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        listener.onError("Failed to fetch songs for playlist "+ playlistObject.getName());
                    }
                });

                offset+=50;
                nbrSongsInPlaylist-=50;
                Log.e("MY_LOGS", String.valueOf(nbrSongsInPlaylist));

            } while (nbrSongsInPlaylist>0);

        }
    }

    public void getRecommendations(String selectedGenre, final RecommendationOperationsCompleteCallback callback)
    {
        if(selectedGenre==null || selectedGenre.equals(""))
            callback.onError("Genre was not selected");
        else getAllItems(new OnAllSongsReceivedListener() {
            @Override
            public void onAllSongsReceived(HashMap<TrackObject,String> allSongs) {

                Log.e("MY_LOGS", "All received "+ allSongs.size());
                if(allSongs.size()==0)
                {
                    callback.onError("No songs found for the playlists.");
                }
                else
                {
                    RecommendationsRequestBody request = createRequestBody(allSongs);
                    getRecommendationsFromApi(selectedGenre, request, new RecommendationOperationsCompleteCallback() {
                        @Override
                        public void onComplete() {

                            callback.onComplete();

                        }

                        @Override
                        public void onError(String message)
                        {
                            callback.onError("Error: " + message);
                        }
                    });
                }
            }
            @Override
            public void onError(String error)
            {
                callback.onError("Error fetching playlists items");
            }

        });
    }
    public RecommendationsRequestBody createRequestBody(HashMap<TrackObject,String> allSongs)
    {
        RecommendationsRequestBody request = new RecommendationsRequestBody();
        List<RecommendationsRequestBody.PlaylistItem> items = new ArrayList<>();
        for (TrackObject track : allSongs.keySet()) {
            RecommendationsRequestBody.PlaylistItem item = new RecommendationsRequestBody.PlaylistItem();
            item.setArtist(track.getArtists().get(0).getName());
            item.setName(track.getName());
            item.setId(track.getId());
            item.setUrl(track.getAlbum().getImages().get(0).getUrl());
            String addedAt = allSongs.get(track);
            item.setDate_added(addedAt != null ? addedAt : "2019-06-21T14:59:08Z");
            items.add(item);
        }
        request.setItems(items);
        return request;
    }
    public void getRecommendationsFromApi(String genre, RecommendationsRequestBody body, RecommendationOperationsCompleteCallback callback)
    {  Log.e("MY_LOGS","GENREE "+ genre );
        customRecommendationsApiInterface.getRecommendations(genre, body)
                .enqueue(new Callback<MyApiRecommendationsResponse>() {

                    @Override
                    public void onResponse(Call<MyApiRecommendationsResponse> call, Response<MyApiRecommendationsResponse> response) {
                        if (response.isSuccessful()) {

                            MyApiRecommendationsResponse recommendationsResponse = response.body();
                            Log.e("MY_LOGS", "SIZEE" + String.valueOf(recommendationsResponse.getRecommendations().size()));
                            getSongsFromIds(recommendationsResponse.getRecommendations(), callback);


                        } else {
                            Log.e("MY_LOGS", "Error: " + response.message()+ " "+response.code()+ " "+response.errorBody() + " "+response.body()+ " "+response.headers());
                            callback.onError("Failed to fetch recommendations from google cloud api: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyApiRecommendationsResponse> call, Throwable t) {
                        Log.e("MY_LOGS", "Failed to get recommendations from google cloud api. "+t.getMessage());
                        callback.onError("Failure at google cloud api: " + t.getMessage());
                    }
                });
    }

    public void getSongsFromIds(List<String> ids, RecommendationOperationsCompleteCallback callback)
    {
        if (ids.isEmpty()) {
            callback.onComplete();
            return;
        }
        List<String> firstHalf=ids.subList(0,ids.size()/2);
        List<String> secondHalf=ids.subList(ids.size()/2,ids.size());

        recommendations.clear();
        playlistsApiManager.getSeveralTracks(firstHalf, new PlaylistsApiManager.OnAllTracksFetchedListener() {

            @Override
            public void onSeveralTracksReceived(List<Track> tracks) {
                Log.d("MY_LOGS", "Tracks received: " + tracks.size());
                recommendations.addAll(tracks);
                playlistsApiManager.getSeveralTracks(secondHalf, new PlaylistsApiManager.OnAllTracksFetchedListener() {

                    @Override
                    public void onSeveralTracksReceived(List<Track> tracks) {
                        Log.d("MY_LOGS", "Tracks second received: " + tracks.size());
                        recommendations.addAll(tracks);
                        callback.onComplete();
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("MY_LOGS", "Error fetching recommended tracks from spotify api: " + errorMessage);
                        callback.onError("Error fetching tracks");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MY_LOGS", "Error fetching recommended tracks from spotify api: " + errorMessage);
                callback.onError("Error fetching recommended tracks from spotify api");
            }
        });

    }

    public void setSelectedFriends(String selectedFriends) {
        this.selectedFriends = selectedFriends;
    }

    public static List<Track> getRecs()
    {
        return recommendations;
    }
}
