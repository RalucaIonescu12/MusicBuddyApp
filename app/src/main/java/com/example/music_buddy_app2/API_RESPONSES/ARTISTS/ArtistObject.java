package com.example.music_buddy_app2.API_RESPONSES.ARTISTS;

import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalUrls;
import com.google.gson.annotations.SerializedName;

public class ArtistObject {
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
