package com.example.music_buddy_app2.ADAPTERS;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchTracksAdapter extends RecyclerView.Adapter<SearchTracksAdapter.ViewHolder> {

    private Context context;
    private List<TrackSearchItem> searchResults;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TrackSearchItem item);
    }
    public SearchTracksAdapter(Context context, List<TrackSearchItem> searchResults, OnItemClickListener listener) {
        this.context = context;
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrackSearchItem result = searchResults.get(position);
        holder.songName.setText(result.getSongName());
        holder.artistName.setText(result.getArtistName());
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
