package com.example.music_buddy_app2.ACTIVITIES.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_buddy_app2.ACTIVITIES.OUR_RECOMMENDATIONS.ChooseContextDetailsActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.DiscoverGenresMenuActivity;
import com.example.music_buddy_app2.ACTIVITIES.SPOTIFY_RECOMMENDATIONS.StartSpotifyRecommendationsActivity;
import com.example.music_buddy_app2.R;

import java.util.ArrayList;

public class BrowseRecommendationTypesActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<String> menuOptions;
    ArrayList<String> menuOptionsDescription;
    LinearLayoutManager linearLayoutManager;
    BrowseRecommendationTypesActivity.MyRVAdapter myRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recommendation_types);

        //recommendation options of the app
        menuOptions=new ArrayList<>();
        menuOptions.add("Recommendations from Spotify");
        menuOptions.add("Discover each genre");
        menuOptions.add("Context recommendations");
//        menuOptions.add("Leaderboard");
//        menuOptions.add("Analysis");

        menuOptionsDescription=new ArrayList<>();
        menuOptionsDescription.add("filter spotify recommendations by the song's popularity, energy, danceability etc. ");
        menuOptionsDescription.add("discover each existing genre based on listening history ");
        menuOptionsDescription.add("tell us if you're in a group setting, the mood and genres and get recommendations");
//        menuOptionsDescription.add("see if you're really a music genious");
//        menuOptionsDescription.add("let me diagnose you're music patterns");
        rv = findViewById(R.id.recommendation_rv);
        linearLayoutManager= new LinearLayoutManager(BrowseRecommendationTypesActivity.this,LinearLayoutManager.VERTICAL,false);
        myRVAdapter = new BrowseRecommendationTypesActivity.MyRVAdapter(menuOptions, menuOptionsDescription);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(myRVAdapter);
    }

    class MyRVAdapter extends RecyclerView.Adapter<BrowseRecommendationTypesActivity.MyRVAdapter.MyHolder>

    {
        ArrayList<String> titleData;
        ArrayList<String> desciptionData;
        public MyRVAdapter(ArrayList<String> titleData ,ArrayList<String> descriptionData) {
            this.titleData= titleData;
            this.desciptionData=descriptionData;
        }

        @NonNull
        @Override
        public BrowseRecommendationTypesActivity.MyRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BrowseRecommendationTypesActivity.this).inflate(R.layout.rv_recommendation_item,parent , false);
            return new BrowseRecommendationTypesActivity.MyRVAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BrowseRecommendationTypesActivity.MyRVAdapter.MyHolder holder, int position) {
            holder.Title.setText(titleData.get(position));
            holder.Description.setText(desciptionData.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedOption = titleData.get(holder.getAdapterPosition());
                    switch (selectedOption) {
                        case "Recommendations from Spotify":
                            startActivity(new Intent(BrowseRecommendationTypesActivity.this, StartSpotifyRecommendationsActivity.class));
                            break;
                        case "Discover each genre":
                            startActivity(new Intent(BrowseRecommendationTypesActivity.this, DiscoverGenresMenuActivity.class));
                            break;
                        case "Context recommendations":
                            startActivity(new Intent(BrowseRecommendationTypesActivity.this, ChooseContextDetailsActivity.class));
                            break;
                        default:
                            break;

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return titleData.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView Title;
            TextView Description;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                Title= itemView.findViewById(R.id.recommendation_option_title_text_view);
                Description= itemView.findViewById(R.id.recommendation_option_description_text_view);
            }
        }
    }

}