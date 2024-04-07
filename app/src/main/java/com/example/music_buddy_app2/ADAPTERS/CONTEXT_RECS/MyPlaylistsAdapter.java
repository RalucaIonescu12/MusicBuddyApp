package com.example.music_buddy_app2.ADAPTERS.CONTEXT_RECS;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;

import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistsAdapter extends RecyclerView.Adapter<MyPlaylistsAdapter.PlaylistViewHolder>{

    private Context context;
    private List<SimplifiedPlaylistObject> playlistItemList;
    private SparseBooleanArray selectedItems;

    public MyPlaylistsAdapter(Context context, List<SimplifiedPlaylistObject> playlistItemList) {
        this.context = context;
        this.playlistItemList = playlistItemList;
        this.selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_playlist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        SimplifiedPlaylistObject playlistItem = playlistItemList.get(position);
        holder.playlistName.setText(playlistItem.getName());

        Picasso.get().load(playlistItem.getImages().get(0).getUrl()).into(holder.playlistPic);

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(selectedItems.get(position, false));
        holder.itemView.setOnClickListener(v ->
        {
            openPlaylistInSpotify(position);
        });

        holder.checkbox.setOnClickListener(v -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                toggleSelection(holder.getAdapterPosition());
            }
        });

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> toggleSelection(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return playlistItemList.size();
    }
    private void openPlaylistInSpotify(int position) {

        String spotifyUri = playlistItemList.get(position).getExternalUrls().getSpotify();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.spotify.music");
        context.startActivity(intent);
    }
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<SimplifiedPlaylistObject> getSelectedItems() {
        List<SimplifiedPlaylistObject> selectedPlaylists = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedPlaylists.add(playlistItemList.get(selectedItems.keyAt(i)));
        }
        return selectedPlaylists;
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView playlistPic;
        TextView playlistName;
        CheckBox checkbox;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistPic = itemView.findViewById(R.id.playlistPic);
            playlistName = itemView.findViewById(R.id.playlistName);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
