package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Questionfragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_questionfragment, container, false);


        genLayout(this.getArguments());


        return v;
    }

    public void genLayout(Bundle bundle) {




    }

}
