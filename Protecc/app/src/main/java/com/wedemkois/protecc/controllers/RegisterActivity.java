package com.wedemkois.protecc.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wedemkois.protecc.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private ProgressBar progressBar;
    private View registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        mEmailField = findViewById(R.id.usernameText);
        mPasswordField = findViewById(R.id.passwordText);
        mFirstNameField = findViewById(R.id.firstNameText);
        mLastNameField = findViewById(R.id.lastNameText);
        progressBar = findViewById(R.id.progressBar);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
    }

    private void register(String email, String password, final String firstname, final String lastname) {
        if(!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToDatabase(firstname, lastname, user.getUid());
                            Intent i = new Intent(RegisterActivity.this, DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressBar();
                    }
                });



    }

    private void addUserToDatabase(String first, String last, String uid) {
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", first);
        user.put("lastName", last);
        mDatabase.collection("users").document(uid).set(user);
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

        String firstName = mFirstNameField.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameField.setError("Required.");
            valid = false;
        } else {
            mFirstNameField.setError(null);
        }

        String lastName = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastNameField.setError("Required.");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.registerButton) {
            // Hide the keyboard
            View currentFocus = getWindow().getDecorView().getRootView();
            if (currentFocus != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            //
            register(mEmailField.getText().toString(),
                    mPasswordField.getText().toString(),
                    mFirstNameField.getText().toString(),
                    mLastNameField.getText().toString());
        }
    }

    public void showProgressBar() {
        registerButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            registerButton.setVisibility(View.VISIBLE);
        }
    }
}
