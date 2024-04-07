package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchTrackResponse {
    @SerializedName("tracks")
    private Tracks tracks;
    public static class Tracks {
        @SerializedName("href")
        private String href;
        @SerializedName("limit")
        private int limit;
        @SerializedName("next")
        private String next;
        @SerializedName("offset")
        private int offset;
        @SerializedName("previous")
        private String previous;
        @SerializedName("total")
        private int total;
        @SerializedName("items")
        private List<TrackObject> items;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setItems(List<TrackObject> items) {
            this.items = items;
        }

        public int getLimit() {
            return limit;
        }

        public String getNext() {
            return next;
        }

        public int getOffset() {
            return offset;
        }

        public String getPrevious() {
            return previous;
        }

        public int getTotal() {
            return total;
        }

        public List<TrackObject> getItems() {
            return items;
        }
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }


    public static class LinkedFrom
    {

    }


}
