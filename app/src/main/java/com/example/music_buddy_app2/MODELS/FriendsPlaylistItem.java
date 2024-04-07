package com.example.music_buddy_app2.MODELS;

public class FriendsPlaylistItem {
    private String name;
    private int imageResId;
    private String userName;

    public FriendsPlaylistItem(String name, int imageResId, String userName) {
        this.name = name;
        this.imageResId = imageResId;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
