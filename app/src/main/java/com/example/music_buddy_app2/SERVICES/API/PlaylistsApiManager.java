package com.example.music_buddy_app2.SERVICES.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS.ChooseArtistForSpotifyRecActivity;
import com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS.ChooseTracksWithAudioFeaturesForSpotifyRecActivity;
import com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS.SeeSpotifyRecommendationsActivity;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.ArtistSearchObject;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.SearchArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.TopArtistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistRequestBody;
import com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES.PlaylistTracksRequest;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.AddTracksToPlaylistResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.GenresResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.PlaylistItemsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.PlaylistTrackObject;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.PlaylistsResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SeveralTracksResponse;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.TrackObject;
import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlaylistsApiManager {
    private static PlaylistsApiManager instance;
    private Context context;
    private SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private List<SimplifiedPlaylistObject> usersPlaylists;
    private String userId;

    private PlaylistsApiManager(Context context)
    {
        this.context=context;
        initiateSpotifyApiService();
        usersPlaylists = new ArrayList<>();
        this.userId= SharedPreferencesManager.getUserId(context);
    }
    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
    public static PlaylistsApiManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PlaylistsApiManager.class) {
                if (instance == null) {
                    instance = new PlaylistsApiManager(context);
                }
            }
        }
        return instance;
    }
    public interface PlaylistsCallback {
        void onSuccess(List<SimplifiedPlaylistObject> playlists);
        void onFailure(String errorMessage);
    }
    public List<SimplifiedPlaylistObject> getPlaylistsForUser(String userId_,int offset,final PlaylistsCallback callback)
    {
       
        Call<PlaylistsResponse> callForGetPlaylists = spotifyApiServiceInterface.getUserPlaylists(userId_, 50, offset);
        callForGetPlaylists.enqueue(new Callback<PlaylistsResponse>() {
            @Override
            public void onResponse(Call<PlaylistsResponse> call, Response<PlaylistsResponse> response) {
                if (response.isSuccessful()) {
                    PlaylistsResponse playlistsResponse = response.body();
                    List<SimplifiedPlaylistObject> playlists = playlistsResponse.getItems();
                    Log.e("MY_LOGS", " Playlists: " + playlists);
                    usersPlaylists.addAll(playlists);
                    callback.onSuccess(playlists);
                } else
                {
                    int statusCode = response.code();
                    Log.e("MY_LOGS", "Failed to fetch user playlists. Status code: " + statusCode);
                    callback.onFailure("Failed to fetch user playlists");
                }
            }
            @Override
            public void onFailure(Call<PlaylistsResponse> call, Throwable t) {
                Toast.makeText(context, "Failed get playlist id!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });
        return usersPlaylists;
    }
    public List<SimplifiedPlaylistObject> getPlaylistsForCurrentUser(int offset,final PlaylistsCallback callback)
    {
       
        Call<PlaylistsResponse> callForGetPlaylists = spotifyApiServiceInterface.getUserPlaylists(userId, 50, offset);
        callForGetPlaylists.enqueue(new Callback<PlaylistsResponse>() {
            @Override
            public void onResponse(Call<PlaylistsResponse> call, Response<PlaylistsResponse> response) {
                if (response.isSuccessful()) {
                    PlaylistsResponse playlistsResponse = response.body();
                    List<SimplifiedPlaylistObject> playlists = playlistsResponse.getItems();
                    Log.e("MY_LOGS", " My playlists: " + playlists);
                    usersPlaylists.addAll(playlists);
                    callback.onSuccess(playlists);
                } else
                {
                    int statusCode = response.code();
                    Log.e("MY_LOGS", "Failed to fetch user playlists. Status code: " + statusCode);
                    callback.onFailure("Failed to fetch user playlists");
                }
            }
            @Override
            public void onFailure(Call<PlaylistsResponse> call, Throwable t) {
                Toast.makeText(context, "Failed get playlist id!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });
        return usersPlaylists;
    }

    public List<SimplifiedPlaylistObject> getMyPlaylists()
    {
        //get playlist id
        List<SimplifiedPlaylistObject> myPlaylists=new ArrayList<>();
       
        Call<PlaylistsResponse> callForGetPlaylists = spotifyApiServiceInterface.getMyPlaylists( 50, 0);
        callForGetPlaylists.enqueue(new Callback<PlaylistsResponse>() {
            @Override
            public void onResponse(Call<PlaylistsResponse> call, Response<PlaylistsResponse> response) {
                if (response.isSuccessful())
                {
                    PlaylistsResponse playlistsResponse = response.body();
                    myPlaylists.addAll(playlistsResponse.getItems());
                } else
                {
                    int statusCode = response.code();
                    Log.e("MY_LOGS", "Failed to fetch user playlists. Status code: " + statusCode);
                }
            }
            @Override
            public void onFailure(Call<PlaylistsResponse> call, Throwable t) {
                Toast.makeText(context, "Failed get playlist id!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });

        return myPlaylists;
    }
    public interface GenresCallback {
        void onSuccess(List<String> genres);
        void onFailure(String errorMessage);
    }
    public void getAllGenres(PlaylistsApiManager.GenresCallback callback) {

        Call<GenresResponse> call = spotifyApiServiceInterface.getAvailableGenres();
        call.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.isSuccessful()) {
                    GenresResponse genresResponse = response.body();
                    List<String> genres = genresResponse.getGenres();
                    callback.onSuccess(genres);
                } else {
                    callback.onFailure("API response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                callback.onFailure("Failed to get available genres: " + t.getMessage());
            }
        });
    }
    public interface SearchArtistListener {
        void onSuccess(List<ArtistSearchItem> searchResults);
        void onFailure(String errorMessage);
    }
    public void searchArtist(String q, String type, int limit,int offset, PlaylistsApiManager.SearchArtistListener listener) {


        Call<SearchArtistsResponse> call = spotifyApiServiceInterface.searchArtists( q, type, limit, offset);
        List<ArtistSearchItem> searchResults=new ArrayList<>();
        call.enqueue(new Callback<SearchArtistsResponse>() {
            @Override
            public void onResponse(Call<SearchArtistsResponse> call, Response<SearchArtistsResponse> response) {

                if (response.isSuccessful()) {
                    SearchArtistsResponse searchArtistsResponse = response.body();
                    List<ArtistSearchObject> artistItems = searchArtistsResponse.getArtists().getItems();

                    for (ArtistSearchObject artistForSearch : artistItems) {
                        ArtistSearchItem artistItem = new ArtistSearchItem();

                        artistItem.setArtistName(artistForSearch.getName());
                        artistItem.setId(artistForSearch.getId());
                        if (!artistForSearch.getImages().isEmpty())
                            artistItem.setImageResourceId(artistForSearch.getImages().get(0).getUrl());
                        else artistItem.setNoPhoto();
                        if (artistForSearch.getGenres().size() > 0)
                            artistItem.setGenres(String.join(", ", artistForSearch.getGenres()));
                        else artistItem.setGenres("No information");
                        searchResults.add(artistItem);
                    }
                    listener.onSuccess(searchResults);

                } else {
                    listener.onFailure("Response: " + response.code());
                    Toast.makeText(context, "Api response for search artist not successful!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchArtistsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
                Toast.makeText(context, "Failed search artist request!", Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call for search artists failed", t);
                t.printStackTrace();
            }
        });
    }
    public interface SearchTracksListener {
        void onSuccess(List<TrackSearchItem> searchResults);
        void onFailure(String errorMessage);
    }
    public void searchArtist(String q, String type, int limit,int offset, PlaylistsApiManager.SearchTracksListener listener) {

        List<TrackSearchItem> searchResults=new ArrayList<>();
        Call<SearchTrackResponse> call= spotifyApiServiceInterface.searchTracks(q,type,limit,offset);
        call.enqueue(new Callback<SearchTrackResponse>() {
            @Override
            public void onResponse(Call<SearchTrackResponse> call, Response<SearchTrackResponse> response) {

                if (response.isSuccessful()) {

                    SearchTrackResponse searchTrackResponse = response.body();
                    List<TrackObject> trackItems = searchTrackResponse.getTracks().getItems();

                    for (TrackObject trackItem : trackItems) {
                        TrackSearchItem trackForSearch = new TrackSearchItem();
                        trackForSearch.setSongName(trackItem.getName());
                        trackForSearch.setId(trackItem.getId());
                        trackForSearch.setImageResourceId(trackItem.getAlbum().getImages().get(0).getUrl());
                        StringBuilder artistsNames = new StringBuilder();
                        List<TopArtistsResponse.TopItem> artists = trackItem.getArtists();

                        for (int i = 0; i < artists.size(); i++) {
                            artistsNames.append(artists.get(i).getName());
                            if (i < artists.size() - 1) {
                                artistsNames.append(", ");
                            }
                        }
                        trackForSearch.setArtistName(artistsNames.toString());
                        searchResults.add(trackForSearch);
                    }
                    listener.onSuccess(searchResults);

                } else {
                    Toast.makeText(context, "Api response for search song not successful!", Toast.LENGTH_SHORT).show();
                    Log.e("MY_LOGS", response.toString());
                    listener.onFailure("Response: " +response.code());
                }
            }
            @Override
            public void onFailure(Call<SearchTrackResponse> call, Throwable t) {

                Toast.makeText(context, "Failed search song request!", Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call for song search failed", t);
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }
    public interface AddItemToQueueListener {
        void onAllItemsAdded();
        void onFailure(String errorMessage);
    }
    public void addItemsToPlaybackQueue(List<TrackSearchItem> recommendationTracks, AddItemToQueueListener listener) {

        String uri;

        AtomicInteger successCounter = new AtomicInteger(0);
        int totalTracks = recommendationTracks.size();

        for(TrackSearchItem track: recommendationTracks)
        {
            uri= "spotify:track:" + track.getId();
            Call<Void> callForUserId= spotifyApiServiceInterface.addItemToPlaybackQueue(uri);
            callForUserId.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful())
                    {
                        Log.e("MY_LOGS",  "Nu a mers: " + response.code());
                        listener.onFailure(String.valueOf(successCounter.get()));

                    }
                    else {
                        successCounter.incrementAndGet();
                        Log.e("MY_LOGS", "S-a adaugat : " + successCounter.get());
                        if (successCounter.get() == totalTracks) {//all the tracks are added
                            listener.onAllItemsAdded();
                            Log.e("MY_LOGS", "all handled" + successCounter.get());
                        }
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("MY_LOGS",   "failure " + t+ "la nr : " + String.valueOf(successCounter.get()));
                    listener.onFailure(String.valueOf(successCounter.get()));
                    t.printStackTrace();
                }
            });

        }
    }
    public void addTracksToPlaybackQueue(List<Track> recommendationTracks, AddItemToQueueListener listener) {


        String uri="spotify:track:";

        AtomicInteger successCounter = new AtomicInteger(0);
        int totalTracks = recommendationTracks.size();

        for(Track track: recommendationTracks)
        {
            uri+=track.getId();
            Call<Void> callForUserId= spotifyApiServiceInterface.addItemToPlaybackQueue( uri);
            callForUserId.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful())
                    {
                        Log.e("MY_LOGS",  "nu a mers " + response.code());
                        listener.onFailure(response.message());
                    }
                    else {
                        successCounter.incrementAndGet();
                        Log.e("MY_LOGS", successCounter+  " " + response.code());
                        if (successCounter.get() == totalTracks) {//all the tracks are added
                            listener.onAllItemsAdded();
                        }
                        Log.e("MY_LOGS",String.valueOf(response.code()) + response.message());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("MY_LOGS",   "failure " + t);
                    listener.onFailure(t.getMessage());
                    Log.e("MY_LOGS", "API call failed", t);
                    t.printStackTrace();
                }
            });
            uri="spotify:track:";
        }
    }

    public void createPlaylistForUser(String userId,String playlistName, boolean _public, boolean collaborative,String description,  AddItemToQueueListener listener)
    {
       

        PlaylistRequestBody playlistRequestBody = new PlaylistRequestBody(playlistName,_public, collaborative,description);

        Call<Void> callForCreatePlaylist= spotifyApiServiceInterface.createPlaylistForUser( userId,playlistRequestBody);
        callForCreatePlaylist.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful())
                {
                   listener.onFailure(String.valueOf(response.code()));
                }
                else {
                    listener.onAllItemsAdded();

                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed create playlist request!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });

    }
    public interface GetPlaylistIdForUserByNameAndDescriptionListener {
        void onIdFound(String playlistId);
        void onFailure(String errorMessage);
    }
    public void getPlaylistIdForUserByPlaylistNameAndDescription(String spotifyUserId,int offset, int limit, String playlistName, String playlistDescription, GetPlaylistIdForUserByNameAndDescriptionListener listener )
    {
        //get playlist id
       
        Call<PlaylistsResponse> callForGetPlaylists = spotifyApiServiceInterface.getUserPlaylists(spotifyUserId, limit, offset);
        callForGetPlaylists.enqueue(new Callback<PlaylistsResponse>() {
            @Override
            public void onResponse(Call<PlaylistsResponse> call, Response<PlaylistsResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getItems().size()==0)
                        listener.onFailure("Playlist not found");
                    PlaylistsResponse playlistsResponse = response.body();
                    List<SimplifiedPlaylistObject> playlists = playlistsResponse.getItems();
                    int ok=0;
                    Log.e("MY_LOGS", "CAUTA " +playlistDescription + "  "+ playlistName);
                    for (SimplifiedPlaylistObject playlist : playlists)
                    {
                        Log.e("MY_LOGS", playlist.getName() + "  "+ playlist.getDescription());
                        if(playlist.getName().equals(playlistName) && playlist.getDescription().equals(playlistDescription)) {
                            ok=1;
                            listener.onIdFound(playlist.getId());
                            break;
                        }
                    }
                    if(ok==0)
                    {
                        Log.e("MY_LOGS", "recursion ");
                        getPlaylistIdForUserByPlaylistNameAndDescription(spotifyUserId, offset + limit, limit, playlistName, playlistDescription, listener);
                        //another call for the nextb 50 playlists
                    }
                } else
                {
                    int statusCode = response.code();
                    listener.onFailure("Failed to fetch user playlists. Status code: " + statusCode);
                }
            }
            @Override
            public void onFailure(Call<PlaylistsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
                Toast.makeText(context, "Failed get playlist id!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });

    }
    public void addTracksToPlaylist(String playlistId, PlaylistTracksRequest request, AddItemToQueueListener listener) {


        Call<AddTracksToPlaylistResponse> callForAddToPlaylist = spotifyApiServiceInterface.addTracksToPlaylist( playlistId, request);
        callForAddToPlaylist.enqueue(new Callback<AddTracksToPlaylistResponse>() {
            @Override
            public void onResponse(Call<AddTracksToPlaylistResponse> call, Response<AddTracksToPlaylistResponse> response) {
                if (response.isSuccessful()) {
                    listener.onAllItemsAdded();
                    Log.e("MY_LOGS", "add tracks to playlist :  " + response.code() + " " + response.message());
                } else {
                    Log.e("MY_LOGS", "Didn't work" + String.valueOf(response.code()) + response.message());
                    listener.onFailure("Failed to add songs. Response: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AddTracksToPlaylistResponse> call, Throwable t) {
                listener.onFailure("Failed request to add songs in playlist.");
                Log.e("MY_LOGS", "API call failed", t);
                t.printStackTrace();
            }
        });
    }

    public interface GetPlaylistItemsListener {
        void onItemsReceived(HashMap<TrackObject,String> receivedTracks);
        void onFailure(String errorMessage);
    }
    public void getPlaylistItems(int offset, int limit, String playlistId, GetPlaylistItemsListener listener) {


        HashMap<TrackObject,String> receivedTracks = new HashMap<>();
        Call<PlaylistItemsResponse> callForGetPlaylists = spotifyApiServiceInterface.getPlaylistItems( playlistId, limit, offset);
        callForGetPlaylists.enqueue(new Callback<PlaylistItemsResponse>() {
            @Override
            public void onResponse(Call<PlaylistItemsResponse> call, Response<PlaylistItemsResponse> response) {
                if (response.isSuccessful()) {
                    List<PlaylistTrackObject> tracks = response.body().getItems();
                    for (PlaylistTrackObject playlistTrack: tracks)
                    {
                        if(!playlistTrack.getIs_local())
                            receivedTracks.put(playlistTrack.getTrack(), playlistTrack.getAdded_at());
                    }
                    listener.onItemsReceived(receivedTracks);
                } else {
                    listener.onFailure("Failed to fetch tracks for playlist." + response.code());
                }
            }

            @Override
            public void onFailure(Call<PlaylistItemsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
                Log.e("MY_LOGS", "API call to get items from playlist failed", t);
                t.printStackTrace();
            }
        });
    }

    public interface OnAllTracksFetchedListener {
        void onSeveralTracksReceived(List<Track> tracks);

        void onFailure(String errorMessage);
    }
    public void getSeveralTracks(List<String> ids, OnAllTracksFetchedListener listener )
    {
       
        String idsString = String.join(",", ids);
        List<Track> fetchedTracks = new ArrayList<>();
        Call<SeveralTracksResponse> callForGetPlaylists = spotifyApiServiceInterface.getSeveralTracks( idsString);
        callForGetPlaylists.enqueue(new Callback<SeveralTracksResponse>() {
            @Override
            public void onResponse(Call<SeveralTracksResponse> call, Response<SeveralTracksResponse> response) {
                if (response.isSuccessful()) {
                    SeveralTracksResponse severalTracksResponse = response.body();
                    List<TrackObject> tracks = severalTracksResponse.getTracks();
                    for (TrackObject trackObject : tracks)
                    {
                        List<String> artists = new ArrayList<>();
                        for(TopArtistsResponse.TopItem artist: trackObject.getArtists())
                        {
                            artists.add(artist.getName());
                        }

                        fetchedTracks.add(new Track(trackObject.getName(),trackObject.getId(), artists, trackObject.getAlbum().getImages().get(0).getUrl(),trackObject.getAlbum().getName(),trackObject.getUri()));

                    }
                    listener.onSeveralTracksReceived(fetchedTracks);
                }
                else
                {
                    listener.onFailure("Error fetching tracks");
                }
            }
            @Override
            public void onFailure(Call<SeveralTracksResponse> call, Throwable t) {
                Log.e("MY_LOGS", "API call failed", t);
                listener.onFailure("Error fetching tracks");
                t.printStackTrace();
            }
        });

    }
}
