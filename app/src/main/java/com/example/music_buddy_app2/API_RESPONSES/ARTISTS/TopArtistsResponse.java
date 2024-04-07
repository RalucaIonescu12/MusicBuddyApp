package com.example.music_buddy_app2.API_RESPONSES.ARTISTS;

import com.example.music_buddy_app2.API_RESPONSES.INTERFACES.TopItemInterface;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopArtistsResponse {
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
        private ExternalUrls external_urls;
        private Followers followers;
        private List<String> genres;
        private String href;
        private String id;
        private List<Image> images;
        private String name;
        private int popularity;
        private String type;
        private String uri;

        public ExternalUrls getExternal_urls() {
            return external_urls;
        }

        public void setExternal_urls(ExternalUrls external_urls) {
            this.external_urls = external_urls;
        }
        @Override
        public String getArtistName()
        {
            return this.getName();
        }
        public Followers getFollowers() {
            return followers;
        }

        public void setFollowers(Followers followers) {
            this.followers = followers;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
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

        public void setName(String name) {
            this.name = name;
        }

        public int getPopularity() {
            return popularity;
        }

        public void setPopularity(int popularity) {
            this.popularity = popularity;
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
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPreviewUrl() {
            return null; // You may need to implement this based on your data model
        }

        @Override
        public String getImageUrl() {
            return images.get(0).getUrl();
        }

        @Override
        public String getAdditionalInfo() {
            return "Popularity: " + popularity;
        }
    }

    // Nested classes ExternalUrls, Followers, and Image
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

    public static class Followers {
        private String href;
        private int total;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
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
