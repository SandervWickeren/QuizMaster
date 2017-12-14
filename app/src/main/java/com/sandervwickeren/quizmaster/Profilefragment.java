package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;


/***********************************************************************
 Fragment that shows if a user is logged in. Contains a log out button
 so users can log out also.

 ***********************************************************************/
public class Profilefragment extends Fragment implements View.OnClickListener {


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

                // Pop fragment from backstack to prevent going back illegally
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack(this.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Launch Login fragment
                Loginfragment fragment = new Loginfragment();
                ((MainActivity)getActivity()).replaceFragment(fragment);
                break;
        }
    }

}
