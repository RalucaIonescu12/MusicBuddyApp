package com.example.music_buddy_app2.API_RESPONSES.AUDIO_FEATURES;

import com.example.music_buddy_app2.API_RESPONSES.AUDIO_FEATURES.AudioFeaturesObjectResponse;
import com.google.gson.annotations.SerializedName;

public class SeveralTracksAudioFeaturesResponse {
    @SerializedName("audio_features")
    private AudioFeaturesObjectResponse[] audioFeatures;

    public AudioFeaturesObjectResponse[] getAudioFeatures() {
        return audioFeatures;
    }
}
