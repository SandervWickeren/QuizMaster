package com.sandervwickeren.quizmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

        // Generating layout
        genLayout(this.getArguments());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replay:
                Intent replayIntent = new Intent(getActivity(), Quiz.class);
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
            Toast.makeText(getActivity(),
                    "Not logged in",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void genLayout(Bundle bundle) {
        TextView score = getView().findViewById(R.id.score);
        int finalscore = bundle.getInt("score");

        // Set score
        score.setText(Integer.toString(finalscore));

        // Save score
        getNameAndSave(finalscore);
    }
}
