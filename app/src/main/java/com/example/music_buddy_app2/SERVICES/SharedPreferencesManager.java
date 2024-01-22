package com.example.music_buddy_app2.SERVICES;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String SPOTIFY_PREFERENCES = "SpotifyPreferences";

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AccessToken", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("AccessToken", null);
    }
}
