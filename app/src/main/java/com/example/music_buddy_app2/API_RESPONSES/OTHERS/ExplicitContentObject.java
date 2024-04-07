package com.example.music_buddy_app2.API_RESPONSES.OTHERS;

import com.google.gson.annotations.SerializedName;

public class ExplicitContentObject {
    @SerializedName("filter_enabled")
    private boolean filterEnabled;

    @SerializedName("filter_locked")
    private boolean filterLocked;

    public boolean isFilterEnabled() {
        return filterEnabled;
    }

    public boolean isFilterLocked() {
        return filterLocked;
    }
    public void setFilterEnabled(boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }

    public void setFilterLocked(boolean filterLocked) {
        this.filterLocked = filterLocked;
    }

}