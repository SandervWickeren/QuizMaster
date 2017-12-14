package com.sandervwickeren.quizmaster;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/***********************************************************************
 Fragment that handles the logins.

 ***********************************************************************/
public class Loginfragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loginfragment, container, false);

        Button login = v.findViewById(R.id.login_button);
        Button register = v.findViewById(R.id.register_button);

        register.setOnClickListener(new Register_click());
        login.setOnClickListener(new Login_click());

        return v;
    }

    private class Register_click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Registerfragment fragment = new Registerfragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            ft.addToBackStack(null);
            ft.replace(R.id.fragment_container, fragment, "categories");
            ft.commit();
        }
    }

    private class Login_click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // Get input
            EditText email = getView().findViewById(R.id.email);
            EditText pass = getView().findViewById(R.id.password);

            // Get errortext
            TextView errorText = getView().findViewById(R.id.errorText);

            if (email.getText().length() == 0 || pass.getText().length() == 0) {
                errorText.setVisibility(View.VISIBLE);
            } else {
                errorText.setVisibility(View.GONE);
                ((MainActivity)getActivity()).logIn(view, email.getText().toString(), pass.getText().toString());
                ((MainActivity)getActivity()).hideKeyboard();
                }
            }

        }
    }


