package com.sandervwickeren.quizmaster;



import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/***********************************************************************
 Fragment that is used to show questions. Every new question is loaded
 into this fragment, which will randomly shuffle the answers to a button.
 And visually show information as: amount of questions to go, category and
 the timer.
 ***********************************************************************/
public class Questionfragment extends Fragment implements View.OnClickListener {

    ArrayList<String> answers = new ArrayList<>();

    // Saves position of correct answer.
    int cor_answer_pos;

    // Holds timer
    CountDownTimer timer;

    // Hold time
    long secondsLeft;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_questionfragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        answers.clear();
        getValues(this.getArguments());

    }
    @Override
    public void onStop() {
        super.onStop();

        // Stop timer to prevent background actions
        timer.cancel();
        timer = null;
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

            // Get score
            Integer finalScore = calculateScore(secondsLeft, Boolean.TRUE);

            // Next question, update score
            nextQuestion(finalScore);

        } else {
            // Update visually red if possible
            try {
                button.setBackgroundResource(R.drawable.red_button);
            } catch (Exception e) {
                Log.d("TimerError", e.toString());
            }

            // Vibrate phone
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);

            // Mark the correct answer
            if (cor_answer_pos == 0) {
                Button ans_a = getView().findViewById(R.id.answer_a);
                ans_a.setBackgroundResource(R.drawable.button);
            } else if (cor_answer_pos == 1) {
                Button ans_b = getView().findViewById(R.id.answer_b);
                ans_b.setBackgroundResource(R.drawable.button);
            } else if (cor_answer_pos == 2) {
                Button ans_c = getView().findViewById(R.id.answer_c);
                ans_c.setBackgroundResource(R.drawable.button);
            } else if (cor_answer_pos == 3) {
                Button ans_d = getView().findViewById(R.id.answer_d);
                ans_d.setBackgroundResource(R.drawable.button);
            }
            // Get score
            Integer finalScore = calculateScore(secondsLeft, Boolean.FALSE);

            // Next question, update score
            nextQuestion(finalScore);
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
            String category = jsonObject.getString("category");

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

            // Create question info
            String questInfo = String.valueOf(current + 1) + "/8 - " + category;

            // Call genLayout.
            genLayout(answers, question, questInfo);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void genLayout(List<String> shuffled, String question, String questInfo) {

        // Set question
        TextView question_text = getView().findViewById(R.id.question);
        TextView questionInfo = getView().findViewById(R.id.questionInfo);
        String question_encoded = Html.fromHtml(question).toString();
        question_text.setText(question_encoded);
        questionInfo.setText(questInfo);

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

        // Set button text and fix html encoding
        answer_a.setText(Html.fromHtml(shuffled.get(0)));
        answer_b.setText(Html.fromHtml(shuffled.get(1)));
        answer_c.setText(Html.fromHtml(shuffled.get(2)));
        answer_d.setText(Html.fromHtml(shuffled.get(3)));

        startTimer();
    }

    public void startTimer() {
        try {
            // Get Actionbar view
            android.support.v7.app.ActionBar actionBar = ((QuizActivity)getActivity()).getSupportActionBar();
            View v = actionBar.getCustomView();
            final TextView timerText = v.findViewById(R.id.time);

            // Replace original color for new question
            timerText.setTextColor(getResources().getColor(R.color.colorLightBackground));

            // Get progressbar
            final ProgressBar progressBar = getView().findViewById(R.id.timeLeftProgressbar);
            progressBar.setProgress(0);

            // Create new timer and start
            timer = new CountDownTimer(30000, 1000) {
                public void onTick(long millUntilFinished) {
                    secondsLeft = millUntilFinished / 1000;
                    String timeLeft = "'" + String.valueOf(secondsLeft);
                    timerText.setText(timeLeft);
                    Integer progressleft = (int) millUntilFinished;
                    progressBar.setProgress(30000 - progressleft);

                    // Change color if almost finished
                    if (millUntilFinished / 1000 < 10) {
                        timerText.setTextColor(getResources().getColor(R.color.colorEndingTime));
                    }
                }
                public void onFinish() {

                    // When finished it should show the correct answer
                    Button button = null;
                    checkAnswer(button, 10);
                }
            }.start();

        } catch (Exception e) {
            Log.d("TimerError", e.toString());
        }
    }
    public int calculateScore(long time, Boolean correct) {
        if (!correct) {
            // Prevent quick quessing
            if (time > 28) {
                long score = - 50 - time;
                return (int) score;
            } else {
                long score = - time;
                return (int) score;
            }
        } else {

            // Quicker correctness results in higher score.
            long score = 100 + ((30 - time) * 5);
            return (int) score;
        }
    }

    public void nextQuestion(int Score) {
        // Get bundle information
        Bundle old_bundle = this.getArguments();

        // Score shouldn't go below 0
        if (Score < 0) {
            Score = 0;
        }

        // Make new bundle
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("questions", old_bundle.getStringArrayList("questions"));
        bundle.putInt("current", old_bundle.getInt("current") + 1);
        bundle.putInt("amount", old_bundle.getInt("amount"));
        bundle.putInt("score", old_bundle.getInt("score") + Score);

        // Check if last question is reached
        if (Objects.equals(old_bundle.getInt("amount"), old_bundle.getInt("current") + 1)) {

            // Go to score fragment
            Scorefragment fragment = new Scorefragment();
            nextFragment(fragment, bundle);

        } else {
            // Inflate new fragment
            Questionfragment fragment = new Questionfragment();
            nextFragment(fragment, bundle);
        }
    }

    public void nextFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slow_enter_from_right, R.anim.slow_exit_to_left, R.anim.slow_enter_from_left, R.anim.slow_exit_to_right);
        ft.replace(R.id.quest_fragment_container, fragment, fragment.getClass().getName());
        ft.commit();
    }



}
