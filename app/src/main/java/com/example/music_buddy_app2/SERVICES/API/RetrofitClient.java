package com.example.music_buddy_app2.SERVICES.API;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
public class RetrofitClient {
    //singleton class
    private static final String BASE_URL = "https://api.spotify.com";
    private static final int TIMEOUT_SECONDS = 20;
    private static final String TOKEN_URL = "https://accounts.spotify.com";
    private static final String MY_API_BASE_URL="https://recommendations-dakig7klxq-lm.a.run.app/";
    private static Retrofit retrofit;
    private static Retrofit retrofitToken;
    private static Retrofit myApiRetrofit;
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
    public static Retrofit getMyApiRetrofit()
    {

        if (myApiRetrofit  == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();

            myApiRetrofit  = new Retrofit.Builder()
                .baseUrl(MY_API_BASE_URL)
                    .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return myApiRetrofit ;

    }
}
