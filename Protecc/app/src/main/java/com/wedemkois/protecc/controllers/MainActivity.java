package com.wedemkois.protecc.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.User;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, User> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);

        User defaultUser = new User("user", "pass");
        users.put(defaultUser.getUsername(), defaultUser);

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.login);
                loginScreen();
            }
        });

        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.register);
                registerScreen();
            }
        });


    }

    void loginScreen()
    {
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
                    setContentView(R.layout.activity_main);
                }
                else
                {
                    loginErrorMessage.setVisibility(v.VISIBLE);
                }
            }
        });
    }

    void registerScreen()
    {
        final Button registerSubmitButton = findViewById(R.id.registerSubmitButton);
        final EditText usernameTextEdit =  findViewById(R.id.usernameText);
        final EditText passwordTextEdit =  findViewById(R.id.passwordText);
        final TextView registerErrorText = findViewById(R.id.registerErrorText);

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                if (users.containsKey(username))
                {
                    registerErrorText.setVisibility(v.VISIBLE);
                }
                else
                {
                    if (registerErrorText.getVisibility() == v.VISIBLE)
                        registerErrorText.setVisibility(v.INVISIBLE);
                    users.put(username, new User(username, password));
                    setContentView(R.layout.activity_main);
                }
            }
        });
    }

}
