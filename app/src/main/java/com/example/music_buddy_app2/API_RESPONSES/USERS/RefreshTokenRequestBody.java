package com.example.music_buddy_app2.API_RESPONSES.USERS;

public class RefreshTokenRequestBody {
    private String refresh_token;

    public RefreshTokenRequestBody(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
