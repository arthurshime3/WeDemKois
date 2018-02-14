package com.wedemkois.protecc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, User> users = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        User defaultUser = new User("user", "pass");
        users.put(defaultUser.getUsername(), defaultUser);

        final Button loginButton = findViewById(R.id.loginButton);
        final EditText usernameTextEdit =  (EditText) findViewById(R.id.usernameText);
        final EditText passwordTextEdit =  (EditText) findViewById(R.id.passwordText);
        final TextView loginErrorMessage = (TextView) findViewById(R.id.invalidLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                if (users.containsKey(username) && users.get(username).getPassword().equals(password))
                {
                    Log.d("login button:", "valid username and password");
                    if (loginErrorMessage.getVisibility() == v.VISIBLE)
                        loginErrorMessage.setVisibility(v.INVISIBLE);
                }
                else
                {
                    loginErrorMessage.setVisibility(v.VISIBLE);
                }
            }
        });
    }


}
