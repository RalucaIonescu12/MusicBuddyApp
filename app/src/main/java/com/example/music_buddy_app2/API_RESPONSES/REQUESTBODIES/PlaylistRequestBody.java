package com.example.music_buddy_app2.API_RESPONSES.REQUESTBODIES;

public class PlaylistRequestBody {
    public PlaylistRequestBody(String name, boolean _public, boolean collaborative, String description) {
        this.name = name;
        this._public = _public;
        this.collaborative = collaborative;
        this.description = description;
    }

    private String name;
    private boolean _public;
    private boolean collaborative;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean is_public() {
        return _public;
    }

    public void set_public(boolean _public) {
        this._public = _public;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
