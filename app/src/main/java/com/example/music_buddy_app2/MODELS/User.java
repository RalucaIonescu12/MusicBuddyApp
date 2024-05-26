package com.example.music_buddy_app2.MODELS;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username="";
    private String email="";
    private String profileImageUrl="";
    private Integer playlistsCreatedWithTheApp=0;
    private String SpotifyId="";
    private String uri="";
    private List<String> followingIds;
    private List<String> followersIds;
    private boolean isSelected=false;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public User() {
        this.playlistsCreatedWithTheApp = 0;
        this.followingIds=new ArrayList<>();
        this.followersIds=new ArrayList<>();
    }
    public User(String userId, String username, String email, String displayName, String profileImageUrl, String SpotifyId, String uri, List<String> followingIds,List<String> followerIds) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.SpotifyId = SpotifyId;
        this.playlistsCreatedWithTheApp= 0;
        this.followingIds=new ArrayList<>();
        this.followingIds.addAll(followingIds);
        this.followersIds=new ArrayList<>();
        this.followersIds.addAll(followerIds);
    }
    public User(String username, String email, String profileImageUrl, String spotifyId, String uri) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        SpotifyId = spotifyId;
        this.playlistsCreatedWithTheApp = 0;
        this.uri=uri;
        this.followingIds=new ArrayList<>();
        this.followersIds=new ArrayList<>();
    }

// Getter and setter methods for each field

    public Integer getPlaylistsCreatedWithTheApp() {
        return playlistsCreatedWithTheApp;
    }

    public void setPlaylistsCreatedWithTheApp(Integer playlistsCreatedWithTheApp) {
        this.playlistsCreatedWithTheApp = playlistsCreatedWithTheApp;
    }

    public List<String> getFollowersIds() {
        return followersIds;
    }

    public void setFollowersIds(List<String> followersIds) {
        this.followersIds = followersIds;
    }



    public List<String> getFollowingIds() {
        return followingIds;
    }

    public void setFollowingIds(List<String> followingIds) {
        this.followingIds = followingIds;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


    public void setSpotifyId(String spotifyId) {
        SpotifyId = spotifyId;
    }



    public String getSpotifyId() {
        return SpotifyId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", playlistsCreatedWithTheApp=" + playlistsCreatedWithTheApp +
                ", SpotifyId='" + SpotifyId + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
