package com.example.music_buddy_app2.MODELS;

import java.util.ArrayList;
import java.util.List;

public class Track {
    private String songName;
    private String id;
    private List<String> artists=new ArrayList<>();
    private String imageResourceId;
    private String album;
    private String uri;

    public Track(String songName, String id, List<String> artists, String imageResourceId, String album, String uri) {
        this.songName = songName;
        this.id = id;
        this.artists.addAll(artists);
        this.imageResourceId = imageResourceId;
        this.album = album;
        this.uri = uri;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }
}
