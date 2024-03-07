package com.example.music_buddy_app2.API_RESPONSES;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchTrackResponse {
    @SerializedName("tracks")
    private Tracks tracks;
    public static class Tracks {
        @SerializedName("href")
        private String href;
        @SerializedName("limit")
        private int limit;
        @SerializedName("next")
        private String next;
        @SerializedName("offset")
        private int offset;
        @SerializedName("previous")
        private String previous;
        @SerializedName("total")
        private int total;
        @SerializedName("items")
        private List<TrackItem> items;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setItems(List<TrackItem> items) {
            this.items = items;
        }

        public int getLimit() {
            return limit;
        }

        public String getNext() {
            return next;
        }

        public int getOffset() {
            return offset;
        }

        public String getPrevious() {
            return previous;
        }

        public int getTotal() {
            return total;
        }

        public List<TrackItem> getItems() {
            return items;
        }
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public static class TrackItem {
        @SerializedName("album")
        private Album album;
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
        private LinkedFrom linked_from;

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

        public void setAlbum(Album album) {
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

        public LinkedFrom getLinked_from() {
            return linked_from;
        }

        public void setLinked_from(LinkedFrom linked_from) {
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

        public Album getAlbum() {
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
    public static class LinkedFrom
    {

    }
    public static class Album {
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
        private List<Image> images;
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

        public void setImages(List<Image> images) {
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

        public List<Image> getImages() {
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
    public static class Restrictions
    {
        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        @SerializedName("reason")
        private String reason;  //market/product/explicit

    }
    public static class ExternalUrls {
        @SerializedName("spotify")
        private String spotify;

        public String getSpotify() {
            return spotify; //external urls for the artist
        }

    }

    public static class ExternalIds {
        @SerializedName("isrc")
        private String isrc;

        public String getIsrc() {
            return isrc;
        }
    }

    public static class Image {
        @SerializedName("height")
        private int height;
        @SerializedName("url")
        private String url;
        @SerializedName("width")
        private int width;

        public int getHeight() {
            return height;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class SimplifiedArtistObject {
        @SerializedName("external_urls")
        private ExternalUrls externalUrls;
        @SerializedName("href")
        private String href;
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private String type;
        @SerializedName("uri")
        private String uri;

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

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }
    }


}
