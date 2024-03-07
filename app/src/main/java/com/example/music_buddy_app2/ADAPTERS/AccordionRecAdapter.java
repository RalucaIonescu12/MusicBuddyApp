package com.example.music_buddy_app2.ADAPTERS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ACTIVITIES.RecFromSpotify;
import com.example.music_buddy_app2.MODELS.AccordionItemSpotifyRec;
import com.example.music_buddy_app2.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
public class AccordionRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AccordionItemSpotifyRec> accordionItems;
    private Context context;

    public AccordionRecAdapter(List<AccordionItemSpotifyRec> accordionItems, Context context) {
        this.accordionItems = accordionItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case AccordionItemSpotifyRec.FORM_TYPE_INPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accordion_input, parent, false);
                return new InputViewHolder(view);
            case AccordionItemSpotifyRec.FORM_TYPE_SEEKBAR: // Add this case
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accordion_switches, parent, false);
                return new SeekBarViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AccordionItemSpotifyRec item = accordionItems.get(position);
        switch (item.getFormType()) {
            case AccordionItemSpotifyRec.FORM_TYPE_INPUT:
                ((InputViewHolder) holder).bind(item);
                break;
            case AccordionItemSpotifyRec.FORM_TYPE_SEEKBAR:
                ((SeekBarViewHolder) holder).bind(item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return accordionItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return accordionItems.get(position).getFormType();
    }

    // ViewHolder for input form
    class InputViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout detailsLayout;
        EditText inputGenres;
        EditText inputTracks;
        EditText inputArtists;
        ChipGroup chipGroup;
        InputViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            inputGenres = itemView.findViewById(R.id.inputGenres);
            inputTracks = itemView.findViewById(R.id.inputTracks);
            inputArtists = itemView.findViewById(R.id.inputArtists);
            chipGroup = itemView.findViewById(R.id.chip_group_rec_spotify);
            itemView.setOnClickListener(v -> {
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE); // Show details on click
                } else {
                    detailsLayout.setVisibility(View.GONE); // Hide details if already visible
                }
            });
            inputGenres.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        // Add chip when the enter key is pressed
                        addChip(inputGenres.getText().toString());
                        inputGenres.setText(""); // Clear the EditText after adding the chip
                        return true;
                    }
                    return false;
                }
            });
            inputTracks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        // Add chip when the enter key is pressed
                        addChip(inputTracks.getText().toString());
                        inputTracks.setText(""); // Clear the EditText after adding the chip
                        return true;
                    }
                    return false;
                }
            });
            inputArtists.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        // Add chip when the enter key is pressed
                        addChip(inputArtists.getText().toString());
                        inputArtists.setText(""); // Clear the EditText after adding the chip
                        return true;
                    }
                    return false;
                }
            });
        }

        void bind(AccordionItemSpotifyRec item) {
            titleTextView.setText(item.getTitle());
            // You can further customize this as needed
        }
        private void addChip(String text) {
            Chip chip = new Chip(context);
            chip.setText(text);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                }
            });

            chipGroup.addView(chip);
        }
    }

    // ViewHolder for switches form
    class SeekBarViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        SeekBar seekBarDanceability;
        TextView progressValueDanceability;

        SeekBar seekBarInstrumentalness;
        TextView progressValueInstrumentalness;
        SeekBar seekBarTempo;
        TextView progressValueTempo;
        SeekBar seekBarLiveness;
        TextView progressValueLiveness;
        SeekBar seekBarValence;
        TextView progressValueValence;
        LinearLayout detailsLayout;

        SeekBarViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            itemView.setOnClickListener(v -> {
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE); // Show details on click
                } else {
                    detailsLayout.setVisibility(View.GONE); // Hide details if already visible
                }
            });

            progressValueDanceability= itemView.findViewById(R.id.progressValueDanceability);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            seekBarDanceability = itemView.findViewById(R.id.seekBarDanceability);

            // listener to update the progress value when SeekBar's progress changes
            seekBarDanceability.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueDanceability.setText(String.valueOf(progress)); // Update TextView with current progress
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }
            });

            progressValueLiveness= itemView.findViewById(R.id.progressValueLiveness);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            seekBarLiveness = itemView.findViewById(R.id.seekBarLiveness);

            // listener to update the progress value when SeekBar's progress changes
            seekBarLiveness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueLiveness.setText(String.valueOf(progress)); // Update TextView with current progress
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }
            });

            progressValueValence= itemView.findViewById(R.id.progressValueValence);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            seekBarValence = itemView.findViewById(R.id.seekBarValence);


            // listener to update the progress value when SeekBar's progress changes
            seekBarValence.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueValence.setText(String.valueOf(progress)); // Update TextView with current progress
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }
            });


            progressValueInstrumentalness= itemView.findViewById(R.id.progressValueInstrumentalness);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            seekBarInstrumentalness = itemView.findViewById(R.id.seekBarInstrumentalness);

            // listener to update the progress value when SeekBar's progress changes
            seekBarInstrumentalness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueInstrumentalness.setText(String.valueOf(progress)); // Update TextView with current progress
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }
            });

            progressValueTempo= itemView.findViewById(R.id.progressValueTempo);
            detailsLayout.setVisibility(View.GONE); // Initially hide the details
            seekBarTempo = itemView.findViewById(R.id.seekBarTempo);

            // listener to update the progress value when SeekBar's progress changes
            seekBarTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueTempo.setText(String.valueOf(progress)); // Update TextView with current progress
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this implementation
                }
            });


        }


        void bind(AccordionItemSpotifyRec item) {
            titleTextView.setText(item.getTitle());

        }
    }
}
