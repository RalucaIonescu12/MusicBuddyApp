package com.example.music_buddy_app2.API_RESPONSES.OTHERS;

import com.google.gson.annotations.SerializedName;

public class ExternalUrls {
    @SerializedName("spotify")
    private String spotify;

    public String getSpotify() {
        return spotify; //external urls for the item
    }
}
