package com.example.music_buddy_app2.API_RESPONSES;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumObject {
    @SerializedName("album_type")
    private String albumType;
    @SerializedName("total_tracks")
    private int totalTracks;
    @SerializedName("available_markets")
    private List<String> availableMarkets;
    @SerializedName("external_urls")
    private ExternalUrls externalUrls;
    @SerializedName("href")
    private String href;
    @SerializedName("id")
    private String id;
    @SerializedName("images")
    private List<ImageObject> images;
    @SerializedName("name")
    private String name;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("release_date_precision")
    private String releaseDatePrecision;

    @SerializedName("restrictions")
    private Restrictions restrictions;

    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;
    @SerializedName("artists")
    private List<SimplifiedArtistObject> artists;

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(List<ImageObject> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setArtists(List<SimplifiedArtistObject> artists) {
        this.artists = artists;
    }

    public String getAlbumType() {
        return albumType;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public String getHref() {
        return href;
    }

    public String getId() {
        return id;
    }

    public List<ImageObject> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public List<SimplifiedArtistObject> getArtists() {
        return artists;
    }
}
