package com.example.music_buddy_app2.ADAPTERS.OUR_RECOMMENDATIONS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ADAPTERS.SPOTIFY_RECOMMENDATIONS.SearchTracksAdapter;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.MODELS.Track;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OurRecommendationsAdapter extends RecyclerView.Adapter<OurRecommendationsAdapter.ViewHolder>{
    private Context context;
    private List<Track> searchResults;
    private OurRecommendationsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Track item);
    }
    public OurRecommendationsAdapter(Context context, List<Track> searchResults, OurRecommendationsAdapter.OnItemClickListener listener) {
        this.context = context;
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OurRecommendationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result_track, parent, false);
        return new OurRecommendationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OurRecommendationsAdapter.ViewHolder holder, int position) {
        Track result = searchResults.get(position);
        holder.songName.setText(result.getSongName());
        holder.artistName.setText(String.join(", ",result.getArtists()));
        Picasso.get().load(result.getImageResourceId()).into(holder.songImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(result);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songName, artistName;
        ImageView songImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.textView_song_name);
            artistName = itemView.findViewById(R.id.textView_artist_name);
            songImage = itemView.findViewById(R.id.imageView_song);

        }
    }
}
