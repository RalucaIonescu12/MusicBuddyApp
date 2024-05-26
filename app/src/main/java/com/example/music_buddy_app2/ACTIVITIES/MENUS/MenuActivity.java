package com.example.music_buddy_app2.ACTIVITIES.MENUS;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.OTHERS.AnalysisActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.BrowseRecommendationTypesActivity;
import com.example.music_buddy_app2.ACTIVITIES.OTHERS.LeaderboardActivity;
import com.example.music_buddy_app2.ACTIVITIES.OTHERS.MultiplayerGamesActivity;
import com.example.music_buddy_app2.ACTIVITIES.PROFILE.FindFriendsActivity;
import com.example.music_buddy_app2.ACTIVITIES.PROFILE.ProfileActivity;
import com.example.music_buddy_app2.ACTIVITIES.OTHERS.SingleplayerGamesActivity;
import com.example.music_buddy_app2.ACTIVITIES.LOGIN.WelcomeActivity;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.SERVICES.API.TokenManager;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity {

    RecyclerView rv;
    ImageView vynilImage;
    ArrayList<String> menuOptions;
    ArrayList<String> menuOptionsImage;
    ArrayList<String> menuOptionsDescription;
    LinearLayoutManager linearLayoutManager;
    CardView cardView;
    MyRVAdapter myRVAdapter;
    Animation rotationAnimation;
    @Override
    protected void onResume() {
        super.onResume();
        TokenManager.initialize(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        rv=findViewById(R.id.menuRV);
        cardView = findViewById(R.id.vynil_profile);
        //animation for vynil
        vynilImage= (ImageView) findViewById(R.id.vynil_image);
        rotateAnimation();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ProfileActivity when CardView is clicked
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //menu options of the app
        menuOptions=new ArrayList<>();
        menuOptions.add("Recommendations");
        menuOptions.add("Connect with friends");

        menuOptionsDescription=new ArrayList<>();
        menuOptionsDescription.add("experiment with our recommendation methods and find new songs");
        menuOptionsDescription.add("find friends and compare your music");

        menuOptionsImage=new ArrayList<>();
        menuOptionsImage.add(String.valueOf(R.drawable.recommendation));
        menuOptionsImage.add(String.valueOf(R.drawable.multiplayer));
        linearLayoutManager= new LinearLayoutManager(MenuActivity.this,LinearLayoutManager.HORIZONTAL,false);
        myRVAdapter = new MyRVAdapter(menuOptions, menuOptionsDescription,menuOptionsImage);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(myRVAdapter);
    }
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(MenuActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
    class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyHolder>

    {
        ArrayList<String> titleData;
        ArrayList<String> desciptionData;
        ArrayList<String> imageData;
        public MyRVAdapter(ArrayList<String> titleData ,ArrayList<String> descriptionData,ArrayList<String> imageData) {
            this.titleData= titleData;
            this.desciptionData=descriptionData;
            this.imageData=imageData;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MenuActivity.this).inflate(R.layout.rv_menu_item,parent , false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.Title.setText(titleData.get(position));
            holder.Description.setText(desciptionData.get(position));
            holder.ImageView.setImageResource(Integer.parseInt(imageData.get(position)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedOption = titleData.get(holder.getAdapterPosition());
                    switch (selectedOption) {
                        case "Recommendations":
                            startActivity(new Intent(MenuActivity.this, BrowseRecommendationTypesActivity.class));
                            break;
                        case "Connect with friends":
                            startActivity(new Intent(MenuActivity.this, FindFriendsActivity.class));
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
            ImageView ImageView;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                Title= itemView.findViewById(R.id.menu_option_title_text_view);
                Description= itemView.findViewById(R.id.menu_option_description_text_view);
                ImageView = itemView.findViewById(R.id.menu_option_image_view);
            }
        }
    }
    private void rotateAnimation()
    {
        rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_vynil);
        vynilImage.startAnimation(rotationAnimation);
    }
}