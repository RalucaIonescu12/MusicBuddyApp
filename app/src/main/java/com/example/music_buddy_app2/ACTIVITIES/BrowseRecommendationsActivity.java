package com.example.music_buddy_app2.ACTIVITIES;

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

import com.example.music_buddy_app2.R;

import java.util.ArrayList;

public class BrowseRecommendationsActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<String> menuOptions;
    ArrayList<String> menuOptionsDescription;
    LinearLayoutManager linearLayoutManager;
    BrowseRecommendationsActivity.MyRVAdapter myRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recommendations);

        //recommendation options of the app
        menuOptions=new ArrayList<>();
        menuOptions.add("Recommendations from Spotify");
        menuOptions.add("Our recommendations");
        menuOptions.add("Artists Recommendations");
//        menuOptions.add("Leaderboard");
//        menuOptions.add("Analysis");

        menuOptionsDescription=new ArrayList<>();
        menuOptionsDescription.add("filter spotify recommendations by the song's popularity, energy, danceability etc. ");
        menuOptionsDescription.add("get music or artists based on specific playlists/albums using our algorithms");
        menuOptionsDescription.add("get music and artists using our recommendation algorithms by mentioning specific artists or tracks");
//        menuOptionsDescription.add("see if you're really a music genious");
//        menuOptionsDescription.add("let me diagnose you're music patterns");
        rv = findViewById(R.id.recommendation_rv);
        linearLayoutManager= new LinearLayoutManager(BrowseRecommendationsActivity.this,LinearLayoutManager.VERTICAL,false);
        myRVAdapter = new BrowseRecommendationsActivity.MyRVAdapter(menuOptions, menuOptionsDescription);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(myRVAdapter);
    }

    class MyRVAdapter extends RecyclerView.Adapter<BrowseRecommendationsActivity.MyRVAdapter.MyHolder>

    {
        ArrayList<String> titleData;
        ArrayList<String> desciptionData;
        public MyRVAdapter(ArrayList<String> titleData ,ArrayList<String> descriptionData) {
            this.titleData= titleData;
            this.desciptionData=descriptionData;
        }

        @NonNull
        @Override
        public BrowseRecommendationsActivity.MyRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BrowseRecommendationsActivity.this).inflate(R.layout.rv_recommendation_item,parent , false);
            return new BrowseRecommendationsActivity.MyRVAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BrowseRecommendationsActivity.MyRVAdapter.MyHolder holder, int position) {
            holder.Title.setText(titleData.get(position));
            holder.Description.setText(desciptionData.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedOption = titleData.get(holder.getAdapterPosition());
                    switch (selectedOption) {
                        case "Recommendations from Spotify":
                            startActivity(new Intent(BrowseRecommendationsActivity.this, RecommendationsBySpotifyActivity.class));
//                        case "Multiplayer games":
//                            startActivity(new Intent(RecommendationsActivity.this, MultiplayerGamesActivity.class));
//                            break;
//                        case "Singleplayer Games":
//                            startActivity(new Intent(RecommendationsActivity.this, SingleplayerGamesActivity.class));
//                            break;
//                        case "Leaderboard":
//                            startActivity(new Intent(RecommendationsActivity.this, LeaderboardActivity.class));
//                            break;
//                        case "Analysis":
//                            startActivity(new Intent(RecommendationsActivity.this, AnalysisActivity.class));
//                            break;
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