package com.example.music_buddy_app2.FRAGMENTS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
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
        TextView buttonText = view.findViewById(R.id.addButtontext);
        ImageView addIcon=view.findViewById(R.id.addForRecArtistImage);
        Picasso.get().load(selectedItem.getImageResourceId()).into(artistImage);
        artistName.setText(selectedItem.getArtistName());
        genres.setText(selectedItem.getGenres());
        if(manager==null)
            manager = SpotifyApiRecommendationsManager.getInstance();
        boolean exists = manager.checkIfArtistAdded(selectedItem);
        if(exists)
        {
            buttonText.setText("Added");
            addIcon.setImageResource(R.drawable.baseline_favorite_border_24_blue);
        }
        else {
            addArtist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    boolean exists = manager.checkIfArtistAdded(selectedItem);
                    if(exists)
                    {
                        Toast.makeText(getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                        buttonText.setText("Add for recommendations");
                        addIcon.setImageResource(R.drawable.add_blue);
                        manager.removeFilter(selectedItem.getArtistName());
                    }
                    else
                    {
                        String add = manager.addRecFilter("seed_artists", selectedItem.getArtistName(), selectedItem.getId(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, 0, 0);
                        if (add.equals("5_filters"))
                            Toast.makeText(getContext(), "You already have 5 filters!", Toast.LENGTH_SHORT).show();
                        else if (add.equals("filter_added")) {
                            Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                            buttonText.setText("Added");
                            addIcon.setImageResource(R.drawable.baseline_favorite_border_24_blue);
                        } else if (add.equals("already_exists")) {
                            Toast.makeText(getContext(), "Already addded!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        builder.setView(view);
        return builder.create();
    }


}