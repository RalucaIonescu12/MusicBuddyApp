package com.example.music_buddy_app2.ADAPTERS.USERS;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.FirebaseManagement.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ManageFriendsAdapter extends RecyclerView.Adapter<ManageFriendsAdapter.UserViewHolder>  {

    private Context context;
    private List<User> users;
    private UserManager userManager;
    public ManageFriendsAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
        userManager=UserManager.getInstance(context);
    }
    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(this.users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView username;
        private CardView button;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.user_profile_image);
            username = itemView.findViewById(R.id.username);
            button=itemView.findViewById(R.id.addBtn);

        }
         public void bind(User user) {
            username.setText(user.getUsername());
            Picasso.get().load(user.getProfileImageUrl()).into(profilePic);

            boolean addFollower=true;
            if(userManager.getCurrentUser().getFollowingIds()!= null && userManager.getCurrentUser().getFollowingIds().contains(user.getSpotifyId()))
                addFollower=false;

            if (!addFollower) {
                 button.setVisibility(View.GONE);
            } else
            {
                 button.setVisibility(View.VISIBLE);
                 button.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         userManager.followUser(user);
                     }
                 });
            }
        }
    }
}