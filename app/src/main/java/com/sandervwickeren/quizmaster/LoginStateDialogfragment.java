package com.sandervwickeren.quizmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginStateDialogfragment extends DialogFragment implements View.OnClickListener {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_state_dialogfragment, container, false);

        // Set onclicklisteners
        Button cancel = v.findViewById(R.id.cancel);
        Button play = v.findViewById(R.id.play);
        cancel.setOnClickListener(this);
        play.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                // Close dialog.
                getDialog().cancel();
                break;
            case R.id.play:
                // Start activity
                getDialog().cancel();
                Intent intent = new Intent(getContext(), Quiz.class);
                getActivity().startActivity(intent);
                break;
        }
    }

}
