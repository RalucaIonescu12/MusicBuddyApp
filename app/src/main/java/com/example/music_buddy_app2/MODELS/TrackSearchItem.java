package com.example.music_buddy_app2.MODELS;

public class TrackSearchItem {
    private String songName;
    private String id;
    private String artistName;
    private String imageResourceId;

    public TrackSearchItem(String songName, String artistName, String imageResourceId, String id) {
        this.songName = songName;
        this.artistName = artistName;
        this.imageResourceId = imageResourceId;
        this.id = id;
    }
    public TrackSearchItem(){}
    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}

