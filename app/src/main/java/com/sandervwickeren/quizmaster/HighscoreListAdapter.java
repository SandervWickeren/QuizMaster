package com.sandervwickeren.quizmaster;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


/***********************************************************************
 Used to correctly adapt information retrieved from the score class
 into a listview.

 ***********************************************************************/
class HighscoreListAdapter extends ArrayAdapter<Score> {

    public HighscoreListAdapter(Context context, ArrayList<Score> values) {
        super(context, 0, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for the current position
        Score score = getItem(position);

        // Check if view is reused, otherwise inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_row_layout, parent, false);
        }

        // Lookup view for data population
        TextView pos = convertView.findViewById(R.id.position);
        TextView name = convertView.findViewById(R.id.name);
        TextView scoretext = convertView.findViewById(R.id.score);

        // Populate data
        pos.setText(String.valueOf(position + 1));
        name.setText(score.name);
        scoretext.setText(String.valueOf(score.score));

        // Return view to render
        return convertView;
    }


}