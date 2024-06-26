package com.example.music_buddy_app2.ADAPTERS.CONTEXT_RECS;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;

import com.example.music_buddy_app2.MANAGERS.ChooseContextDetailsManager;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class FriendsPlaylistsAdapter extends RecyclerView.Adapter<FriendsPlaylistsAdapter.FriendsPlaylistViewHolder> {
    private Context context;
    private List<SimplifiedPlaylistObject> playlistItems;
    private ChooseContextDetailsManager manager;
    private List<SimplifiedPlaylistObject> selectedPlaylistItems;
    private OnFriendsPlaylistSelectedListener listener;
    public FriendsPlaylistsAdapter(Context context, List<SimplifiedPlaylistObject> friendsPlaylists, OnFriendsPlaylistSelectedListener listener) {
        this.context = context;
        this.playlistItems = friendsPlaylists;
        this.selectedPlaylistItems = new ArrayList<>();
        this.listener=listener;
        this.manager=ChooseContextDetailsManager.getInstance(context);
    }

    public void setPlaylistItems(List<SimplifiedPlaylistObject> playlistItems) {
        this.playlistItems = playlistItems;
        notifyDataSetChanged();
    }

    public List<SimplifiedPlaylistObject> getSelectedPlaylistItems() {
        return selectedPlaylistItems;
    }

    @NonNull
    @Override
    public FriendsPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_user_playlist_item, parent, false);
        return new FriendsPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsPlaylistViewHolder holder, int position) {
        SimplifiedPlaylistObject playlistItem = playlistItems.get(position);
        holder.bind(playlistItem);

    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }
    public interface OnFriendsPlaylistSelectedListener{
        void onFriendsPlaylistSelected();
        void onFriendsPlaylistUncheck();
        void onFriendsLimitExceeded();
    }
    public void reset() {
        selectedPlaylistItems.clear();
        notifyDataSetChanged();
    }
    public class FriendsPlaylistViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private ImageView playlistImage;
        private TextView username;
        private TextView playlistName;
        private TextView nbrOfSongs;

        public FriendsPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            playlistImage = itemView.findViewById(R.id.friednsPlaylistPic);
            username = itemView.findViewById(R.id.username);
            playlistName=itemView.findViewById(R.id.friendsPlaylistName);
            nbrOfSongs=itemView.findViewById(R.id.nbrOfSongss);
            playlistImage.setOnClickListener(v ->
            {
                openPlaylistInSpotify(getAdapterPosition());
            });
            playlistName.setOnClickListener(v ->
            {
                openPlaylistInSpotify(getAdapterPosition());
            });
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SimplifiedPlaylistObject playlistItem = playlistItems.get(getAdapterPosition());
                Integer nbr = playlistItem.getTracks().getTotal();
                if (isChecked) {
                    if(ChooseContextDetailsManager.nbrOfSongsAdded+nbr<=300)
                    {
                        manager.addToNbrOfSongs(nbr, playlistItem);
                        selectedPlaylistItems.add(playlistItem);
                        listener.onFriendsPlaylistSelected();
                    }
                    else{
                        checkBox.setChecked(false);
                        listener.onFriendsLimitExceeded();
                    }

                } else {
                    manager.deleteFromNbrOfSongs(nbr, playlistItem);
                    listener.onFriendsPlaylistUncheck();
                    selectedPlaylistItems.remove(playlistItem);
                }
            });
        }
        private void openPlaylistInSpotify(int position) {
            String spotifyUri = playlistItems.get(position).getExternalUrls().getSpotify();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.spotify.music");
            context.startActivity(intent);
        }
        public void bind(SimplifiedPlaylistObject playlistItem) {
            checkBox.setChecked(selectedPlaylistItems.contains(playlistItem));
            Picasso.get().load(playlistItem.getImages().get(0).getUrl()).into(playlistImage);
            playlistName.setText(playlistItem.getName());
            username.setText(playlistItem.getOwner().getDisplayName());
            nbrOfSongs.setText(playlistItem.getTracks().getTotal() + " songs");
        }
    }
}