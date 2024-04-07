package com.example.music_buddy_app2.SERVICES.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //singleton class
    private static final String BASE_URL = "https://api.spotify.com";
    private static final String TOKEN_URL = "https://accounts.spotify.com";
    private static Retrofit retrofit;
    private static Retrofit retrofitToken;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //uses Gson for serialization/deserialization
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getRetrofitTokenInstance() {
        if (retrofitToken  == null) {
            retrofitToken  = new Retrofit.Builder()
                    .baseUrl(TOKEN_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //uses Gson for serialization/deserialization
                    .build();
        }
        return retrofitToken ;
    }
}
