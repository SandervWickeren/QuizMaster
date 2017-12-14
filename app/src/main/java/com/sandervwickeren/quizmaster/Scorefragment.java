package com.sandervwickeren.quizmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;


/***********************************************************************
 Fragment that shows up at the end of the game. It displays the score,
 the position and the current top 5 scores. The user can choose to play
 again or to go back to the Mainactivity / Home.

 ***********************************************************************/
public class Scorefragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scorefragment, container, false);

        Button replay = v.findViewById(R.id.replay);
        replay.setOnClickListener(this);
        Button home = v.findViewById(R.id.home);
        home.setOnClickListener(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get instances
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Remove seconds display
        android.support.v7.app.ActionBar actionBar = ((QuizActivity)getActivity()).getSupportActionBar();
        View v = actionBar.getCustomView();
        TextView timerText = v.findViewById(R.id.time);
        timerText.setVisibility(View.INVISIBLE);

        // Generating layout
        genLayout(this.getArguments());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replay:
                Intent replayIntent = new Intent(getActivity(), QuizActivity.class);
                Scorefragment.this.startActivity(replayIntent);
                getActivity().finish();
                break;
            case R.id.home:
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                Scorefragment.this.startActivity(homeIntent);
                getActivity().finish();
                break;
        }
    }
    public void getNameAndSave(final Integer score) {
        // Check if logged in
        if (currentUser != null) {

            // Add listener for single event to prevent infinite looping.
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Get username
                    User user = dataSnapshot.child("users").child(currentUser.getUid()).getValue(User.class);
                    username = user.name;

                    // Make instance of score
                    Score newScore = new Score(username, score);

                    // Get new post key
                    String newKey = mDatabase.child("scores").push().getKey();

                    // Save score to db
                    mDatabase.child("scores").child(newKey).setValue(newScore);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void getScores(final Integer achievedScore) {
        Query query = mDatabase.child("scores").orderByChild("score").limitToLast(100);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Score> scores = new ArrayList<>();

                /*Convert every row to instance of score class and add them to
                a list. */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Score score = postSnapshot.getValue(Score.class);
                    scores.add(score);
                }
                // Gen layout listview
                try {
                    ListView highscores = getView().findViewById(R.id.scorelist);
                    // Clear list (necessary when updates occur to recreate list.)
                    highscores.setAdapter(null);

                    // Reverse list to get heighest at the top.
                    Collections.reverse(scores);

                    // Check if achieved score in top 100
                    for (Score score: scores){
                        if (score.score <= achievedScore) {
                            Integer position = scores.indexOf(score);

                            // Set position
                            TextView pos = getView().findViewById(R.id.position);
                            pos.setText(String.valueOf(position + 1));

                            // Quit looping
                            break;
                        }
                    }
                    // Select top 5
                    ArrayList<Score> sliced = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        sliced.add(scores.get(i));
                    }

                    // Set list adapter with the top 5
                    HighscoreListAdapter adapter = new HighscoreListAdapter(getContext(),
                            sliced);
                    highscores.setAdapter(adapter);

                    // Remove load icon
                    getView().findViewById(R.id.loadingHighscores).setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.d("Highscore Error", "fillListview:Couldn't do it.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void genLayout(Bundle bundle) {
        TextView score = getView().findViewById(R.id.score);
        int finalscore = bundle.getInt("score");

        // Set score
        score.setText(Integer.toString(finalscore));

        // Save score
        getNameAndSave(finalscore);

        // Fill highscore list and position
        getScores(finalscore);
    }
}
