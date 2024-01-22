package com.example.music_buddy_app2.API_RESPONSES;

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
        private Album album;
        private List<Artist> artists;
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

        public Album getAlbum() {
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
        public void setAlbum(Album album) {
            this.album = album;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public void setArtists(List<Artist> artists) {
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
        public static class Album {
            @SerializedName("album_type")
            private String albumType;
            @SerializedName("total_tracks")
            private int totalTracks;
            @SerializedName("available_markets")
            private List<String> availableMarkets;
            @SerializedName("external_urls")
            private ExternalUrls externalUrls;
            private String href;
            private String id;
            private List<Image> images;
            private String name;
            @SerializedName("release_date")
            private String releaseDate;
            @SerializedName("release_date_precision")
            private String releaseDatePrecision;
            private String type;
            private String uri;
            private List<Artist> artists;

            public String getAlbumType() {
                return albumType;
            }

            public void setAlbumType(String albumType) {
                this.albumType = albumType;
            }

            public int getTotalTracks() {
                return totalTracks;
            }

            public void setTotalTracks(int totalTracks) {
                this.totalTracks = totalTracks;
            }

            public List<String> getAvailableMarkets() {
                return availableMarkets;
            }

            public void setAvailableMarkets(List<String> availableMarkets) {
                this.availableMarkets = availableMarkets;
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

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getReleaseDate() {
                return releaseDate;
            }

            public void setReleaseDate(String releaseDate) {
                this.releaseDate = releaseDate;
            }

            public String getReleaseDatePrecision() {
                return releaseDatePrecision;
            }

            public void setReleaseDatePrecision(String releaseDatePrecision) {
                this.releaseDatePrecision = releaseDatePrecision;
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

            public List<Artist> getArtists() {
                return artists;
            }

            public void setArtists(List<Artist> artists) {
                this.artists = artists;
            }
        }

        public static class Artist {
            @SerializedName("external_urls")
            private ExternalUrls externalUrls;
            private String href;
            private String id;
            private String name;
            private String type;
            private String uri;

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


        public static class ExternalUrls {
            @SerializedName("spotify")
            private String spotifyUrl;

            public String getSpotifyUrl() {
                return spotifyUrl;
            }

            public void setSpotifyUrl(String spotifyUrl) {
                this.spotifyUrl = spotifyUrl;
            }

        }

        public static class ExternalIds {
            private String isrc;

            public String getIsrc() {
                return isrc;
            }

            public void setIsrc(String isrc) {
                this.isrc = isrc;
            }

        }

        public static class Image {
            private String url;
            private int height;
            private int width;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }

    }

}