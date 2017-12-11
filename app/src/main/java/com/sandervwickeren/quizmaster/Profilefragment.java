package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class Profilefragment extends Fragment implements View.OnClickListener {

    Button logOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profilefragment, container, false);

        Button logOut = v.findViewById(R.id.logOut);
        logOut.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logOut:
                // Logout
                FirebaseAuth.getInstance().signOut();

                // Launch Login fragment
                Loginfragment fragment = new Loginfragment();
                ((MainActivity)getActivity()).replaceFragment(fragment);
                break;
        }
    }

}
