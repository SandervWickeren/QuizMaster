package com.sandervwickeren.quizmaster;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/***********************************************************************
 Activity that holds the questionfragments. It prepares the game
 by retrieving the question from the opentDB API. After retrieving it
 parses it and sends it to the first questionfragment. By only requesting
 one time in the quiz, it reduces bandwith.

 ***********************************************************************/
public class QuizActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.quiz_actionbar_layout);

        if (savedInstanceState == null) {
            volleyRequest("test", "test", "test");
        }
    }


    public void startQuiz(ArrayList<String> questions) {

        // Generate information for fragment
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("questions", questions);
        bundle.putInt("current", 0);
        bundle.putInt("amount", 8);
        bundle.putInt("score", 0);

        FragmentManager fm = getSupportFragmentManager();
        Questionfragment fragment = new Questionfragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.quest_fragment_container, fragment, "question");
        ft.commit();
    }



    public void volleyRequest(String url, final String wkey, final String type){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        url = "https://opentdb.com/api.php?amount=8&type=multiple";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Put all information from jasonarray into list.
                        JSONArray resp = parseJason(response, "results");
                        //Toast.makeText(QuizActivity.this,  resp.toString(), Toast.LENGTH_LONG).show();

                        // Create question list
                        ArrayList<String> questions = new ArrayList<>();

                        // Add all questions to the list
                        if (resp != null) {
                            for (int i = 0; i < resp.length(); i++) {
                                try {
                                    questions.add(resp.getString(i));
                                    //Toast.makeText(QuizActivity.this, resp.getString(i), Toast.LENGTH_LONG).show();

                                } catch (Exception e) {

                                }
                            }
                            // End progressbar
                            findViewById(R.id.loadingProcess).setVisibility(View.GONE);

                            // Start quiz
                            startQuiz(questions);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    // Show error
                    Toast.makeText(QuizActivity.this,
                            "Can't reach the questions, please check your connection.", Toast.LENGTH_LONG).show();

                    // Return to base activity
                    Intent homeIntent = new Intent(QuizActivity.this, MainActivity.class);
                    QuizActivity.this.startActivity(homeIntent);
                    QuizActivity.this.finish();

                } else {
                    Toast.makeText(QuizActivity.this,
                            error.toString(), Toast.LENGTH_SHORT).show();
                }

            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public static JSONArray parseJason(String json, String key) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(key);


            return jsonArray;

        } catch (Exception e) {
            return null;
        }
    }


}
