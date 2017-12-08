package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Questionfragment extends Fragment implements View.OnClickListener {

    ArrayList<String> answers = new ArrayList<>();

    // Saves position of correct answer.
    int cor_answer_pos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_questionfragment, container, false);
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        answers.clear();
        getValues(this.getArguments());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.answer_a:
                Button ans_a = getView().findViewById(R.id.answer_a);
                checkAnswer(ans_a, 0);
                break;
            case R.id.answer_b:
                Button ans_b = getView().findViewById(R.id.answer_b);
                checkAnswer(ans_b, 1);
                break;
            case R.id.answer_c:
                Button ans_c = getView().findViewById(R.id.answer_c);
                checkAnswer(ans_c, 2);
                break;
            case R.id.answer_d:
                Button ans_d = getView().findViewById(R.id.answer_d);
                checkAnswer(ans_d, 3);
                break;
        }
    }
    public void checkAnswer (Button button, int given) {
        if (Objects.equals(given, cor_answer_pos)) {

            // Update visually green
            button.setBackgroundResource(R.drawable.button);

            // Next question, update score
            nextQuestion();

        } else {
            // Update visually red
            button.setBackgroundResource(R.drawable.red_button);

            // Mark the correct answer
            if (cor_answer_pos == 0) {
                Button ans_a = getView().findViewById(R.id.answer_a);
                ans_a.setBackgroundResource(R.drawable.button);
            } else if (cor_answer_pos == 1) {
                Button ans_b = getView().findViewById(R.id.answer_b);
                ans_b.setBackgroundResource(R.drawable.button);
            }
            else if (cor_answer_pos == 2) {
                Button ans_c = getView().findViewById(R.id.answer_c);
                ans_c.setBackgroundResource(R.drawable.button);
            }
            else if (cor_answer_pos == 3) {
                Button ans_d = getView().findViewById(R.id.answer_d);
                ans_d.setBackgroundResource(R.drawable.button);
            }

            // Next question, update score
            nextQuestion();
        }
    }

    public void getValues(Bundle bundle) {
        ArrayList<String> questions = bundle.getStringArrayList("questions");
        Integer current = bundle.getInt("current");

        try {
            // Get correct question by number.
            JSONObject jsonObject = new JSONObject(questions.get(current));

            // Get question and correct answer.
            String question = jsonObject.getString("question");
            String cor_answer = jsonObject.getString("correct_answer");

            // Get wrong answers.
            JSONArray jsonArray = jsonObject.getJSONArray("incorrect_answers");

            // Combine wrong and correct answers in a list.
            answers.add(cor_answer);
            for (int i = 0; i < jsonArray.length(); i++) {
                answers.add(jsonArray.getString(i));
            }

            // Shuffle list.
            Collections.shuffle(answers);

            // Save position of answer.
            cor_answer_pos = answers.indexOf(cor_answer);

            // Call genLayout.
            genLayout(answers, question);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void genLayout(List<String> shuffled, String question) {

        // Set question
        TextView question_text = getView().findViewById(R.id.question);
        String question_encoded = Html.fromHtml(question).toString();
        question_text.setText(question_encoded);

        // Get button views
        Button answer_a = getView().findViewById(R.id.answer_a);
        Button answer_b = getView().findViewById(R.id.answer_b);
        Button answer_c = getView().findViewById(R.id.answer_c);
        Button answer_d = getView().findViewById(R.id.answer_d);

        // Set clicklisteners
        answer_a.setOnClickListener(this);
        answer_b.setOnClickListener(this);
        answer_c.setOnClickListener(this);
        answer_d.setOnClickListener(this);

        // Set button text
        answer_a.setText(shuffled.get(0));
        answer_b.setText(shuffled.get(1));
        answer_c.setText(shuffled.get(2));
        answer_d.setText(shuffled.get(3));
    }

    public void nextQuestion() {
        // Get bundle information
        Bundle old_bundle = this.getArguments();

        // Check if last question is reached
        if (Objects.equals(old_bundle.getInt("amount"), old_bundle.getInt("current") + 1)) {

            // Go to finish activity
        } else {

            // Make new bundle
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("questions", old_bundle.getStringArrayList("questions"));
            bundle.putInt("current", old_bundle.getInt("current") + 1);
            bundle.putInt("amount", old_bundle.getInt("amount"));

            // Inflate new fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Questionfragment fragment = new Questionfragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            ft.replace(R.id.quest_fragment_container, fragment, "question");
            ft.commit();
        }



    }



}
