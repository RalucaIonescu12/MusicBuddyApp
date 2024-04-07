package com.example.music_buddy_app2.ADAPTERS.USERS;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.API_RESPONSES.TRACKS_PLAYLISTS.SimplifiedPlaylistObject;
import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FriendsChecklistAdapter extends RecyclerView.Adapter<FriendsChecklistAdapter.UserViewHolder>  {

    private Context context;
    private List<User> users;
    private SparseBooleanArray selectedItems;
    public FriendsChecklistAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
        this.selectedItems = new SparseBooleanArray();
    }
    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.user_item_checkbox, parent, false);
        return new UserViewHolder(view);
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
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User userItem = users.get(position);
        holder.username.setText(userItem.getUsername());

        Picasso.get().load(userItem.getProfileImageUrl()).into(holder.profilePic);

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(selectedItems.get(position, false));

        holder.checkbox.setOnClickListener(v -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                toggleSelection(holder.getAdapterPosition());
                userItem.setSelected(holder.checkbox.isChecked());
            }
        });

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleSelection(holder.getAdapterPosition());
            userItem.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public List<User> getSelectedFriends() {
        List<User> selectedFriends = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedFriends.add(users.get(selectedItems.keyAt(i)));
        }
        return selectedFriends;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView username;
        CheckBox checkbox;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.user_profile_image);
            username = itemView.findViewById(R.id.username);
            checkbox = itemView.findViewById(R.id.checkbox);
        }

    }
}