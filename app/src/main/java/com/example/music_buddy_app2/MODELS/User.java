package com.example.music_buddy_app2.MODELS;

public class User {

    private String username;
    private String email;
    private String profileImageUrl;
    private Integer totalScore;
    private Integer totalWins;
    private Integer totalGamesPlayed;
    private String SpotifyId;
    public User() {
        this.totalScore = 0;
        this.totalWins = 0;
        this.totalGamesPlayed = 0;
    }

    public User(String userId, String username, String email, String displayName, String profileImageUrl, String SpotifyId) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.SpotifyId = SpotifyId;
        this.totalWins = 0;
        this.totalScore = 0;
        this.totalGamesPlayed = 0;
        // Initialize more fields as needed
    }

    public User(String username, String email, String profileImageUrl, String spotifyId) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        SpotifyId = spotifyId;
        this.totalWins = 0;
        this.totalScore = 0;
        this.totalGamesPlayed = 0;
    }
// Getter and setter methods for each field

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
        return "User {" +
                "username = '" + username + '\'' +
                ", email = '" + email + '\'' +
                ", profileImageUrl = '" + profileImageUrl + '\'' +
                ", totalWins = '" + totalWins + '\'' +
                ", totalGamesPlayed = '" + totalGamesPlayed + '\'' +
                ", totalScore = '" + totalScore + '\'' +
                '}';
    }
}
