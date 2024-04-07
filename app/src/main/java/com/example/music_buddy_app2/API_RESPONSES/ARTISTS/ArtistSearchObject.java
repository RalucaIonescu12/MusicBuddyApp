package com.example.music_buddy_app2.API_RESPONSES.ARTISTS;

import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalUrls;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.FollowersObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ImageObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistSearchObject {
    @SerializedName("external_urls")
    private ExternalUrls externalUrls;
    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
    private int popularity;
    private FollowersObject followers;
    private List<String> genres;
    private List<ImageObject> images;

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public FollowersObject getFollowers() {
        return followers;
    }

    public void setFollowers(FollowersObject followers) {
        this.followers = followers;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<ImageObject> getImages() {
        return images;
    }

    public void setImages(List<ImageObject> images) {
        this.images = images;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
