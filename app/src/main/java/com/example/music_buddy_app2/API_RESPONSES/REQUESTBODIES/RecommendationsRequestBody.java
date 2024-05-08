package com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES;

import java.util.List;

public class RecommendationsRequestBody {
    private List<PlaylistItem> items;

    public static class PlaylistItem {
        private String artist;
        private String name;
        private String id;
        private String url;
        private String date_added;

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDate_added() {
            return date_added;
        }

        public void setDate_added(String date_added) {
            this.date_added = date_added;
        }
    }

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }
}
