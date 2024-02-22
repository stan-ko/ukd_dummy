package ua.ukd.dummy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends Activity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);

        final View btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });


        final View btGoogleSignIn = findViewById(R.id.btnGoogle);
        btGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, R.string.not_implemented, Toast.LENGTH_LONG).show();
            }
        });

        final View btnAnon = findViewById(R.id.btnAnon);
        btnAnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void signUp() {
        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Example: Register a new user with email and password
        EditText edLogin = findViewById(R.id.tifEmail);
        EditText adPass = findViewById(R.id.tifPassword);

        String email = edLogin.getText().toString();
        String password = adPass.getText().toString();

        // fixme: validate email and pass!
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            Log.d("RegistrationActivity", "User registration successful");
                            // You can redirect to the main activity or perform other actions
                            // for the newly registered user.
                        } else {
                            // User registration failed
                            Log.w("RegistrationActivity", "User registration failed", task.getException());
                            // Handle the error (e.g., display an error message)
                            Toast.makeText(SignUpActivity.this, "Registration failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}