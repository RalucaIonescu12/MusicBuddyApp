package com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS;

import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ExternalUrls;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.FollowersObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.ImageObject;
import com.example.music_buddy_app2.API_RESPONSES.OTHERS.OwnerObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimplifiedPlaylistObject {
    private boolean collaborative;
    private String description;
    private ExternalUrls external_urls;
    private String href;
    private String id;
    private List<ImageObject> images;
    private FollowersObject followers;
    private String name;
    private OwnerObject owner;
    @SerializedName("public")
    private boolean _public;
    @SerializedName("snapshot_id")
    private String snapshotId;
    private TracksFromPlaylistObject tracks;
    private String type;
    private String uri;

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

    public ExternalUrls getExternalUrls() {
        return external_urls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.external_urls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ImageObject> getImages() {
        return images;
    }

    public void setImages(List<ImageObject> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OwnerObject getOwner() {
        return owner;
    }

    public void setOwner(OwnerObject owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean aPublic) {
        _public = aPublic;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public TracksFromPlaylistObject getTracks() {
        return tracks;
    }

    public void setTracks(TracksFromPlaylistObject tracks) {
        this.tracks = tracks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public void setDisplayName(String displayName)
    {
        owner.setDisplayName(displayName);
    }
}
