package com.example.music_buddy_app2.API_RESPONSES.OTHERS;

import com.google.gson.annotations.SerializedName;

public class ImageObject {
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
