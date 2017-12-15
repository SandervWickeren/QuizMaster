package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;


/***********************************************************************
 Fragment that shows all highscores in a list using the
 HighscoreListAdapter

 ***********************************************************************/
public class Highscoresfragment extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_highscoresfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get instances
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Generate layout
        fillHighscoreList();
    }
    private void fillHighscoreList() {
        Query query = mDatabase.child("scores").orderByChild("score").limitToLast(20);
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
                    ListView highscores = getView().findViewById(R.id.highscoreList);
                    // Clear list (necessary when updates occur to recreate list.)
                    highscores.setAdapter(null);

                    // Reverse list to get heighest at the top.
                    Collections.reverse(scores);

                    // Set list adapter
                    HighscoreListAdapter adapter = new HighscoreListAdapter(getContext(), scores);
                    highscores.setAdapter(adapter);

                    // Remove load icon
                    getView().findViewById(R.id.loadingHighscores).setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),
                            "Couldn't load highscores, check your connection.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database Error:", databaseError.toString());
            }
        });
    }

}
