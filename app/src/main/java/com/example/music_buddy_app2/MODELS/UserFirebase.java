package com.example.music_buddy_app2.MODELS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFirebase {

    private String username = "";
    private String email = "";
    private String profileImageUrl = "";
    private Integer playlistsCreatedWithTheApp = 0;
    private String spotifyId = "";
    private String uri = "";
    private List<String> followingIds;
    private List<String> followerIds;

    public UserFirebase() {
        this.playlistsCreatedWithTheApp = 0;
        this.followingIds = new ArrayList<>();
        this.followerIds = new ArrayList<>();
    }

    public UserFirebase(String userId, String username, String email, String profileImageUrl, String spotifyId, String uri, List<String> followingIds, List<String> followerIds) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.spotifyId = spotifyId;
        this.uri = uri;
        this.playlistsCreatedWithTheApp = 0;
        this.followingIds = followingIds != null ? new ArrayList<>(followingIds) : new ArrayList<>();
        this.followerIds = followerIds != null ? new ArrayList<>(followerIds) : new ArrayList<>();
    }

    public UserFirebase(String username, String email, String profileImageUrl, String spotifyId, String uri) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.spotifyId = spotifyId;
        this.uri = uri;
        this.playlistsCreatedWithTheApp = 0;
        this.followingIds = new ArrayList<>();
        this.followerIds = new ArrayList<>();
    }

    // Getters and setters

    public Integer getPlaylistsCreatedWithTheApp() {
        return playlistsCreatedWithTheApp;
    }

    public void setPlaylistsCreatedWithTheApp(Integer playlistsCreatedWithTheApp) {
        this.playlistsCreatedWithTheApp = playlistsCreatedWithTheApp;
    }

    public List<String> getFollowingIds() {
        return followingIds;
    }

    public void setFollowingIds(List<String> followingIds) {
        this.followingIds = followingIds;
    }

    public List<String> getFollowerIds() {
        return followerIds;
    }

    public void setFollowerIds(List<String> followerIds) {
        this.followerIds = followerIds;
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

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    @Override
    public String toString() {
        return "UserFirebase{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", playlistsCreatedWithTheApp=" + playlistsCreatedWithTheApp +
                ", spotifyId='" + spotifyId + '\'' +
                ", uri='" + uri + '\'' +
                ", followingIds=" + followingIds +
                ", followerIds=" + followerIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFirebase that = (UserFirebase) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(profileImageUrl, that.profileImageUrl) &&
                Objects.equals(playlistsCreatedWithTheApp, that.playlistsCreatedWithTheApp) &&
                Objects.equals(spotifyId, that.spotifyId) &&
                Objects.equals(uri, that.uri) &&
                Objects.equals(followingIds, that.followingIds) &&
                Objects.equals(followerIds, that.followerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, profileImageUrl, playlistsCreatedWithTheApp, spotifyId, uri, followingIds, followerIds);
    }
}
