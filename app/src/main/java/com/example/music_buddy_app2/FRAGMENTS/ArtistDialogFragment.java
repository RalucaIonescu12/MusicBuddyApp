package com.example.music_buddy_app2.FRAGMENTS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.SpotifyApiRecommendationsManager;
import com.squareup.picasso.Picasso;

public class ArtistDialogFragment extends DialogFragment {

    private ArtistSearchItem selectedItem;
    SpotifyApiRecommendationsManager manager;
    public ArtistDialogFragment(ArtistSearchItem selectedItem, Context context) {
        this.selectedItem = selectedItem;
    }
    public ArtistDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_artist_dialog, null);
        ImageView artistImage = view.findViewById(R.id.imageView_artist);
        TextView artistName = view.findViewById(R.id.artist_name);
        TextView genres = view.findViewById(R.id.textView_genres);
        CardView addArtist = view.findViewById(R.id.addForRecArtistCV);

        Picasso.get().load(selectedItem.getImageResourceId()).into(artistImage);
        artistName.setText(selectedItem.getArtistName());
        genres.setText(selectedItem.getGenres());

        addArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manager==null)
                     manager = SpotifyApiRecommendationsManager.getInstance();

                String add = manager.addRecFilter("seed_artists", selectedItem.getArtistName(),selectedItem.getId(),0.0,0.0,0.0,0.0,0.0,0.0,0,0.0,0.0,0.0,0,0);
                if(add.equals("5_filters")) Toast.makeText(getContext(), "You already have 5 filters!", Toast.LENGTH_SHORT).show();
                else if(add.equals("filter_added"))Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                else if (add.equals("already_exists"))Toast.makeText(getContext(), "Already addded!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        return builder.create();
    }


}