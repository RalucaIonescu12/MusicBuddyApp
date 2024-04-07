package com.example.music_buddy_app2.FRAGMENTS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.API_RESPONSES.AUDIO_FEATURES.AudioFeaturesObjectResponse;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.AUTHORIZATION.SharedPreferencesManager;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.example.music_buddy_app2.SERVICES.API.SpotifyApiServiceInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AudioFeaturesDialogFragment extends DialogFragment {

    private TrackSearchItem selectedItem;
    Retrofit retrofit;
    private Context mContext;
    public SpotifyApiRecommendationsManager manager;
    public SpotifyApiServiceInterface spotifyApiServiceInterface;
    public AudioFeaturesDialogFragment(TrackSearchItem selectedItem, Context context) {
        this.selectedItem = selectedItem;

    }
    public AudioFeaturesDialogFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_audio_features_dialog, null);
        initiateSpotifyApiService();
        ImageView songImage = view.findViewById(R.id.imageView_song);
        TextView songName = view.findViewById(R.id.textView_song_name);
        TextView artistName = view.findViewById(R.id.artist_name);

        TextView valenceValue = view.findViewById(R.id.valenceValue);
        TextView instrumentalnessValue = view.findViewById(R.id.instrumentalnessValue);
        TextView danceabilityValue = view.findViewById(R.id.danceabilityValue);
        TextView tempoValue = view.findViewById(R.id.TempoValue);
        TextView livenessValue = view.findViewById(R.id.LivenessValue);
        TextView energyValue = view.findViewById(R.id.energyValue);
        TextView timeSignature = view.findViewById(R.id.TimeSignatureValue);
        CardView addTrack = view.findViewById(R.id.addForRec);
        TextView buttonText = view.findViewById(R.id.textAddButton);
        ImageView addIcon=view.findViewById(R.id.addForRecImage);
        Picasso.get().load(selectedItem.getImageResourceId()).into(songImage);
        songName.setText(selectedItem.getSongName());
        artistName.setText(selectedItem.getArtistName());
        if(manager==null)
            manager = SpotifyApiRecommendationsManager.getInstance();

        boolean exists = manager.checkIfTrackAdded(selectedItem);
        if(exists)
        {
            buttonText.setText("Added");
            addIcon.setImageResource(R.drawable.baseline_favorite_border_24);
        }
        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean exists = manager.checkIfTrackAdded(selectedItem);
                if(exists)
                {
                    Toast.makeText(getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                    buttonText.setText("Add for recommendations");
                    addIcon.setImageResource(R.drawable.baseline_add_24);
                    manager.removeFilter(selectedItem.getSongName());
                }
                else
                {
                    String add = manager.addRecFilter("seed_tracks", selectedItem.getSongName(), selectedItem.getId(), selectedItem.getDanceabilityValue(),
                            selectedItem.getInstrumentalnessValue(), selectedItem.getTempoValue(), selectedItem.getLivenessValue(), selectedItem.getValenceValue(), selectedItem.getEnergyValue(),
                            selectedItem.getTimeSignature(), selectedItem.getAcousticness(), selectedItem.getLoudness(), selectedItem.getSpeechiness(), selectedItem.getDuration_ms(), selectedItem.getKey());
                    if (add.equals("5_filters"))
                        Toast.makeText(getContext(), "You already have 5 filters!", Toast.LENGTH_SHORT).show();
                    else if (add.equals("filter_added")) {
                        Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                        buttonText.setText("Added");
                        addIcon.setImageResource(R.drawable.baseline_favorite_border_24);
                    } else if (add.equals("already_exists"))
                        Toast.makeText(getContext(), "Already added!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // API callfor track features
        String id;
        String accessToken = SharedPreferencesManager.getToken(mContext);
        String autorization = "Bearer "+ accessToken;
        id = selectedItem.getId();
        Call<AudioFeaturesObjectResponse> call= spotifyApiServiceInterface.getTracksFeatures(autorization,id);

        call.enqueue(new Callback<AudioFeaturesObjectResponse>() {
            @Override
            public void onResponse(Call<AudioFeaturesObjectResponse> call, Response<AudioFeaturesObjectResponse> response) {

                if (response.isSuccessful()) {
                    AudioFeaturesObjectResponse trackFeatures = response.body();
                    valenceValue.setText(String.valueOf(trackFeatures.getValence()));
                    livenessValue.setText(String.valueOf(trackFeatures.getLiveness()));
                    danceabilityValue.setText(String.valueOf(trackFeatures.getDanceability()));
                    tempoValue.setText(String.valueOf(trackFeatures.getTempo()));
                    instrumentalnessValue.setText(String.valueOf(trackFeatures.getInstrumentalness()));
                    energyValue.setText(String.valueOf(trackFeatures.getEnergy()));

                    timeSignature.setText(String.valueOf(trackFeatures.getTimeSignature()));
                    selectedItem.setAcousticness(Double.valueOf(trackFeatures.getAcousticness()));
                    selectedItem.setDuration_ms((int)trackFeatures.getDurationMs());
                    selectedItem.setKey((int)trackFeatures.getKey());
                    selectedItem.setLoudness(Double.valueOf(trackFeatures.getLoudness()));
                    selectedItem.setMode((int)trackFeatures.getMode());
                    selectedItem.setSpeechiness(Double.valueOf(trackFeatures.getSpeechiness()));
                    selectedItem.setValenceValue(Double.valueOf(trackFeatures.getValence()));
                    selectedItem.setInstrumentalnessValue(Double.valueOf(trackFeatures.getInstrumentalness()));
                    selectedItem.setDanceabilityValue(Double.valueOf(trackFeatures.getDanceability()));
                    selectedItem.setTempoValue(Double.valueOf(trackFeatures.getTempo()));
                    selectedItem.setLivenessValue(Double.valueOf(trackFeatures.getLiveness()));
                    selectedItem.setEnergyValue(Double.valueOf(trackFeatures.getEnergy()));
                    selectedItem.setTimeSignature((int)trackFeatures.getTimeSignature());
                }
                else {
                    Toast.makeText(requireContext(), "Api response not successful!" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AudioFeaturesObjectResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(requireContext(), "Failed features request!" , Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "API call failed", t);
                t.printStackTrace();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public void initiateSpotifyApiService()
    {
        if (retrofit == null) {
            retrofit = RetrofitClient.getRetrofitInstance();
        }
        spotifyApiServiceInterface = retrofit.create(SpotifyApiServiceInterface.class);
    }
}