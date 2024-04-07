package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

public class TracksFromPlaylistObject {
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
}
