package com.wedemkois.protecc.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wedemkois.protecc.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private View loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mEmailField = findViewById(R.id.usernameText);
        mPasswordField = findViewById(R.id.passwordText);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginSubmitButton);

        loginButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressBar();


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(LoginActivity.this,
                                    DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }
                        // [END_EXCLUDE]
                        hideProgressBar();
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void showProgressBar() {
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.loginSubmitButton) {
            // Hide the keyboard
            View currentFocus = getWindow().getDecorView().getRootView();
            if (currentFocus != null) {
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm)
                        .hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            //
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
