package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ADAPTERS.AccordionRecAdapter;
import com.example.music_buddy_app2.API_RESPONSES.GenresResponse;
import com.example.music_buddy_app2.MODELS.AccordionItemSpotifyRec;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FinalChangesForSpotifyRecommendationsActivity extends AppCompatActivity {

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
    private Button getRecButton;
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

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        manager=SpotifyApiRecommendationsManager.getInstance();
        getRecButton =findViewById(R.id.buttonGetRecs);
        // Initialize accordion items
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
                if (manager.getEnableButton()) {
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
        Log.e("FEATURES",manager.getAudioFeatureFields().toString());

    }
    public void setSpinnerGenres() {
        genreSpinner = findViewById(R.id.genresSpinner);

        String accessToken = SharedPreferencesManager.getToken(this);
        String autorization = "Bearer "+ accessToken;

        Call<GenresResponse> call= spotifyApiServiceInterface.getAvailableGenres(autorization);
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
                    Log.e("Response",response.toString());
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(FinalChangesForSpotifyRecommendationsActivity.this, "Failed search request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });



    }

}




