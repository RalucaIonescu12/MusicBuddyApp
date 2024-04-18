package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

import com.example.music_buddy_app2.API_RESPONSES.OTHERS.AddedByObject;

public class PlaylistTrackObject
{
    private String added_at; //date-time
    private AddedByObject added_by;
    private boolean is_local;
    private TrackObject track;

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public AddedByObject getAdded_by() {
        return added_by;
    }

    public void setAdded_by(AddedByObject added_by) {
        this.added_by = added_by;
    }

    public boolean getIs_local() {
        return is_local;
    }

    public void setIs_local(boolean is_local) {
        this.is_local = is_local;
    }

    public TrackObject getTrack() {
        return track;
    }

    public void setTrack(TrackObject track) {
        this.track = track;
    }
}
