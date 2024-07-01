package com.example.music_buddy_app2.ACTIVITIES.PROFILE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ADAPTERS.OUR_RECOMMENDATIONS.OurRecommendationsAdapter;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.R;

import java.util.List;

public class SeeCommonSongsActivity  extends BaseActivity {
    private RecyclerView recyclerView;
    private OurRecommendationsAdapter adapter;
    private static List<Track> commonTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_spotify_recommendations_made_by_us);

        recyclerView = findViewById(R.id.rv_tracks_our);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateRecyclerView();
    }

    public static void setCommonTracks(List<Track> tracks) {
        commonTracks = tracks;
    }

    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new OurRecommendationsAdapter(this, commonTracks, new OurRecommendationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Track item) {
                    String url = "https://open.spotify.com/track/" + item.getId();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
