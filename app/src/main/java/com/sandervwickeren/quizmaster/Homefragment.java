package com.sandervwickeren.quizmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Homefragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homefragment, container, false);


        Button start = v.findViewById(R.id.start);
        start.setOnClickListener(new start_click());

        return v;
    }
    private class start_click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), Quiz.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

}
