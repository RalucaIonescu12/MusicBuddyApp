package com.example.music_buddy_app2.API_RESPONSES.OTHERS;

public class AddedByObject {
    private ExternalUrls external_urls;
    private FollowersObject followers;
    private String href;
    private String id; //user id
    private String type;
    private String uri;

    public ExternalUrls getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(ExternalUrls external_urls) {
        this.external_urls = external_urls;
    }

    public FollowersObject getFollowers() {
        return followers;
    }

    public void setFollowers(FollowersObject followers) {
        this.followers = followers;
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
}
