package com.example.music_buddy_app2.MODELS;

public class AccordionItemSpotifyRec {
    public static final int FORM_TYPE_INPUT = 1;
    public static final int FORM_TYPE_SEEKBAR = 2;

    private String title;
    private String details;
    private int formType;

    public AccordionItemSpotifyRec(String title, String details, int formType) {
        this.title = title;
        this.details = details;
        this.formType = formType;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getFormType() {
        return formType;
    }
}
