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
         //avoids refreshing simultaneously
        synchronized (this) {
            String currentToken = tokenManager.refreshTokenSync();
            //verifies if the token has already been refreshed by another thread
            if (!response.request().header("Authorization").equals("Bearer " + currentToken)) {
                return newRequestWithAccessToken(response.request(), currentToken);
            }
            //refresh the token
            String newAccessToken = tokenManager.refreshTokenSync();
            if (newAccessToken != null) {
                return newRequestWithAccessToken(response.request(), newAccessToken);
            }
        }
        return null;
    }
    private Request newRequestWithAccessToken(Request request, String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}
