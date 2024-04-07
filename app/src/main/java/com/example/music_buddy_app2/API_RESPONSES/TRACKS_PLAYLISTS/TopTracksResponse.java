package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

import com.example.music_buddy_app2.API_RESPONSES.ARTISTS.ArtistObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalIds;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalUrls;
import com.example.music_buddy_app2.API_RESPONSES.INTERFACES.TopItemInterface;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TopTracksResponse {
    private String href;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;
    private List<TopItem> items;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TopItem> getItems() {
        return items;
    }

    public void setItems(List<TopItem> items) {
        this.items = items;
    }

    // Nested class TopItem
    public static class TopItem implements TopItemInterface {
        private AlbumObject album;
        private List<ArtistObject> artists;
        @SerializedName("available_markets")
        private List<String> availableMarkets;
        @SerializedName("disc_number")
        private int discNumber;
        @SerializedName("duration_ms")
        private int durationMs;
        private boolean explicit;
        @SerializedName("external_ids")
        private ExternalIds externalIds;
        @SerializedName("external_urls")
        private ExternalUrls externalUrls;
        private String href;
        private String id;
        private String name;
        private int popularity;
        @SerializedName("preview_url")
        private String previewUrl;
        @SerializedName("track_number")
        private int trackNumber;
        private String type;
        private String uri;
        @SerializedName("is_local")
        private boolean isLocal;

        public AlbumObject getAlbum() {
            return album;
        }
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPreviewUrl() {
            return previewUrl;
        }

        @Override
        public String getImageUrl() {
            return album.getImages().get(0).getUrl();
        }

        @Override
        public String getAdditionalInfo() {
            return "Popularity: " + popularity;
        }
        public void setAlbum(AlbumObject album) {
            this.album = album;
        }

        public List<ArtistObject> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistObject> artists) {
            this.artists = artists;
        }

        public List<String> getAvailableMarkets() {
            return availableMarkets;
        }

        public void setAvailableMarkets(List<String> availableMarkets) {
            this.availableMarkets = availableMarkets;
        }

        public int getDiscNumber() {
            return discNumber;
        }

        public void setDiscNumber(int discNumber) {
            this.discNumber = discNumber;
        }

        public int getDurationMs() {
            return durationMs;
        }

        public void setDurationMs(int durationMs) {
            this.durationMs = durationMs;
        }

        public boolean isExplicit() {
            return explicit;
        }

        public void setExplicit(boolean explicit) {
            this.explicit = explicit;
        }

        public ExternalIds getExternalIds() {
            return externalIds;
        }

        public void setExternalIds(ExternalIds externalIds) {
            this.externalIds = externalIds;
        }

        public ExternalUrls getExternalUrls() {
            return externalUrls;
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

        public void setName(String name) {
            this.name = name;
        }

        public int getPopularity() {
            return popularity;
        }

        public void setPopularity(int popularity) {
            this.popularity = popularity;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }

        public int getTrackNumber() {
            return trackNumber;
        }

        public void setTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
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

        public boolean isLocal() {
            return isLocal;
        }

        public void setLocal(boolean local) {
            isLocal = local;
        }
        @Override
        public String getArtistName() {
            if (artists != null && artists.size() > 0) {
                return artists.get(0).getName();
            }
            return "";
        }

    }

}