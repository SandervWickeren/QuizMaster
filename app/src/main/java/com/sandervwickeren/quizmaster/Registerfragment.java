package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Objects;


public class Registerfragment extends Fragment {

    EditText email, pass, repeatPass;
    TextView error_user, error_pass, error_pass2;
    Button back, register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registerfragment, container, false);

        // Buttons
        Button back = v.findViewById(R.id.back_button);
        Button register = v.findViewById(R.id.register_button);

        // Set onclicklisteners
        back.setOnClickListener(new backClick());
        register.setOnClickListener(new registerClick());

        return v;
    }

    private class backClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getFragmentManager().popBackStackImmediate();
        }
    }

    private class registerClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // Input
            EditText email = getView().findViewById(R.id.email);
            EditText pass = getView().findViewById(R.id.password);
            EditText repeatPass = getView().findViewById(R.id.password2);

            // Error report
            TextView error_user = getView().findViewById(R.id.error_user);
            TextView error_pass = getView().findViewById(R.id.error_pass);
            TextView error_pass2 = getView().findViewById(R.id.error_pass2);

            // Check input length name
            if (email.getText().length() == 0) {
                error_user.setVisibility(View.VISIBLE);
            } else {
                error_user.setVisibility(View.GONE);
            }

            // Check input length passwords
            if (pass.getText().length() < 6){
                error_pass.setVisibility(View.VISIBLE);
            } else {
                error_pass.setVisibility(View.GONE);
            }

            // Check match passwords
            /*Toast.makeText(getActivity(), pass.getText().toString() , Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), repeatPass.getText().toString() , Toast.LENGTH_SHORT).show();*/

            // Validate pass words
            if (!(Objects.equals(pass.getText().toString(), repeatPass.getText().toString()))) {
                error_pass2.setVisibility(View.VISIBLE);
            } else {
                error_pass2.setVisibility(View.GONE);
            }

            // Create account
            ((MainActivity)getActivity()).createUser(email.getText().toString(), repeatPass.getText().toString());
        }

    }

}
