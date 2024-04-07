package com.example.music_buddy_app2.ACTIVITIES.MENUS;

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

import com.example.music_buddy_app2.ACTIVITIES.ChooseContextDetailsActivity;
import com.example.music_buddy_app2.R;

import java.util.ArrayList;

public class ContextRecsMenuActivity extends AppCompatActivity {
    
    RecyclerView rv;
    ArrayList<String> menuOptions;
    ArrayList<String> menuOptionsDescription;
    LinearLayoutManager linearLayoutManager;
    ContextRecsMenuActivity.MyRVAdapter myRVAdapter;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_recs_menu);

        //context options
        menuOptions=new ArrayList<>();
        menuOptions.add("Studying");
        menuOptions.add("House chores");
        menuOptions.add("Road trip");
        menuOptions.add("Driving alone");
        menuOptions.add("Party");
        menuOptions.add("Predrinks");
        menuOptions.add("Chilling");
        menuOptions.add("Work out");
        menuOptions.add("Background music");

        menuOptionsDescription=new ArrayList<>();
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        menuOptionsDescription.add("");
        rv = findViewById(R.id.contexts_rv);
        linearLayoutManager= new LinearLayoutManager(ContextRecsMenuActivity.this,LinearLayoutManager.VERTICAL,false);
        myRVAdapter = new ContextRecsMenuActivity.MyRVAdapter(menuOptions, menuOptionsDescription);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(myRVAdapter);
    }

    class MyRVAdapter extends RecyclerView.Adapter<ContextRecsMenuActivity.MyRVAdapter.MyHolder> {
        ArrayList<String> titleData;
        ArrayList<String> desciptionData;

        public MyRVAdapter(ArrayList<String> titleData, ArrayList<String> descriptionData) {
            this.titleData = titleData;
            this.desciptionData = descriptionData;
        }

        @NonNull
        @Override
        public ContextRecsMenuActivity.MyRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ContextRecsMenuActivity.this).inflate(R.layout.rv_context_item, parent, false);
            return new ContextRecsMenuActivity.MyRVAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContextRecsMenuActivity.MyRVAdapter.MyHolder holder, int position) {
            holder.Title.setText(titleData.get(position));
            holder.Description.setText(desciptionData.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedOption = titleData.get(holder.getAdapterPosition());
                    switch (selectedOption) {
                        default:
                            startActivity(new Intent(ContextRecsMenuActivity.this, ChooseContextDetailsActivity.class));
                            break;

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return titleData.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView Title;
            TextView Description;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                Title = itemView.findViewById(R.id.context_option_title_text_view);
                Description = itemView.findViewById(R.id.context_option_description_text_view);
            }
        }
    }
}