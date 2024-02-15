package ua.ukd.dummy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends Activity {


    // Initialize Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_in);
        // Check current authentication state
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            String uid = currentUser.getUid();
            // Perform actions for authenticated user
        } else {
            // User is not signed in
            // Redirect to sign-in page or handle authentication flow

            View btnSignIn = findViewById(R.id.btnSignIn);
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAuthState();
                }
            });

            View btnSignUp = findViewById(R.id.btnSignUp);
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                    finish();
                }
            });
        }
    }


    private void checkAuthState() {
        // Check current authentication state
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            String uid = currentUser.getUid();
            // Perform actions for authenticated user
        } else {
            // User is not signed in
            // Redirect to sign-in page or handle authentication flow
        }
        EditText edLogin = findViewById(R.id.tifEmail);
        EditText adPass = findViewById(R.id.tifPassword);

        // Example: Sign up a new user with email and password
        mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), adPass.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser newUser = mAuth.getCurrentUser();
                        // Perform additional actions for the new user
                    } else {
                        // User registration failed
                        // Handle the error (e.g., display an error message)
                    }
                });

    }
}