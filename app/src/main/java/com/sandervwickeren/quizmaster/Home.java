package com.sandervwickeren.quizmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new start_click());
    }

    private class start_click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Home.this, Quiz.class);
            Home.this.startActivity(intent);
        }
    }
}
