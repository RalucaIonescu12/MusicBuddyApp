package com.example.music_buddy_app2.API_RESPONSES.OTHERS;

import com.google.gson.annotations.SerializedName;

public class Restrictions {
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @SerializedName("reason")
    private String reason;  //market/product/explicit
}
