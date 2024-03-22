package com.example.music_buddy_app2.API_RESPONSES;

import java.util.List;

public class SpotifyRecommendationsResponse {

    List<RecommendationSeedObject> seeds;
    List<TrackObject> tracks;

    public List<RecommendationSeedObject> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<RecommendationSeedObject> seeds) {
        this.seeds = seeds;
    }

    public List<TrackObject> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackObject> tracks) {
        this.tracks = tracks;
    }
}
