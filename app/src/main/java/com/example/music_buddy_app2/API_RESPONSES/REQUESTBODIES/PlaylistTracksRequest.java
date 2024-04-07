package com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES;

import java.util.List;

public class PlaylistTracksRequest {
    private List<String> uris;
    private Integer position;
    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }


    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }


}
