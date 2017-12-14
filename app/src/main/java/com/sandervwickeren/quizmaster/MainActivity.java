package com.sandervwickeren.quizmaster;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/***********************************************************************
 Startup activity, show a bottomnavigationbar with profile, play and
 highscores. It displays the fragments in the container and contains
 functions that are important for all fragments to prevent code
 duplication.

 ***********************************************************************/
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Actionbar design
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //Toolbar toolbar = findViewById(R.id.homeToolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Quiz Master");
        //getSupportActionBar().setIcon(getDrawable(R.drawable.quiz_master));


        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        // Set onclicklistener
        bottomNavigationView.setOnNavigationItemSelectedListener(new navigationClicks());


        // Launch the middle fragment
        Homefragment fragment = new Homefragment();
        replaceFragment(fragment);



        // Set standard selected item, to "play".
        //bottomNavigationView.setSelectedItemId(R.id.navigation_play);

        // Set backstack listener
        getSupportFragmentManager().addOnBackStackChangedListener(new backstackListener());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        /*if (currentUser != null) {
            Toast.makeText(MainActivity.this,
                    "Logged in", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,
                    "Not Logged in", Toast.LENGTH_SHORT).show();
        }*/
        //updateUI(currentUser);
    }

    public void replaceFragment (Fragment fragment) {
        // Retrieved from:
        // https://stackoverflow.com/questions/18305945/how-to-resume-fragment-from-backstack-if-exists
        // Checks if fragment already active before making a new instance of the fragment.
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_out, R.anim.fade_in);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void createUser(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Created user", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this,
                                    "Account succesfully created!", Toast.LENGTH_SHORT).show();

                            // Add name to user
                            User newUser = new User(username, email);
                            String userID = "as";
                            mDatabase.child("users").child(user.getUid()).setValue(newUser);

                            // Navigate back to start
                            Homefragment fragment = new Homefragment();
                            replaceFragment(fragment);
                        } else {

                            // Check failure
                            try {
                                throw task.getException();

                                // Email already in use.
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(MainActivity.this,
                                        "The entered email is already used. Please use another email",
                                        Toast.LENGTH_SHORT).show();

                                // Network problems
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this,
                                        "Can't make a connection to the server.",
                                        Toast.LENGTH_SHORT).show();

                                // Other error
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this,
                                        "An error occured, please try it again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void logIn(View view, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign in", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this,
                                    "Succesfully logged in",
                                    Toast.LENGTH_SHORT).show();

                            // Get current user
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Launch profile fragment
                            Profilefragment fragment = new Profilefragment();
                            replaceFragment(fragment);
                            //updateUI(user);
                        } else {
                            // Check failure and give feedback
                            try {
                                throw task.getException();

                                // Network problems
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this,
                                        "Can't make a connection to the server.",
                                        Toast.LENGTH_SHORT).show();

                                // Other error
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this,
                                        "Please enter valid credentials.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // Log Error
                            Log.w("email", "signInWithEmail:failure", task.getException());

                        }

                    }
                });
    }

    public void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }




    private void updateNavigation (Fragment fragment) {
        // Get fragment clas name
        String name = fragment.getClass().getName();

        // Get view
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        // Remove Listener to prevent infinite looping.
        bottomNavigationView.setOnNavigationItemSelectedListener(null);

        // Change selected item based on current fragment
        if (Objects.equals(name, Homefragment.class.getName())) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_play);
        }
        else if (Objects.equals(name, Highscoresfragment.class.getName())) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_highscore);
        }
        // All other fragments are from the profile page
        else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        }

        // Set the listener back
        bottomNavigationView.setOnNavigationItemSelectedListener(new navigationClicks());
    }

    private class backstackListener implements FragmentManager.OnBackStackChangedListener {
        @Override
        public void onBackStackChanged() {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null){
                updateNavigation(fragment);
            }

            // If backstack is empty it should close the app
            int backstackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backstackCount == 0) {
                MainActivity.this.finish();
            }

        }
    }

    private class navigationClicks implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Get id
            int id = item.getItemId();

            // Get current selected id
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
            int selected_id = bottomNavigationView.getSelectedItemId();

            // Check if already selected
            if (!(Objects.equals(id, selected_id))) {

                //ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_out, R.anim.fade_in);


                // Launch correct fragment
                if (id == R.id.navigation_play) {
                    Homefragment fragment = new Homefragment();
                    replaceFragment(fragment);

                } else if (id == R.id.navigation_highscore) {
                    Highscoresfragment fragment = new Highscoresfragment();
                    replaceFragment(fragment);

                } else if (id == R.id.navigation_profile) {
                    if (mAuth.getCurrentUser() == null) {
                        Loginfragment fragment = new Loginfragment();
                        replaceFragment(fragment);
                    } else {
                        Profilefragment fragment = new Profilefragment();
                        replaceFragment(fragment);
                    }

                }
            }
            // Visually show the selected item;
            return true;
        }
    }
}
