package com.example.music_buddy_app2.MANAGERS;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.music_buddy_app2.FRAGMENTS.AudioFeaturesDialogFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPreferencesManager {
    private static final String SPOTIFY_PREFERENCES = "SpotifyPreferences";

    private static SharedPreferences getEncryptedSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                SPOTIFY_PREFERENCES,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void saveToken(Context context, String token) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("AccessToken", token);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveFirebaseCustomToken(Context context, String token) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CustomToken", token);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateNbrPlaylists(Context context, int nbr) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("NumberGeneratedPlaylists", nbr);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveRefreshToken(Context context, String token) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("RefreshToken", token);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getString("AccessToken", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int getNbrGeneratedPlaylists(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getInt("NumberGeneratedPlaylists", 0);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static String getRefreshToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getString("RefreshToken", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveUserId(Context context, String userId) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserId", userId);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserId(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getString("UserId", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getCustomToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getString("CustomToken", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveExpiryTime(Context context, long expiryTime) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("ExpiryTime", expiryTime);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static long getExpiryTime(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            return sharedPreferences.getLong("ExpiryTime", 0);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static void clearToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("AccessToken");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearCustomToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("CustomToken");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearRefreshToken(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("RefreshToken");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }


    public static void clearExpiryTime(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("ExpiryTime");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearUserId(Context context) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("UserId");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
