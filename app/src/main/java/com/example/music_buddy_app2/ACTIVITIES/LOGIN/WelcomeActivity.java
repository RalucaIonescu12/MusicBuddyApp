package com.example.music_buddy_app2.ACTIVITIES.LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_buddy_app2.ACTIVITIES.BaseActivity;
import com.example.music_buddy_app2.ACTIVITIES.MENUS.MenuActivity;
import com.example.music_buddy_app2.MANAGERS.UserManager;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;
import com.example.music_buddy_app2.MANAGERS.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity  extends AppCompatActivity {


    private TextView helloTextView;
    private TextView doyouknowTextView;
    private TextView illuseTextView;
    Button loginButton;
    private String[] fullTexts;
    private int currentLength = 0;
    private UserManager manager;
    private static final int DELAY_MILLIS = 30;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        try {
             String firebaseToken=SharedPreferencesManager.getCustomToken(this);
                if (firebaseToken != null && !firebaseToken.isEmpty()) {
                    Log.e("MY_LOGS","Exista shared preferences.");
                if(manager==null)
                    manager=UserManager.getInstance(WelcomeActivity.this);
//                manager.addUserToDatabase(new UserManager.OnTaskCompleteListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e("MY_LOGS"," adaugat la on create in welcome activity.");
//                        goToMenu();
//                    }
//
//                    @Override
//                    public void onFailure(String errorMessage) {
//                        goToLogin();
//                        Log.e("MY_LOGS"," fail la on create weldome activity.");
//                    }
//                });
                manager.setCurrentUser(new UserManager.OnUserReceivedListener() {
                    @Override
                    public void onUserReceived(User user) {
                        manager.addUserToDatabase(new UserManager.OnTaskCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Log.e("MY_LOGS","in on resume in base activity:");

//                                String userId = SharedPreferencesManager.getUserId(WelcomeActivity.this);
//
//
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference userRef = database.getReference("users").child(userId).child("playlistsCreatedWithTheApp");
//                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            Integer playlistsCreated = dataSnapshot.getValue(Integer.class);
//                                            if (playlistsCreated != null) {
//                                               SharedPreferencesManager.updateNbrPlaylists(WelcomeActivity.this,playlistsCreated);
//                                            } else {
//                                                SharedPreferencesManager.updateNbrPlaylists(WelcomeActivity.this,0);
//                                            }
//                                        } else {
//                                            SharedPreferencesManager.updateNbrPlaylists(WelcomeActivity.this,0);
//                                        }
                                        goToMenu();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        Log.e("MY_LOGS", "Database error: " + databaseError.getMessage());
//                                        goToMenu();
//                                    }
//                                });

                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                goToLogin();
                                Log.e("MY_LOGS"," fail la on create weldome activity.");
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        goToLogin();
                        Log.e("MY_LOGS"," fail la on create weldome activity.");
                    }
                });

            }
            else
            {
                helloTextView = findViewById(R.id.hello);
                doyouknowTextView = findViewById(R.id.do_you_know);
                illuseTextView = findViewById(R.id.ill_use);
                fullTexts = new String[]{
                        "Hello!",
                        "Do you know that friend that knows everything about music? That's me!",
                        "I'll use your Spotify account for that!"
                };
                loginButton = findViewById(R.id.login_spotify);
                loginButton.setVisibility(View.INVISIBLE);

                startTypingAnimation(0);
            }
        }
        catch (Exception e)
        {
            Log.e("MY_LOGS",e.toString());
            goToLogin();

        }

    }

    private void goToMenu()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void startTypingAnimation(final int textViewIndex) {
        if (textViewIndex >= fullTexts.length) {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            });
            return;
        }

        final TextView currentTextView;
        switch (textViewIndex) {
            case 0:
                currentTextView = helloTextView;
                break;
            case 1:
                currentTextView = doyouknowTextView;
                break;
            case 2:
                currentTextView = illuseTextView;
                break;
            default:
                return;
        }

        handler.postDelayed(() -> {
            if (currentLength <= fullTexts[textViewIndex].length()) {
                String partialText = fullTexts[textViewIndex].substring(0, currentLength);
                currentTextView.setText(Html.fromHtml(partialText));
                currentLength++;
                startTypingAnimation(textViewIndex);
            } else {
                currentLength = 0;
                startTypingAnimation(textViewIndex + 1);
            }
        }, DELAY_MILLIS);

    }
}