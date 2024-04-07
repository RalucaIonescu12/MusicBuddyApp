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
    public static void saveCodeVerifier(Context context, String code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CodeVerifier", code);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("AccessToken", null);
    }
    public static String getCodeVerifier(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPOTIFY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("CodeVerifier", null);
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
}
