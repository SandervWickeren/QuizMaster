package com.sandervwickeren.quizmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;


/***********************************************************************
 Fragment that show the rules of the games and from here the game
 can be started. It visually shows if the user is logged in by the
 color of the button.

 ***********************************************************************/
public class Homefragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homefragment, container, false);


        Button start = v.findViewById(R.id.start);
        start.setOnClickListener(new start_click());

        // Visually change button color based on login state
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            start.setBackgroundResource(R.drawable.unimportant_button);
        } else {
            start.setBackgroundResource(R.drawable.button);
        }

        return v;
    }
    private class start_click implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // If not logged in give warning, otherwise start new quiz activity.
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                LoginStateDialogfragment fragment = new LoginStateDialogfragment();
                fragment.show(ft, "dialog");
            } else {
                Intent intent = new Intent(getContext(), QuizActivity.class);
                getActivity().startActivity(intent);
            }
        }
    }

}
