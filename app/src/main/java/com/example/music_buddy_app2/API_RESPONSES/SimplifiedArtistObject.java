package com.example.music_buddy_app2.API_RESPONSES;

import com.example.music_buddy_app2.API_RESPONSES.ExternalUrls;
import com.google.gson.annotations.SerializedName;

public class SimplifiedArtistObject {
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
