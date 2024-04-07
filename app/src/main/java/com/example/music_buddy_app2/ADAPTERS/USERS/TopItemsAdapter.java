package com.example.music_buddy_app2.ADAPTERS.USERS;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_buddy_app2.API_RESPONSES.INTERFACES.TopItemInterface;
import com.example.music_buddy_app2.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopItemsAdapter extends BaseAdapter {
    private Context context;
    private List<? extends TopItemInterface> topItems;

    public TopItemsAdapter(Context context, List<? extends TopItemInterface> topItems) {
        this.context = context;
        this.topItems = topItems;
    }

    @Override
    public int getCount() {
        return topItems.size();
    }

    @Override
    public Object getItem(int position) {
        return topItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.top_grid_item, parent, false);
        }
        //tracks or artists?
        SwitchMaterial switchArtistsTracks = ((Activity) context).findViewById(R.id.switchArtistsTracks);

        ImageView imageView = convertView.findViewById(R.id.songImage);
        TextView songTitle = convertView.findViewById(R.id.songTitle);
        TextView artistName = convertView.findViewById(R.id.artistName);

        TopItemInterface currentItem = topItems.get(position);

        // Set data to views
        Picasso.get().load(currentItem.getImageUrl()).into(imageView);

        if (switchArtistsTracks.isChecked()) {
            songTitle.setVisibility(View.VISIBLE);
            songTitle.setText(currentItem.getName());
            artistName.setText(currentItem.getArtistName());
        } else {
            songTitle.setVisibility(View.GONE);
            artistName.setText(currentItem.getName());
        }
        return convertView;
    }
}
