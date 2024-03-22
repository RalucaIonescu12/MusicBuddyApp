package com.example.music_buddy_app2.API_RESPONSES;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackObject {
    @SerializedName("album")
    private AlbumObject album;
    @SerializedName("artists")
    private List<TopArtistsResponse.TopItem> artists;
    @SerializedName("available_markets")
    private List<String> availableMarkets;
    @SerializedName("disc_number")
    private int discNumber;
    @SerializedName("duration_ms")
    private int durationMs;
    @SerializedName("explicit")
    private boolean explicit;
    @SerializedName("external_ids")
    private ExternalIds externalIds;
    @SerializedName("external_urls")
    private ExternalUrls externalUrls;
    @SerializedName("href")
    private String href;
    @SerializedName("id")
    private String id;

    @SerializedName("is_playable")
    private boolean is_playable;

    @SerializedName("linked_from")
    private SearchTrackResponse.LinkedFrom linked_from;

    @SerializedName("restrictions")
    private Restrictions restrictions;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private int popularity;
    @SerializedName("preview_url")  //link to a 30 second prevoiew of the track
    @Nullable
    private String previewUrl;
    @SerializedName("track_number")
    private int trackNumber;
    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;
    @SerializedName("is_local")
    private boolean isLocal;

    public void setAlbum(AlbumObject album) {
        this.album = album;
    }

    public void setArtists(List<TopArtistsResponse.TopItem> artists) {
        this.artists = artists;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
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

    public boolean isIs_playable() {
        return is_playable;
    }

    public void setIs_playable(boolean is_playable) {
        this.is_playable = is_playable;
    }

    public SearchTrackResponse.LinkedFrom getLinked_from() {
        return linked_from;
    }

    public void setLinked_from(SearchTrackResponse.LinkedFrom linked_from) {
        this.linked_from = linked_from;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setPreviewUrl(@Nullable String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public AlbumObject getAlbum() {
        return album;
    }

    public List<TopArtistsResponse.TopItem> getArtists() {
        return artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
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

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public boolean isLocal() {
        return isLocal;
    }
}
