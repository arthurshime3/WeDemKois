package com.wedemkois.protecc.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;

import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.User;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, User> users = new HashMap<>();
    User currentUser = new User(null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        welcomeScreen();
    }

    void welcomeScreen()
    {
        setContentView(R.layout.welcomescreen);
        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginScreen();
            }
        });

        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerScreen();
            }
        });

    }

    void loginScreen()
    {
        setContentView(R.layout.login);

        final Button loginSubmitButton = findViewById(R.id.loginSubmitButton);
        final EditText usernameTextEdit =  findViewById(R.id.usernameText);
        final EditText passwordTextEdit =  findViewById(R.id.passwordText);
        final TextView loginErrorMessage = findViewById(R.id.loginErrorText);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                if (users.containsKey(username) && users.get(username).getPassword().equals(password))
                {
                    if (loginErrorMessage.getVisibility() == v.VISIBLE)
                        loginErrorMessage.setVisibility(v.INVISIBLE);
                    currentUser = users.get(username);
                    mainScreen();
                }
                else
                {
                    loginErrorMessage.setVisibility(v.VISIBLE);
                }
            }
        });

        final Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                welcomeScreen();
            }
        });

    }

    void registerScreen()
    {
        setContentView(R.layout.register);

        final Button registerSubmitButton = findViewById(R.id.registerSubmitButton);
        final EditText usernameTextEdit =  findViewById(R.id.usernameText);
        final EditText passwordTextEdit =  findViewById(R.id.passwordText);
        final TextView registerErrorText = findViewById(R.id.registerErrorText);
        final Spinner userAdminSpinner = findViewById(R.id.userAdminSpinner);
        final EditText firstNameTextEdit = findViewById(R.id.firstNameText);
        final EditText lastNameTextEdit = findViewById(R.id.lastNameText);


        ArrayAdapter<User.UserType> userAdminAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, User.UserType.values());
        userAdminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userAdminSpinner.setAdapter(userAdminAdapter);

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                User.UserType userType = (User.UserType)userAdminSpinner.getSelectedItem();
                String firstName = firstNameTextEdit.getText().toString();
                String lastName = lastNameTextEdit.getText().toString();

                if (users.containsKey(username))
                {
                    registerErrorText.setVisibility(v.VISIBLE);
                }
                else
                {
                    if (registerErrorText.getVisibility() == v.VISIBLE)
                        registerErrorText.setVisibility(v.INVISIBLE);
                    users.put(username, new User(username, password, userType, firstName, lastName));
                    currentUser = users.get(username);
                    mainScreen();
                }
            }
        });

        final Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                welcomeScreen();
            }
        });
    }

    void mainScreen()
    {
        setContentView(R.layout.activity_main);
        final TextView nameText = findViewById(R.id.nameText);
        final TextView userTypeText = findViewById(R.id.userTypeText);

        nameText.setText(currentUser.getName());
        userTypeText.setText(currentUser.getUserType().toString());
        final Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                welcomeScreen();
            }
        });
    }

}
