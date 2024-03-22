package com.example.music_buddy_app2.SERVICES;

import android.app.MediaRouteActionProvider;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpotifyApiRecommendationsManager {

    private static SpotifyApiRecommendationsManager instance;
    private int count = 0;
    private int nbrTracks=0;

    private Map<String,Double> audioFeatures;
    private boolean isButtonEnabled = true;
    private Map<String,Double> audioFeatureTemplate ;
    private Map<String,Map<String,Map<String,Map<String, Double>>>> recommendationFilters;

    private SpotifyApiRecommendationsManager()
    {
        recommendationFilters= new HashMap<>(3);
        recommendationFilters.put("seed_artists",new HashMap<>());
        recommendationFilters.put("seed_genres",new HashMap<>());
        recommendationFilters.put("seed_tracks",new HashMap<>());

        audioFeatureTemplate= new HashMap<>();

        audioFeatures= new HashMap<>();
        audioFeatures.put("min_acousticness", 0.0);
        audioFeatures.put("max_acousticness", 1.0);
        audioFeatures.put("target_acousticness", 0.5);
        audioFeatures.put("min_danceability", 0.0);
        audioFeatures.put("max_danceability",1.0);
        audioFeatures.put("target_danceability", 0.5);
        audioFeatures.put("min_popularity", 0.0);
        audioFeatures.put("max_popularity",100.0);
        audioFeatures.put("target_popularity", 50.0);
        audioFeatures.put("min_duration_ms",0.0);
        audioFeatures.put("max_duration_ms", 420000.0);
        audioFeatures.put("target_duration_ms",210000.0);
        audioFeatures.put("min_energy",0.0);
        audioFeatures.put("max_energy",1.0);
        audioFeatures.put("target_energy", 0.5);
        audioFeatures.put("min_instrumentalness",0.0);
        audioFeatures.put("max_instrumentalness",1.0);
        audioFeatures.put("target_instrumentalness", 0.5);
        audioFeatures.put("min_key",-1.0);
        audioFeatures.put("max_key", 11.0);
        audioFeatures.put("target_key",6.0);
        audioFeatures.put("min_liveness",0.0);
        audioFeatures.put("max_liveness",1.0);
        audioFeatures.put("target_liveness", 0.5);
        audioFeatures.put("min_loudness", -60.0);
        audioFeatures.put("max_loudness",0.0);
        audioFeatures.put("target_loudness", -30.0);
        audioFeatures.put("min_mode",0.0);
        audioFeatures.put("max_mode",1.0);
        audioFeatures.put("target_mode",0.0);
        audioFeatures.put("min_speechiness",0.0);
        audioFeatures.put("max_speechiness",1.0);
        audioFeatures.put("target_speechiness", 0.5);
        audioFeatures.put("min_tempo",0.0);
        audioFeatures.put("max_tempo", 200.0);
        audioFeatures.put("target_tempo", 100.0);
        audioFeatures.put("min_time_signature",3.0);
        audioFeatures.put("max_time_signature", 7.0);
        audioFeatures.put("target_time_signature", 5.0);
        audioFeatures.put("min_valence",0.0);
        audioFeatures.put("max_valence",1.0);
        audioFeatures.put("target_valence", 0.5);

    }

    public static SpotifyApiRecommendationsManager getInstance() {
        if (instance == null) {
            synchronized (SpotifyApiRecommendationsManager.class) {
                if (instance == null) {
                    instance = new SpotifyApiRecommendationsManager();
                }
            }
        }
        return instance;
    }
    public void removeFilter(String name)
    {
        if (recommendationFilters.get("seed_tracks")!= null && recommendationFilters.get("seed_tracks").containsKey(name))
        {
            recommendationFilters.get("seed_tracks").remove(name);
            count--;
        }
        else if (recommendationFilters.get("seed_artists")!= null && recommendationFilters.get("seed_artists").containsKey(name))
        {
            recommendationFilters.get("seed_artists").remove(name);
            count--;
        }
        else if (recommendationFilters.get("seed_genres")!= null && recommendationFilters.get("seed_genres").containsKey(name))
        {
            recommendationFilters.get("seed_genres").remove(name);
            count--;
        }
    }
    public void setTargetAudioFeatures()
    {
        Map<String,Map<String,Map<String,Map<String, Double>>>> seeds = getRecFilters();
        Double avg_danceability = 0.0;
        Double avg_instrumentalness = 0.0;
        Double avg_tempo = 0.0;
        Double avg_liveness = 0.0;
        Double avg_valence = 0.0;
        Double avg_energy = 0.0;
        int avg_time_signature=0;
        Double avg_acousticness=0.0;
        int avg_key=-1;
        int avg_duration_ms=0;
        Double avg_loudness=0.0;
        int avg_mode=0;
        Double avg_speechiness=0.0;
        if(!seeds.get("seed_tracks").isEmpty())
        {
            Map<String,Map<String,Map<String, Double>>> seeds_tracks=seeds.get("seed_tracks"); //{title1:{id1:{"danceability":0.3,...}},title1:{id1:{"danceability":0.3,...}},...}

            for(String songTitle: seeds_tracks.keySet())
            {
                Map<String,Map<String, Double>> idAndfeatures = seeds_tracks.get(songTitle);//{id1:{"danceability":0.3,...}}}

                for(String id: idAndfeatures.keySet())
                {
                    Map<String, Double> features = idAndfeatures.get(id);
                    for(String feature: features.keySet()){
                    switch (feature) {
                        case "danceability":
                            avg_danceability+=features.get(feature);
                            break;
                        case "instrumentalness":
                            avg_instrumentalness+=features.get(feature);
                            break;
                        case "tempo":
                            avg_tempo+=features.get(feature);
                            break;
                        case "liveness":
                            avg_liveness+=features.get(feature);
                            break;
                        case "valence":
                            avg_valence+=features.get(feature);
                            break;
                        case "energy":
                            avg_energy+=features.get(feature);
                            break;
                        case "time_signature":
                            avg_time_signature+=features.get(feature);
                            break;
                        case "acousticness":
                            avg_acousticness += features.get(feature);
                            break;
                        case "duration_ms":
                            avg_duration_ms += features.get(feature).intValue();
                            break;
                        case "key":
                            avg_key += features.get(feature).intValue();
                            break;
                        case "loudness":
                            avg_loudness += features.get(feature);
                            break;
                        case "mode":
                            avg_mode += features.get(feature).intValue();
                            break;
                        case "speechiness":
                            avg_speechiness += features.get(feature);
                            break;
                    }
                    }
                }
            }

            int nbr=seeds.get("seed_tracks").size();
            audioFeatures.put("target_valence", Double.parseDouble(String.format("%.5f", (Double) avg_valence / nbr)));
            audioFeatures.put("target_danceability", Double.parseDouble(String.format("%.5f", (Double) avg_danceability / nbr)));
            audioFeatures.put("target_instrumentalness", Double.parseDouble(String.format("%.5f", (Double) avg_instrumentalness / nbr)));
            audioFeatures.put("target_tempo", Double.parseDouble(String.format("%.5f", (Double) avg_tempo / nbr)));
            audioFeatures.put("target_liveness", Double.parseDouble(String.format("%.5f", (Double) avg_liveness / nbr)));
            audioFeatures.put("target_energy",  Double.parseDouble(String.format("%.5f", (Double) avg_energy / nbr)));
            audioFeatures.put("target_time_signature",  (double)(avg_time_signature / nbr));
            audioFeatures.put("target_acousticness", Double.parseDouble(String.format("%.5f", avg_acousticness / nbr)));
            audioFeatures.put("target_duration_ms", (double)(avg_duration_ms / nbr));
            audioFeatures.put("target_key", (double) (avg_key / nbr));
            audioFeatures.put("target_loudness", Double.parseDouble(String.format("%.5f", (double) avg_loudness / nbr)));
            audioFeatures.put("target_mode", (double)(avg_mode / nbr));
            audioFeatures.put("target_speechiness", Double.parseDouble(String.format("%.5f", avg_speechiness / nbr)));
        }
    }
    public String addRecFilter(String filterType,  String filterTitle,String filterId,Double danceability,Double instrumentalness,Double tempo,Double liveness,
                               Double valence,Double energy,int  timeSignature, Double acousticness, Double loudneess, Double speechiness, int duration_ms, int key)
    {
        if(count<5)
        {
            //recommendationFilters: {"seed_tracks": {title1:{id1:{"danceability":0.3,...}},title1:{id1:{"danceability":0.3,...}},...}}
            Map<String,Map<String,Map<String, Double>>> filters = recommendationFilters.get(filterType); //{title1:{id1:{"danceability":0.3,...}},title1:{id1:{"danceability":0.3,...}},...}
            if (filters == null) {
                filters = new HashMap<>();
                recommendationFilters.put(filterType, filters);
            }
            if (filters.containsKey(filterTitle)) return "already_exists";
            else
            {
                if(filterType.equals("seed_tracks")) {
                    audioFeatureTemplate.put("acousticness",0.0);
                    audioFeatureTemplate.put("loudneess",-60.0);
                    audioFeatureTemplate.put("speechiness",0.0);
                    audioFeatureTemplate.put("duration_ms",210000.0);
                    audioFeatureTemplate.put("key",9.0);
                    audioFeatureTemplate.put("danceability",0.0);
                    audioFeatureTemplate.put("instrumentalness",0.0);
                    audioFeatureTemplate.put("tempo",0.0);
                    audioFeatureTemplate.put("liveness",0.0);
                    audioFeatureTemplate.put("valence",0.0);
                    audioFeatureTemplate.put("energy",0.0);
                    audioFeatureTemplate.put("time_signature",3.0);

//                    Map<String, Map<String, Double>> audioFeatures = new HashMap<>(); //id:{"danceability":0.3,...}}

                    Map<String,Double> audioFeaturesValues = new HashMap<>();// {"danceability":0.3, "tempo" : 0.4...}
                    audioFeaturesValues.putAll(audioFeatureTemplate);

                    audioFeaturesValues.put("danceability", (Double)danceability);
                    audioFeaturesValues.put("instrumentalness", (Double)instrumentalness);
                    audioFeaturesValues.put("tempo", (Double)tempo);
                    audioFeaturesValues.put("liveness", (Double)liveness);
                    audioFeaturesValues.put("valence",(Double) valence);
                    audioFeaturesValues.put("energy", (Double)energy);
                    audioFeaturesValues.put("time_signature",(double)timeSignature);
                    audioFeaturesValues.put("acousticness",acousticness);
                    audioFeaturesValues.put("loudneess",loudneess);
                    audioFeaturesValues.put("speechiness",speechiness);
                    audioFeaturesValues.put("duration_ms",(double)duration_ms);
                    audioFeaturesValues.put("key",(double) key);
                    Map<String, Map<String,Double>> audioFeaturess = new HashMap<>();
                    audioFeaturess.put(filterId,audioFeaturesValues);

                    filters.put(filterTitle, audioFeaturess);
                    recommendationFilters.put(filterType,filters);
                }
                else
                {
                    Map<String, Map<String, Double>> val=new HashMap<>();
                    val.put(filterId,null);//{"id": null}} for artists and genres we dont have audio features
                    filters.put(filterTitle,val);//{"title1": {"id1": null} ,"title2": {"id2": null} ...} for artists and genres we dont have audio features
                    recommendationFilters.put(filterType,filters);
                }
                Log.e("FILTERSS",filterType+ " :  " + filters.keySet().toString());
                count++;
                return "filter_added";

            }
        }
        else return "5_filters";

    }

    public void setAudioFeature(String audioFeature, Double value)
    {
        this.audioFeatures.put(audioFeature, value);
    }
    public Double getAudioFeature(String audioFeature)
    {
        return this.audioFeatures.get(audioFeature);
    }
    public void setEnableButton(boolean isEnabled)
    {
        this.isButtonEnabled=isEnabled;
    }
    public boolean getEnableButton()
    {
        return this.isButtonEnabled;
    }
    public void setNbrTracks(int nbr)
    {
        nbrTracks=nbr;
    }
    public int getNbrTracks(){return nbrTracks;}
    public Map<String,Map<String,Map<String,Map<String, Double>>>>  getRecFilters()
    {
        return recommendationFilters;
    }
    public Map<String,Double> getAudioFeatureFields()
    {
        return audioFeatures;
    }
}
