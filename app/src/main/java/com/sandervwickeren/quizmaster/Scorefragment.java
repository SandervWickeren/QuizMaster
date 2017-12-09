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


public class Scorefragment extends Fragment implements View.OnClickListener {

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

        getValues(this.getArguments());
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

    public void getValues(Bundle bundle) {
        TextView score = getView().findViewById(R.id.score);
        int finalscore = bundle.getInt("score");

        score.setText(Integer.toString(finalscore));
    }

    public void nextFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slow_enter_from_right, R.anim.slow_exit_to_left, R.anim.slow_enter_from_left, R.anim.slow_exit_to_right);
        ft.replace(R.id.quest_fragment_container, fragment, fragment.getClass().getName());
        ft.commit();
    }

}
