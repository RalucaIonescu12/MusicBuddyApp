package com.example.music_buddy_app2.API_RESPONSES;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GenresResponse {

    @SerializedName("genres")
    private List<String> genres;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}