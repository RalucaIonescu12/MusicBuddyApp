package com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.AccordionRecAdapter;
import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.GenresResponse;
import com.example.music_buddy_app2.MODELS.AccordionItemSpotifyRec;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FinalChangesForSpotifyRecommendationsActivity extends BaseActivity
{
    private RecyclerView recyclerView;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    Retrofit retrofit;
    private AccordionRecAdapter adapter;
    private TextView progressValueMinDanceability, progressValueMinInstrumentalness, progressValueMinTempo;
    private TextView progressValueMinLiveness, progressValueMinValence, progressValueMinEnergy, progressValueMinTimeSignature, progressNbrSongsValue;
    private TextView progressValueMaxDanceability, progressValueMaxInstrumentalness, progressValueMaxTempo;
    private TextView progressValueMaxLiveness, progressValueMaxValence, progressValueMaxEnergy, progressValueMaxTimeSignature;
    private List<AccordionItemSpotifyRec> accordionItems;
    private Spinner genreSpinner;
    private List<String> genres;
    private CardView getRecButton;
    private SpotifyApiRecommendationsManager manager;

    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_changes_for_spotify_recommendations);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        manager=SpotifyApiRecommendationsManager.getInstance();
        getRecButton =findViewById(R.id.buttonGetRecs);

        accordionItems = new ArrayList<>();
        accordionItems.add(new AccordionItemSpotifyRec("Artists-Songs-Genres", "Details for Input Form", AccordionItemSpotifyRec.FORM_TYPE_INPUT));
        accordionItems.add(new AccordionItemSpotifyRec("Audio features", "", AccordionItemSpotifyRec.FORM_TYPE_SEEKBAR));
        adapter = new AccordionRecAdapter(accordionItems, FinalChangesForSpotifyRecommendationsActivity.this);
        recyclerView.setAdapter(adapter);
        genres=new ArrayList<>();
        initiateSpotifyApiService();
        getRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manager.noFilters())  Toast.makeText(FinalChangesForSpotifyRecommendationsActivity.this, "No filters added", Toast.LENGTH_SHORT).show();
                else if (manager.getEnableButton())
                {
                    setAudioFeatures();
                    startActivity(new Intent(FinalChangesForSpotifyRecommendationsActivity.this, SeeSpotifyRecommendationsActivity.class));
                } else {
                    Toast.makeText(FinalChangesForSpotifyRecommendationsActivity.this, "Please configure all input fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setAudioFeatures()
    {
        manager.setTargetAudioFeatures();
        progressValueMinDanceability = findViewById(R.id.progressValueMinDanceability);
        progressValueMinInstrumentalness = findViewById(R.id.progressValueMinInstrumentalness);
        progressValueMinTempo = findViewById(R.id.progressValueMinTempo);
        progressValueMinLiveness = findViewById(R.id.progressValueMinLiveness);
        progressValueMinValence = findViewById(R.id.progressValueMinValence);
        progressValueMinEnergy = findViewById(R.id.progressValueMinEnergy);
        progressValueMinTimeSignature = findViewById(R.id.progressValueMinTimeSignature);
        progressValueMaxDanceability = findViewById(R.id.progressValueMaxDanceability);
        progressValueMaxInstrumentalness = findViewById(R.id.progressValueMaxInstrumentalness);
        progressValueMaxTempo = findViewById(R.id.progressValueMaxTempo);
        progressValueMaxLiveness = findViewById(R.id.progressValueMaxLiveness);
        progressValueMaxValence = findViewById(R.id.progressValueMaxValence);
        progressValueMaxEnergy = findViewById(R.id.progressValueMaxEnergy);
        progressValueMaxTimeSignature = findViewById(R.id.progressValueMaxTimeSignature);

        progressNbrSongsValue = findViewById(R.id.progressNbrSongsSignature);
        manager.setNbrTracks(Integer.parseInt(String.valueOf(progressNbrSongsValue.getText())));

        progressValueMinDanceability = findViewById(R.id.progressValueMinDanceability);
        manager.setAudioFeature("min_danceability", Double.parseDouble(String.valueOf(progressValueMinDanceability.getText())));

        progressValueMinInstrumentalness = findViewById(R.id.progressValueMinInstrumentalness);
        manager.setAudioFeature("min_instrumentalness", Double.parseDouble(String.valueOf(progressValueMinInstrumentalness.getText())));

        progressValueMinTempo = findViewById(R.id.progressValueMinTempo);
        manager.setAudioFeature("min_tempo", Double.parseDouble(String.valueOf(progressValueMinTempo.getText())));

        progressValueMinLiveness = findViewById(R.id.progressValueMinLiveness);
        manager.setAudioFeature("min_liveness", Double.parseDouble(String.valueOf(progressValueMinLiveness.getText())));

        progressValueMinValence = findViewById(R.id.progressValueMinValence);
        manager.setAudioFeature("min_valence", Double.parseDouble(String.valueOf(progressValueMinValence.getText())));

        progressValueMinEnergy = findViewById(R.id.progressValueMinEnergy);
        manager.setAudioFeature("min_energy", Double.parseDouble(String.valueOf(progressValueMinEnergy.getText())));

        progressValueMinTimeSignature = findViewById(R.id.progressValueMinTimeSignature);
        manager.setAudioFeature("min_time_signature", Double.parseDouble(String.valueOf(progressValueMinTimeSignature.getText())));

        //max
        progressValueMaxDanceability = findViewById(R.id.progressValueMaxDanceability);
        manager.setAudioFeature("max_danceability", Double.parseDouble(String.valueOf(progressValueMaxDanceability.getText())));

        progressValueMaxInstrumentalness = findViewById(R.id.progressValueMaxInstrumentalness);
        manager.setAudioFeature("max_instrumentalness", Double.parseDouble(String.valueOf(progressValueMaxInstrumentalness.getText())));

        progressValueMaxTempo = findViewById(R.id.progressValueMaxTempo);
        manager.setAudioFeature("max_tempo", Double.parseDouble(String.valueOf(progressValueMaxTempo.getText())));

        progressValueMaxLiveness = findViewById(R.id.progressValueMaxLiveness);
        manager.setAudioFeature("max_liveness", Double.parseDouble(String.valueOf(progressValueMaxLiveness.getText())));

        progressValueMaxValence = findViewById(R.id.progressValueMaxValence);
        manager.setAudioFeature("max_valence", Double.parseDouble(String.valueOf(progressValueMaxValence.getText())));

        progressValueMaxEnergy = findViewById(R.id.progressValueMaxEnergy);
        manager.setAudioFeature("max_energy", Double.parseDouble(String.valueOf(progressValueMaxEnergy.getText())));

        progressValueMaxTimeSignature = findViewById(R.id.progressValueMaxTimeSignature);
        manager.setAudioFeature("max_time_signature", Double.parseDouble(String.valueOf(progressValueMaxTimeSignature.getText())));

        progressNbrSongsValue = findViewById(R.id.progressNbrSongsSignature);
        manager.setNbrTracks(Integer.parseInt(String.valueOf(progressNbrSongsValue.getText())));
//        Log.e("MY_LOGS",manager.getAudioFeatureFields().toString());

    }
    public void setSpinnerGenres() {
        genreSpinner = findViewById(R.id.genresSpinner);


        Call<GenresResponse> call= spotifyApiServiceInterface.getAvailableGenres();
        call.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {

                if (response.isSuccessful()) {

                    GenresResponse genresResponse  = response.body();
                    genres.addAll(genresResponse.getGenres());

                    ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(FinalChangesForSpotifyRecommendationsActivity.this, android.R.layout.simple_spinner_item, genres);
                    genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    genreSpinner.setAdapter(genreAdapter);

                }
                else {
                    Toast.makeText(FinalChangesForSpotifyRecommendationsActivity.this, "Api response not successful!" , Toast.LENGTH_SHORT).show();
                    Log.e("MY_LOGS","API call failed for getGenres in final changes activity. "+ response);
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {

                Toast.makeText(FinalChangesForSpotifyRecommendationsActivity.this, "Failed search request!" , Toast.LENGTH_SHORT).show();
                Log.e("MY_LOGS", "API call failed for getGenres in final changes activity.", t);
                t.printStackTrace();
            }
        });



    }

}




