package com.example.music_buddy_app2.API_RESPONSES;

import com.google.gson.annotations.SerializedName;

public class ExternalIds {
    @SerializedName("isrc")
    private String isrc;

    public String getIsrc() {
        return isrc;
    }
}
