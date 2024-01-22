package com.example.music_buddy_app2.API_RESPONSES;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    private String country;

    @SerializedName("display_name")
    private String displayName;

    private String email;

    @SerializedName("explicit_content")
    private ExplicitContent explicitContent;

    @SerializedName("external_urls")
    private ExternalUrls externalUrls;

    private Followers followers;

    private String href;

    private String id;

    private List<Image> images;

    private String product;

    private String type;

    private String uri;

    // getters and setters

    public static class ExplicitContent {
        @SerializedName("filter_enabled")
        private boolean filterEnabled;

        @SerializedName("filter_locked")
        private boolean filterLocked;

        public boolean isFilterEnabled() {
            return filterEnabled;
        }

        public boolean isFilterLocked() {
            return filterLocked;
        }
        public void setFilterEnabled(boolean filterEnabled) {
            this.filterEnabled = filterEnabled;
        }

        public void setFilterLocked(boolean filterLocked) {
            this.filterLocked = filterLocked;
        }
// getters and setters
    }

    public static class ExternalUrls {
        private String spotify;

        public String getSpotify() {
            return spotify;
        }

        public void setSpotify(String spotify) {
            this.spotify = spotify;
        }
// getters and setters
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
// getters and setters
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

    public String getCountry() {
        return country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public ExplicitContent getExplicitContent() {
        return explicitContent;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public Followers getFollowers() {
        return followers;
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

    public String getProduct() {
        return product;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExplicitContent(ExplicitContent explicitContent) {
        this.explicitContent = explicitContent;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
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

    public void setProduct(String product) {
        this.product = product;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


}
