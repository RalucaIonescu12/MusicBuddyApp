package com.example.music_buddy_app2.SERVICES.AUTHORIZATION;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class PKCEUtil {
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        return android.util.Base64.encodeToString(code, android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING);
    }

    public static String generateCodeChallenge(String codeVerifier){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes());
            return android.util.Base64.encodeToString(hash, android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("CODECHALLANGE","No such algorithm exception");
            return null;
        }
    }
}
