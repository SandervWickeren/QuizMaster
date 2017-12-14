package com.sandervwickeren.quizmaster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Objects;


/***********************************************************************
 Fragment that handles the registration of users by using functions from
 the Mainactivity. It checks the input from the user to prevent errors.

 ***********************************************************************/
public class Registerfragment extends Fragment {

    // Global error report:
    Integer noErrors = 1;

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
            EditText username = getView().findViewById(R.id.username);
            EditText email = getView().findViewById(R.id.email);
            EditText pass = getView().findViewById(R.id.password);
            EditText repeatPass = getView().findViewById(R.id.password2);

            // Error report
            TextView error_username = getView().findViewById(R.id.error_username);
            TextView error_email = getView().findViewById(R.id.error_email);
            TextView error_pass = getView().findViewById(R.id.error_pass);
            TextView error_pass2 = getView().findViewById(R.id.error_pass2);

            // Reset global error
            noErrors = 1;

            // Check input length username
            if (username.getText().length() == 0) {
                error_username.setVisibility(View.VISIBLE);
                noErrors = 0;
            } else {
                error_username.setVisibility(View.GONE);
            }

            // Check input length email
            if (email.getText().length() < 5) {
                error_email.setVisibility(View.VISIBLE);
                noErrors = 0;
            } else {
                error_email.setVisibility(View.GONE);
            }

            // Check input length passwords
            if (pass.getText().length() < 6) {
                error_pass.setVisibility(View.VISIBLE);
                noErrors = 0;
            } else {
                error_pass.setVisibility(View.GONE);
            }

            // Validate passwords
            if (!(Objects.equals(pass.getText().toString(), repeatPass.getText().toString()))) {
                error_pass2.setVisibility(View.VISIBLE);
                noErrors = 0;
            } else {
                error_pass2.setVisibility(View.GONE);
            }

            // Create account if no errors
            if (noErrors == 1) {
                ((MainActivity)getActivity()).createUser(email.getText().toString(),
                        repeatPass.getText().toString(), username.getText().toString());
                ((MainActivity)getActivity()).hideKeyboard();
            }
        }

    }

}
