package com.example.music_buddy_app2.FRAGMENTS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_buddy_app2.ACTIVITIES.AudioFeaturesTrackActivity;
import com.example.music_buddy_app2.ACTIVITIES.ProfileActivity;
import com.example.music_buddy_app2.API_RESPONSES.AudioFeaturesObjectResponse;
import com.example.music_buddy_app2.API_RESPONSES.SearchTrackResponse;
import com.example.music_buddy_app2.API_RESPONSES.TopArtistsResponse;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.RetrofitClient;
import com.example.music_buddy_app2.SERVICES.SharedPreferencesManager;
import com.example.music_buddy_app2.SERVICES.SpotifyApiServiceInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AudioFeaturesDialogFragment extends DialogFragment {

    private TrackSearchItem selectedItem;
    Retrofit retrofit;
    private Context mContext;
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

        Picasso.get().load(selectedItem.getImageResourceId()).into(songImage);
        songName.setText(selectedItem.getSongName());
        artistName.setText(selectedItem.getArtistName());

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