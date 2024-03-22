package com.example.music_buddy_app2.ADAPTERS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.MODELS.ArtistSearchItem;
import com.example.music_buddy_app2.MODELS.TrackSearchItem;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchArtistsAdapter extends RecyclerView.Adapter<SearchArtistsAdapter.ViewHolder>{

    private Context context;
    private List<ArtistSearchItem> searchResults;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ArtistSearchItem item);
    }
    public SearchArtistsAdapter(Context context, List<ArtistSearchItem> searchResults, OnItemClickListener listener) {
        this.context = context;
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArtistSearchItem result = searchResults.get(position);
        holder.artistName.setText(result.getArtistName());
        try {
            Picasso.get().load(result.getImageResourceId()).into(holder.artistImage);
        } catch (Exception e) {
            holder.artistImage.setImageResource(R.drawable.wood);
            e.printStackTrace();
            Log.e("IMAGE_LOADING", "Error loading artist picture: " + e.getMessage(), e);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (listener != null) {
                        listener.onItemClick(result);
                    }
                    else{
                        Log.e("LISTENER", "Listener is null");
                    }
                } catch (Exception e) {
                    Log.e("LISTENER", "Exception: " + e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView artistImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.textView_artist_namee);
            artistImage = itemView.findViewById(R.id.imageView_artistt);

        }
    }
}
