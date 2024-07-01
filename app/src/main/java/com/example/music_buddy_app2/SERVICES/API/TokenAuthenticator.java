package com.example.music_buddy_app2.SERVICES.API;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private TokenManager tokenManager;
    public TokenAuthenticator(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {

        synchronized (this) {
            String tokenRefreshed = tokenManager.refreshTokenSync();
            if (tokenRefreshed != null) {
                return newRequestWithAccessToken(response.request(), tokenRefreshed);
            }
        }
        return null;

    }
    private Request newRequestWithAccessToken(Request request, String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
    ///
    private boolean isTokenStillValid(String token) {
      return tokenManager.isTokenValid();
    }
}
