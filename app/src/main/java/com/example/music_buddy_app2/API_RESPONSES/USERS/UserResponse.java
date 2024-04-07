package com.example.music_buddy_app2.API_RESPONSES.USERS;

import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExplicitContentObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalUrls;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.FollowersObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ImageObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    private String country;

    @SerializedName("display_name")
    private String displayName;

    private String email;

    @SerializedName("explicit_content")
    private ExplicitContentObject explicitContent;

    @SerializedName("external_urls")
    private ExternalUrls externalUrls;

    private FollowersObject followers;

    private String href;

    private String id;

    private List<ImageObject> images;

    private String product;

    private String type;

    private String uri;

    // getters and setters

    public String getCountry() {
        return country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public ExplicitContentObject getExplicitContent() {
        return explicitContent;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public FollowersObject getFollowers() {
        return followers;
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

    public void setExplicitContent(ExplicitContentObject explicitContent) {
        this.explicitContent = explicitContent;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public void setFollowers(FollowersObject followers) {
        this.followers = followers;
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
