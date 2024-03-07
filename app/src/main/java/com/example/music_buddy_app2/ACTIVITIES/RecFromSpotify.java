package com.example.music_buddy_app2.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_buddy_app2.ADAPTERS.AccordionRecAdapter;
import com.example.music_buddy_app2.MODELS.AccordionItemSpotifyRec;
import com.example.music_buddy_app2.R;

import java.util.ArrayList;
import java.util.List;

public class RecFromSpotify extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AccordionRecAdapter adapter;
    private SeekBar seekBarDanceability;
    private TextView progressText;
    private List<AccordionItemSpotifyRec> accordionItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_from_spotify);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize accordion items
        accordionItems = new ArrayList<>();
        accordionItems.add(new AccordionItemSpotifyRec("Artists-Songs-Genres", "Details for Input Form", AccordionItemSpotifyRec.FORM_TYPE_INPUT));
        accordionItems.add(new AccordionItemSpotifyRec("Audio features", "", AccordionItemSpotifyRec.FORM_TYPE_SEEKBAR));

        // Initialize and set adapter
        adapter = new AccordionRecAdapter(accordionItems, this);
        recyclerView.setAdapter(adapter);


    }


}