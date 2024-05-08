package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyApiRecommendationsResponse {

    private List<String> recommendations;

    public List<String> getRecommendations() {
        return recommendations;
    }

}
