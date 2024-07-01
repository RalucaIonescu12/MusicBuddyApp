package com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS.FinalChangesForSpotifyRecommendationsActivity;
import com.example.music_buddy_app2.MODELS.AccordionItemSpotifyRec;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.MANAGERS.SpotifyApiRecommendationsManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Map;

public class AccordionRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//TODO: fix count issue  + instrumentalness bug
    private List<AccordionItemSpotifyRec> accordionItems;
    private Context context;
    private SpotifyApiRecommendationsManager manager;
    public AccordionRecAdapter(List<AccordionItemSpotifyRec> accordionItems, Context context) {
        this.accordionItems = accordionItems;
        this.context = context;
        manager = SpotifyApiRecommendationsManager.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case AccordionItemSpotifyRec.FORM_TYPE_INPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accordion_input, parent, false);
                return new InputViewHolder(view);
            case AccordionItemSpotifyRec.FORM_TYPE_SEEKBAR: 
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
        Spinner genreSpinner;
        ChipGroup chipGroup;
        ImageView icon;
        Context context;
        InputViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            detailsLayout.setVisibility(View.GONE);
            icon=itemView.findViewById(R.id.arrowInput);
            chipGroup = itemView.findViewById(R.id.chip_group_rec_spotify);
            genreSpinner = itemView.findViewById(R.id.genresSpinner);
            genreSpinner.setPrompt("Pick genres");
            Map<String,Map<String,Map<String,Map<String, Double>>>> recFilters= manager.getRecFilters();
            Map<String,Map<String,Map<String, Double>>> artists = recFilters.get("seed_artists");
            Map<String,Map<String,Map<String, Double>>> tracks = recFilters.get("seed_tracks");
            Map<String,Map<String,Map<String, Double>>> genres = recFilters.get("seed_genres");

            if(artists!=null)
                for(String key: artists.keySet())
                    addChip(key);
            if (tracks != null)
                for (String key : tracks.keySet())
                    addChip(key);
            if (genres != null)
                for (String key : genres.keySet())
                    addChip(key);

            itemView.setOnClickListener(v -> {
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE);
                    ((FinalChangesForSpotifyRecommendationsActivity) context).setSpinnerGenres();
                    icon.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                } else {
                    detailsLayout.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                }
            });

            genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedGenre = parentView.getItemAtPosition(position).toString();
                    String add = manager.addRecFilter("seed_genres",selectedGenre,selectedGenre,0.0,0.0,0.0,0.0,0.0,0.0,0,0.0,0.0,0.0,0,0);

                    if(add.equals("5_filters")) Toast.makeText(context, "You already have 5 filters!", Toast.LENGTH_SHORT).show();
                    else
                    {
                        if(add.equals("filter_added"))
                            Toast.makeText(context, "Added!", Toast.LENGTH_SHORT).show();
                        addChip(selectedGenre);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });

        }

        void bind(AccordionItemSpotifyRec item) {
            titleTextView.setText(item.getTitle());
        }
        private void addChip(String text) {
            Chip chip = new Chip(context);
            chip.setText(text);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    manager.removeFilter(chip.getText().toString());
                }
            });

            chipGroup.addView(chip);
        }
    }

    // ViewHolder for switches form
    class SeekBarViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        SeekBar seekBarMinDanceability;
        TextView progressValueCalculatedEnergy;
        TextView progressValueCalculatedDanceability;
        TextView progressValueCalculatedTimeSignature;
        TextView progressValueCalculatedInstrumentalness;
        TextView progressValueCalculatedLiveness;
        TextView progressValueCalculatedValence;
        TextView progressValueMinDanceability;

        SeekBar seekBarMinInstrumentalness;
        TextView progressValueMinInstrumentalness;
        TextView progressValueCalculatedTempo;
        SeekBar seekBarMinTempo,seekBarMinEnergy, seekBarMinTimeSignature;
        TextView progressValueMinTempo;
        TextView progressValueMinEnergy;

        TextView progressValueMinTimeSignature;
        SeekBar seekBarMinLiveness;
        TextView progressValueMinLiveness;
        SeekBar seekBarMinValence;
        SeekBar seekBarNbrSongs;
        TextView progressNbrSongsValue;
        TextView progressValueMinValence;

        SeekBar seekBarMaxDanceability;
        TextView progressValueMaxDanceability;

        SeekBar seekBarMaxInstrumentalness;
        TextView progressValueMaxInstrumentalness;
        SeekBar seekBarMaxTempo,seekBarMaxEnergy, seekBarMaxTimeSignature;
        TextView progressValueMaxTempo;
        TextView progressValueMaxEnergy;
        TextView progressValueMaxTimeSignature;
        SeekBar seekBarMaxLiveness;
        TextView progressValueMaxLiveness;
        SeekBar seekBarMaxValence;
        TextView progressValueMaxValence;
        LinearLayout detailsLayout;
        ImageView icon;

        SeekBarViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            icon=itemView.findViewById(R.id.arrowSwitches);
            itemView.setOnClickListener(v -> {
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE);
                    icon.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                } else {
                    detailsLayout.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                }
            });


            progressValueMinDanceability= itemView.findViewById(R.id.progressValueMinDanceability);
            detailsLayout.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.baseline_arrow_drop_up_24);
            seekBarMinDanceability = itemView.findViewById(R.id.seekBarMinDanceability);
            progressValueCalculatedDanceability = itemView.findViewById(R.id.progressValueCalculatedDanceability);
            progressValueCalculatedDanceability.setText(manager.getAudioFeature("target_danceability").toString());

            seekBarMinDanceability.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinDanceability.setText(String.valueOf((double)progress/100000.0));
                    double minValue = Double.parseDouble(progressValueMinDanceability.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedDanceability.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinDanceability.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinDanceability.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                  
                }
            });
            progressValueMaxDanceability = itemView.findViewById(R.id.progressValueMaxDanceability);

            seekBarMaxDanceability = itemView.findViewById(R.id.seekBarMaxDanceability);
            progressValueMaxDanceability.setText("1.0");
            seekBarMaxDanceability.setProgress(100000);

            seekBarMaxDanceability.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxDanceability.setText(String.valueOf((double) progress / 100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxDanceability.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedDanceability.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxDanceability.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxDanceability.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            progressValueMinLiveness= itemView.findViewById(R.id.progressValueMinLiveness);

            seekBarMinLiveness = itemView.findViewById(R.id.seekBarMinLiveness);
            progressValueCalculatedLiveness= itemView.findViewById(R.id.progressValueCalculatedLiveness);
            progressValueCalculatedLiveness.setText(manager.getAudioFeature("target_liveness").toString());
            // listener to update the progress value when SeekBar's progress changes
            seekBarMinLiveness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinLiveness.setText(String.valueOf((double)progress/100000.0));
                    double minValue = Double.parseDouble(progressValueMinLiveness.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedLiveness.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinLiveness.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinLiveness.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                   
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                   
                }
            });

            progressValueMaxLiveness= itemView.findViewById(R.id.progressValueMaxLiveness);

            seekBarMaxLiveness = itemView.findViewById(R.id.seekBarMaxLiveness);
            progressValueMaxLiveness.setText("1.0");
            seekBarMaxLiveness.setProgress(100000);
            // listener to update the progress value when SeekBar's progress changes
            seekBarMaxLiveness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxLiveness.setText(String.valueOf((double)progress/100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxLiveness.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedLiveness.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxLiveness.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxLiveness.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            progressValueMinValence= itemView.findViewById(R.id.progressValueMinValence);

            seekBarMinValence = itemView.findViewById(R.id.seekBarMinValence);
            progressValueCalculatedValence = itemView.findViewById(R.id.progressValueCalculatedValence);
            progressValueCalculatedValence.setText(manager.getAudioFeature("target_valence").toString());

            // listener to update the progress value when SeekBar's progress changes
            seekBarMinValence.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinValence.setText(String.valueOf((double)progress/100000.0));
                    double minValue = Double.parseDouble(progressValueMinValence.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedValence.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinValence.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinValence.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                   
                }
            });
            progressValueMaxValence= itemView.findViewById(R.id.progressValueMaxValence);

            seekBarMaxValence = itemView.findViewById(R.id.seekBarMaxValence);
            progressValueMaxValence.setText("1.0");
            seekBarMaxValence.setProgress(100000);
            seekBarMaxValence.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxValence.setText(String.valueOf((double)progress/100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxValence.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedValence.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxValence.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxValence.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            progressValueMinInstrumentalness= itemView.findViewById(R.id.progressValueMinInstrumentalness);
            // Initially hide the details
            seekBarMinInstrumentalness = itemView.findViewById(R.id.seekBarMinInstrumentalness);
            progressValueCalculatedInstrumentalness = itemView.findViewById(R.id.progressValueCalculatedInstrumentalness);
            progressValueCalculatedInstrumentalness.setText(manager.getAudioFeature("target_instrumentalness").toString());

            // listener to update the progress value when SeekBar's progress changes
            seekBarMinInstrumentalness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinInstrumentalness.setText(String.valueOf((double)progress/100000.0));
                    double minValue = Double.parseDouble(progressValueMinInstrumentalness.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedInstrumentalness.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinInstrumentalness.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinInstrumentalness.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    
                }
            });
            progressValueMaxInstrumentalness = itemView.findViewById(R.id.progressValueMaxInstrumentalness);
            seekBarMaxInstrumentalness = itemView.findViewById(R.id.seekBarMaxInstrumentalness);
            progressValueMaxInstrumentalness.setText("1.0");
            seekBarMaxInstrumentalness.setProgress(100000);
            seekBarMaxInstrumentalness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxInstrumentalness.setText(String.valueOf((double) progress / 100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxInstrumentalness.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedInstrumentalness.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxInstrumentalness.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxInstrumentalness.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            progressValueMinTempo= itemView.findViewById(R.id.progressValueMinTempo);
            // Initially hide the details
            seekBarMinTempo = itemView.findViewById(R.id.seekBarMinTempo);
            progressValueCalculatedTempo = itemView.findViewById(R.id.progressValuecalculatedTempo);
            progressValueCalculatedTempo.setText(manager.getAudioFeature("target_tempo").toString());

            // listener to update the progress value when SeekBar's progress changes
            seekBarMinTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinTempo.setText(String.valueOf( (double)progress / 100000.0));
                    double minValue = Double.parseDouble(progressValueMinTempo.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedTempo.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinTempo.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinTempo.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    
                }
            });
            progressValueMaxTempo = itemView.findViewById(R.id.progressValueMaxTempo);
            seekBarMaxTempo = itemView.findViewById(R.id.seekBarMaxTempo);
            progressValueMaxTempo.setText("200.0");
            seekBarMaxTempo.setProgress(20000000);
            seekBarMaxTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxTempo.setText(String.valueOf((double)progress / 100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxTempo.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedTempo.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxTempo.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxTempo.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            progressValueMinEnergy= itemView.findViewById(R.id.progressValueMinEnergy);
            // Initially hide the details
            seekBarMinEnergy = itemView.findViewById(R.id.seekBarMinEnergy);
            progressValueCalculatedEnergy = itemView.findViewById(R.id.progressValueCalculatedEnergy);
            progressValueCalculatedEnergy.setText(manager.getAudioFeature("target_energy").toString());

            // listener to update the progress value when SeekBar's progress changes
            seekBarMinEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinEnergy.setText(String.valueOf((double)progress/100000.0));
                    double minValue = Double.parseDouble(progressValueMinEnergy.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedEnergy.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinEnergy.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinEnergy.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            progressValueMaxEnergy= itemView.findViewById(R.id.progressValueMaxEnergy);
            // Initially hide the details
            seekBarMaxEnergy = itemView.findViewById(R.id.seekBarMaxEnergy);
            progressValueMaxEnergy.setText("1.0");
            seekBarMaxEnergy.setProgress(100000);
            // listener to update the progress value when SeekBar's progress changes
            seekBarMaxEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxEnergy.setText(String.valueOf((double)progress/100000.0));
                    double maxValue = Double.parseDouble(progressValueMaxEnergy.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedEnergy.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxEnergy.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxEnergy.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            progressValueMinTimeSignature= itemView.findViewById(R.id.progressValueMinTimeSignature);
            // Initially hide the details
            seekBarMinTimeSignature = itemView.findViewById(R.id.seekBarMinTimeSignature);
            progressValueCalculatedTimeSignature = itemView.findViewById(R.id.progressValueCalculatedTimeSignature);
            progressValueCalculatedTimeSignature.setText(manager.getAudioFeature("target_time_signature").toString());

            // listener to update the progress value when SeekBar's progress changes
            seekBarMinTimeSignature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMinTimeSignature.setText(String.valueOf(progress+3));
                    double minValue = Double.parseDouble(progressValueMinTimeSignature.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedTimeSignature.getText().toString());

                    if (minValue <= targetValue) {
                        progressValueMinTimeSignature.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMinTimeSignature.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            progressValueMaxTimeSignature= itemView.findViewById(R.id.progressValueMaxTimeSignature);
            // Initially hide the details
            seekBarMaxTimeSignature = itemView.findViewById(R.id.seekBarMaxTimeSignature);
            progressValueMaxTimeSignature.setText("7");
            seekBarMaxTimeSignature.setProgress(7);
            // listener to update the progress value when SeekBar's progress changes
            seekBarMaxTimeSignature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValueMaxTimeSignature.setText(String.valueOf(progress+3));
                    double maxValue = Double.parseDouble(progressValueMaxTimeSignature.getText().toString());
                    double targetValue = Double.parseDouble(progressValueCalculatedTimeSignature.getText().toString());

                    if (maxValue >= targetValue) {
                        progressValueMaxTimeSignature.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    } else {
                        progressValueMaxTimeSignature.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            progressNbrSongsValue= itemView.findViewById(R.id.progressNbrSongsSignature);

            seekBarNbrSongs = itemView.findViewById(R.id.seekNbrSongsSignature);
            progressNbrSongsValue.setText("100");
            seekBarNbrSongs.setProgress(100);
            // listener to update the progress value when SeekBar's progress changes
            seekBarNbrSongs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressNbrSongsValue.setText(String.valueOf(progress));
                    if(progress!=0)
                    {
                        progressNbrSongsValue.setTextColor(Color.parseColor("#638CEB"));
                        manager.setEnableButton(true);
                    }
                    else
                    {
                        progressNbrSongsValue.setTextColor(Color.RED);
                        manager.setEnableButton(false);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }


        void bind(AccordionItemSpotifyRec item) {
            titleTextView.setText(item.getTitle());

        }
    }

}
