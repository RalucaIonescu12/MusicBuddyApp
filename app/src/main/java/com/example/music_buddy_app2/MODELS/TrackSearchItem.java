package com.example.music_buddy_app2.MODELS;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.music_buddy_app2.R;

public class TrackSearchItem {
    private String songName;
    private String id;
    private String artistName;
    private String imageResourceId;

    private Double acousticness;
    private int duration_ms;
    private int key;
    private Double loudness;
    private int mode;
    private Double speechiness;
    private Double valenceValue;
    private Double instrumentalnessValue ;
    private Double danceabilityValue ;
    private Double tempoValue ;
    private Double livenessValue ;
    private Double energyValue ;
    private int timeSignature;


    public TrackSearchItem(String songName, String artistName, String imageResourceId, String id) {
        this.songName = songName;
        this.artistName = artistName;
        this.imageResourceId = imageResourceId;
        this.id = id;
        this.valenceValue =0.0;
        this.instrumentalnessValue=0.0 ;
        this.danceabilityValue =0.0;
        this.tempoValue=0.0 ;
        this.livenessValue =0.0;
        this.energyValue =0.0;
        this.timeSignature=3;
        this.acousticness =0.0;
        this.duration_ms =0;
        this.key =0;
        this.loudness =-60.0;
        this.mode =0;
        this.speechiness =0.0;
    }

    public Double getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(Double acousticness) {
        this.acousticness = acousticness;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Double getLoudness() {
        return loudness;
    }

    public void setLoudness(Double loudness) {
        this.loudness = loudness;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(Double speechiness) {
        this.speechiness = speechiness;
    }

    public Double getValenceValue() {
        return valenceValue;
    }

    public void setValenceValue(Double valenceValue) {
        this.valenceValue = valenceValue;
    }

    public Double getInstrumentalnessValue() {
        return instrumentalnessValue;
    }

    public void setInstrumentalnessValue(Double instrumentalnessValue) {
        this.instrumentalnessValue = instrumentalnessValue;
    }

    public Double getDanceabilityValue() {
        return danceabilityValue;
    }

    public void setDanceabilityValue(Double danceabilityValue) {
        this.danceabilityValue = danceabilityValue;
    }

    public Double getTempoValue() {
        return tempoValue;
    }

    public void setTempoValue(Double tempoValue) {
        this.tempoValue = tempoValue;
    }

    public Double getLivenessValue() {
        return livenessValue;
    }

    public void setLivenessValue(Double livenessValue) {
        this.livenessValue = livenessValue;
    }

    public Double getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(Double energyValue) {
        this.energyValue = energyValue;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public TrackSearchItem(){}
    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    @Override
    public String toString() {
        return "TrackSearchItem{" +
                "songName='" + songName + '\'' +
                ", id='" + id + '\'' +
                ", artistName='" + artistName + '\'' +
                ", imageResourceId='" + imageResourceId + '\'' +
                ", acousticness=" + acousticness +
                ", duration_ms=" + duration_ms +
                ", key=" + key +
                ", loudness=" + loudness +
                ", mode=" + mode +
                ", speechiness=" + speechiness +
                ", valenceValue=" + valenceValue +
                ", instrumentalnessValue=" + instrumentalnessValue +
                ", danceabilityValue=" + danceabilityValue +
                ", tempoValue=" + tempoValue +
                ", livenessValue=" + livenessValue +
                ", energyValue=" + energyValue +
                ", timeSignature=" + timeSignature +
                '}';
    }
}

