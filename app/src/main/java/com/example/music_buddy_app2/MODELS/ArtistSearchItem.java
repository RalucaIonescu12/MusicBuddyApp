package com.example.music_buddy_app2.MODELS;

public class ArtistSearchItem {
    private String id;
    private String artistName;
    private String imageResourceId;
    private String genres;

    public ArtistSearchItem(String artistName, String imageResourceId, String id, String genres) {
        this.artistName = artistName;
        this.imageResourceId = imageResourceId;
        this.id = id;
        this.genres=genres;
    }
    public ArtistSearchItem(){
        this.artistName = "Artist Name";
        this.imageResourceId = "app/src/main/res/drawable/no_photo_available.png";
        this.id = "id";
        this.genres="No information";}

    public void setNoPhoto()
    {
        this.imageResourceId ="app/src/main/res/drawable/no_photo_available.png";
    }
    public String getArtistName() {
        return artistName;
    }

    public String getImageResourceId() {
        return imageResourceId;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }


    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
