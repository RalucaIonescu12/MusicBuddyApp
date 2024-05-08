package com.example.music_buddy_app2.MANAGERS;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private List<SimplifiedPlaylistObject> selectedPlaylists;
    private ChooseContextDetailsManager(Context context) {
        nbrOfSongsAdded=0;
        this.selectedPlaylists = new ArrayList<>();
        playlistsApiManager=PlaylistsApiManager.getInstance(context);
        this.context=context;
        initiateApiService();
        allSongs=new HashMap<>();
    }
    public void initiateApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getMyApiRetrofit();
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
                        Log.e("SONGS", String.valueOf(allSongs.size()));
                        if (processedPlaylists.incrementAndGet() == selectedPlaylists.size()) {
                            listener.onAllSongsReceived(allSongs);
                        }
                   }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        listener.onError("Failed to fetch songs");

                    }
                });
                offset+=50;
                nbrSongsInPlaylist-=50;
                Log.e("SONGS", String.valueOf(nbrSongsInPlaylist));
            } while (nbrSongsInPlaylist>0);

        }
//        listener.onAllSongsReceived(allSongs);
    }

    public void getRecommendations()
    {
        getAllItems(new OnAllSongsReceivedListener() {
            @Override
            public void onAllSongsReceived(HashMap<TrackObject,String> allSongs) {

                Log.e("SONGS", "All received "+ allSongs.size());
                createRequestBody(allSongs);
            }
            @Override
            public void onError(String error)
            {
                Toast.makeText(context,"Error fetching playlists items", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void createRequestBody(HashMap<TrackObject,String> allSongs)
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

        getRecommendationsFromApi("r-n-b", request);
    }
    public void getRecommendationsFromApi(String genre, RecommendationsRequestBody body)
    {
        customRecommendationsApiInterface.getRecommendations(genre, body)
                .enqueue(new Callback<MyApiRecommendationsResponse>() {
                    @Override
                    public void onResponse(Call<MyApiRecommendationsResponse> call, Response<MyApiRecommendationsResponse> response) {
                        if (response.isSuccessful()) {
                            MyApiRecommendationsResponse recommendationsResponse = response.body();
                            getSongsFromIds(recommendationsResponse.getRecommendations());

                        } else {
                            Log.e("Recommendations", "Error: " + response.message()+ " "+response.code()+ " "+response.errorBody() + " "+response.body()+ " "+response.headers());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyApiRecommendationsResponse> call, Throwable t) {

                        Log.e("Recommendations", "Failed to get recommendations. "+t.getMessage());
                    }
                });
    }
    public void getSongsFromIds(List<String> ids)
    {
        List<String> firstHalf=ids.subList(0,ids.size()/2);
        List<String> secondHalf=ids.subList(ids.size()/2,ids.size());
        playlistsApiManager.getSeveralTracks(firstHalf, new PlaylistsApiManager.OnAllTracksFetchedListener() {

            @Override
            public void onSeveralTracksReceived(List<Track> tracks) {
                Log.d("SeveralTracks", "Tracks received: " + tracks.size());
                playlistsApiManager.getSeveralTracks(secondHalf, new PlaylistsApiManager.OnAllTracksFetchedListener() {

                    @Override
                    public void onSeveralTracksReceived(List<Track> tracks) {
                        Log.d("SeveralTracks", "Tracks second received: " + tracks.size());
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("SeveralTracks", "Error fetching tracks: " + errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("SeveralTracks", "Error fetching tracks: " + errorMessage);
            }
        });

    }
}
