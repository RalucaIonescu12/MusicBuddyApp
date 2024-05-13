package com.example.music_buddy_app2.API_RESPONSES.USERS;

public class CodeRequestBody {
    private String code;

    public CodeRequestBody(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
