package com.example.music_buddy_app2.SERVICES.API;

import android.media.session.MediaSession;

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
    private static final String SPOTIFY_URL = "https://api.spotify.com";
    private static final int TIMEOUT_SECONDS = 40;
    private static final String TOKEN_API_URL="https://token-dakig7klxq-lm.a.run.app";
    private static final String RECS_API_URL="https://recs-dakig7klxq-lm.a.run.app";
    private static Retrofit spotifyRetrofit;
    private static Retrofit myApiRecsRetrofit;
    private static Retrofit myApiRetrofit;
//    public static Retrofit getRetrofitInstance() {
//        if (retrofit == null) {
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(new Interceptor() { // adds token to each request
//                        @Override
//                        public Response intercept(Interceptor.Chain chain) throws IOException {
//                            Request originalRequest = chain.request();
//
//                            String accessToken = TokenManager.getInstance().getAccessToken();
//                            Request newRequest = originalRequest.newBuilder()
//                                    .header("Authorization", "Bearer " + accessToken)
//                                    .build();
//                            return chain.proceed(newRequest);
//                        }
//                    })
//                    .build();
//
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create()) // Uses Gson for serialization/deserialization
//                    .build();
//        }
//        return retrofit;
//    }
//    public static Retrofit getRetrofitInstance() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create()) //uses Gson for serialization/deserialization
//                    .build();
//        }
//        return retrofit;
//    }
//    public static Retrofit getRetrofitTokenInstance() {
//        if (retrofitToken  == null) {
//            retrofitToken  = new Retrofit.Builder()
//                    .baseUrl(TOKEN_URL)
//                    .addConverterFactory(GsonConverterFactory.create()) //uses Gson for serialization/deserialization
//                    .build();
//        }
//        return retrofitToken ;
//    }


    public static Retrofit getRetrofitInstance() {
        if (spotifyRetrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            String accessToken = TokenManager.getInstance().getAccessToken();
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + accessToken)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .authenticator(new TokenAuthenticator(TokenManager.getInstance()))
                    .build();
            spotifyRetrofit = new Retrofit.Builder()
                    .baseUrl(SPOTIFY_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();}
        return spotifyRetrofit;
    }

    public static Retrofit getMyTokenApiRetrofit()
    {

        if (myApiRetrofit  == null) {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            String accessToken = TokenManager.getInstance().getFirebaseToken();
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + accessToken)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();

            myApiRetrofit  = new Retrofit.Builder()
                .baseUrl(TOKEN_API_URL)
                    .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return myApiRetrofit ;

    }
    public static Retrofit getMyRecsApiRetrofit()
    {

        if (myApiRecsRetrofit  == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            String accessToken = TokenManager.getInstance().getFirebaseToken();
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + accessToken)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();

            myApiRecsRetrofit  = new Retrofit.Builder()
                    .baseUrl(RECS_API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return myApiRecsRetrofit ;

    }
}
