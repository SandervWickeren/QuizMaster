package com.sandervwickeren.quizmaster;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Loginfragment fragment = new Loginfragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, "categories");
            ft.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void createUser(String email, String password) {
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

                            // Start next activity
                        } else {

                            // Check failure
                            try {
                                throw task.getException();

                                // Email already in use.
                            }catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(MainActivity.this,
                                        "The entered email is already used. Please use another email",
                                        Toast.LENGTH_SHORT).show();

                                // Network problems
                            }catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this,
                                        "Can't make a connection to the server.",
                                        Toast.LENGTH_SHORT).show();

                                // Other error
                            }catch (Exception e) {
                                Toast.makeText(MainActivity.this,
                                        "An error occured, please try it again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void logIn(View view, String email, String password){
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

                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI(user);
                        } else {
                            // Check failure
                            try {
                                throw task.getException();

                                // Network problems
                            }catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this,
                                        "Can't make a connection to the server.",
                                        Toast.LENGTH_SHORT).show();

                                // Other error
                            }catch (Exception e) {
                                Toast.makeText(MainActivity.this,
                                        "Please enter valid credentials.",
                                        Toast.LENGTH_SHORT).show();
                            }





                            // If sign in fails, display a message to the user.
                            Log.w("email", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
