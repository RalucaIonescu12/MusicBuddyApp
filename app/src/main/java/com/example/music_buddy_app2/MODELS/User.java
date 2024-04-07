package com.example.music_buddy_app2.MODELS;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username="";
    private String email="";
    private String profileImageUrl="";
    private Integer totalScore;
    private Integer totalWins;
    private Integer totalGamesPlayed;
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
        this.totalScore = 0;
        this.totalWins = 0;
        this.totalGamesPlayed = 0;
        this.followingIds=new ArrayList<>();
        this.followersIds=new ArrayList<>();
    }
    public User(String userId, String username, String email, String displayName, String profileImageUrl, String SpotifyId, String uri, List<String> followingIds,List<String> followerIds) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.SpotifyId = SpotifyId;
        this.totalWins = 0;
        this.totalScore = 0;
        this.totalGamesPlayed = 0;
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
        this.totalWins = 0;
        this.totalScore = 0;
        this.totalGamesPlayed = 0;
        this.uri=uri;
        this.followingIds=new ArrayList<>();
        this.followersIds=new ArrayList<>();
    }
// Getter and setter methods for each field

    public List<String> getFollowerIds() {
        return followersIds;
    }

    public void setFollowerIds(List<String> followerIds) {
        this.followersIds = followerIds;
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

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public void setTotalWins(Integer totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalGamesPlayed(Integer totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public void setSpotifyId(String spotifyId) {
        SpotifyId = spotifyId;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Integer getTotalWins() {
        return totalWins;
    }

    public Integer getTotalGamesPlayed() {
        return totalGamesPlayed;
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
                ", totalScore=" + totalScore +
                ", totalWins=" + totalWins +
                ", totalGamesPlayed=" + totalGamesPlayed +
                ", SpotifyId='" + SpotifyId + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
