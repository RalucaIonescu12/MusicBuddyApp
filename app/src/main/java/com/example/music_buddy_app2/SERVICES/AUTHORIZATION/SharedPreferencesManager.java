package com.example.music_buddy_app2.SERVICES.AUTHORIZATION;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.music_buddy_app2.FRAGMENTS.AudioFeaturesDialogFragment;

public class SharedPreferencesManager {
    private static final String SPOTIFY_PREFERENCES = "SpotifyPreferences";

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AccessToken", token);
        editor.apply();
    }
    public static void saveRefreshToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RefreshToken", token);
        editor.apply();
    }


    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("AccessToken", null);
    }
    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("RefreshToken", null);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserId", userId);
        editor.apply();
    }
    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserId", null);
    }

    public static void saveExpiryTime(Context context, long expiryTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("ExpiryTime", expiryTime);
        editor.apply();
    }

    public static long getExpiryTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("ExpiryTime", 0);
    }
}
