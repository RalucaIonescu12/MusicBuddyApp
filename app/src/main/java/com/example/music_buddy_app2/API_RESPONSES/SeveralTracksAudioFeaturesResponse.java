package com.example.music_buddy_app2.API_RESPONSES;

import com.example.music_buddy_app2.API_RESPONSES.AudioFeaturesObjectResponse;
import com.google.gson.annotations.SerializedName;

public class SeveralTracksAudioFeaturesResponse {
    @SerializedName("audio_features")
    private AudioFeaturesObjectResponse[] audioFeatures;

    public AudioFeaturesObjectResponse[] getAudioFeatures() {
        return audioFeatures;
    }
}
