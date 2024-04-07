package com.example.music_buddy_app2.API_RESPONSES.ARTISTS;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchArtistsResponse {
    @SerializedName("artists")
    private Artists artists;

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }

    public static class Artists {
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
        private List<ArtistSearchObject> items;

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

        public void setItems(List<ArtistSearchObject> items) {
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

        public List<ArtistSearchObject> getItems() {
            return items;
        }
    }
}
