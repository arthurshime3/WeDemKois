package com.wedemkois.protecc.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;

import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String, User> users = new HashMap<>();
    ArrayList<Shelter> shelters = new ArrayList<>();
    User currentUser = new User(null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readCSV();

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
        final Spinner userAdminSpinner = findViewById(R.id.userAdminSpinner);
        final EditText firstNameTextEdit = findViewById(R.id.firstNameText);
        final EditText lastNameTextEdit = findViewById(R.id.lastNameText);

        final TextView registerErrorText = findViewById(R.id.registerErrorText);
        final TextView firstNameErrorText = findViewById(R.id.firstNameError);
        final TextView lastNameErrorText = findViewById(R.id.lastNameError);
        final TextView usernameErrorText = findViewById(R.id.usernameError);
        final TextView passwordErrorText = findViewById(R.id.passwordError);

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

                //check validity of inputs
                String usernameValid = usernameTextEdit.getText().toString().replaceAll("\\s+","");
                String passwordValid = passwordTextEdit.getText().toString().replaceAll("\\s+","");
                String firstNameValid = firstNameTextEdit.getText().toString().replaceAll("\\s+","");
                String lastNameValid = lastNameTextEdit.getText().toString().replaceAll("\\s+","");

                boolean usernameIsValid = usernameValid.length() > 0;
                boolean passwordIsValid = passwordValid.length() > 0;
                boolean firstNameIsValid = firstNameValid.length() > 0;
                boolean lastNameIsValid = lastNameValid.length() > 0;
                boolean valid =  usernameIsValid && passwordIsValid && firstNameIsValid && lastNameIsValid;
                if (!valid)
                {
                    if (!usernameIsValid && usernameErrorText.getVisibility() == View.INVISIBLE)
                        usernameErrorText.setVisibility(View.VISIBLE);
                    else if (usernameIsValid && usernameErrorText.getVisibility() == View.VISIBLE)
                        usernameErrorText.setVisibility(View.INVISIBLE);

                    if (!passwordIsValid && passwordErrorText.getVisibility() == View.INVISIBLE)
                        passwordErrorText.setVisibility(View.VISIBLE);
                    else if (passwordIsValid && passwordErrorText.getVisibility() == View.VISIBLE)
                        passwordErrorText.setVisibility(View.INVISIBLE);

                    if (!firstNameIsValid && firstNameErrorText.getVisibility() == View.INVISIBLE)
                        firstNameErrorText.setVisibility(View.VISIBLE);
                    else if (firstNameIsValid && firstNameErrorText.getVisibility() == View.VISIBLE)
                        firstNameErrorText.setVisibility(View.INVISIBLE);

                    if (!lastNameIsValid && lastNameErrorText.getVisibility() == View.INVISIBLE)
                        lastNameErrorText.setVisibility(View.VISIBLE);
                    else if (lastNameIsValid && lastNameErrorText.getVisibility() == View.VISIBLE)
                        lastNameErrorText.setVisibility(View.INVISIBLE);
                }
                //validity confirmed

                if (users.containsKey(username))
                {
                    registerErrorText.setVisibility(v.VISIBLE);
                }
                else if (valid)
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

        displayShelters();

        final Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                welcomeScreen();
            }
        });
    }

    void displayShelters()
    {
        final LinearLayout linearLayout = findViewById(R.id.shelterDisplayLayout);
        TextView[] textViews = new TextView[shelters.size()];
        for (int i = 0; i < shelters.size(); i++)
        {
            textViews[i] = new TextView(this);
            textViews[i].setText(shelters.get(i).getName());
            linearLayout.addView(textViews[i]);

            final int index = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { display(shelters.get(index)); }
            });
        }
    }

    public void display(Shelter shelter)
    {
        setContentView(R.layout.shelter);

        final TextView shelterName = findViewById(R.id.shelterName);
        final TextView shelterAddress = findViewById(R.id.shelterAddress);
        final TextView shelterPhoneNumber = findViewById(R.id.shelterPhoneNumber);
        final TextView shelterGender = findViewById(R.id.shelterGender);
        final TextView shelterCapacity = findViewById(R.id.shelterCapacity);
        final TextView longitude = findViewById(R.id.longitude);
        final TextView latitude = findViewById(R.id.latitude);

        shelterName.setText(shelter.getName());
        shelterAddress.setText(shelter.getAddress());
        shelterPhoneNumber.setText(shelter.getPhoneNumber());
        shelterGender.setText(shelter.getGender());
        shelterCapacity.setText(shelter.getCapacity());
        longitude.setText(shelter.getLongitude());
        latitude.setText(shelter.getLatitude());

        final Button backButton = findViewById(R.id.backToMainButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                mainScreen();
            }
        });
    }

    void readCSV()
    {
        InputStream is = getResources().openRawResource(R.raw.homeless_shelter_database);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line;
        String comma = ",";
        String[] splitLine;

        try
        {
            reader.readLine();
            int counter = 0;
            while (((line = reader.readLine()) != null))
            {
                splitLine = line.split(comma);
                Shelter newShelter = new Shelter(splitLine[1], splitLine[2], splitLine[3],
                        splitLine[5], splitLine[6], splitLine[7] + ", " + splitLine[8]
                        + ", " + splitLine[9], splitLine[11]);
                shelters.add(newShelter);
                db.collection("shelters").document(counter + "").set(newShelter.getName());
                counter++;
            }
        } catch (IOException e)
        {
            Log.d("CSV", "IO Exception");
        } finally
        {
            try
            {
                reader.close();
            } catch (IOException e)
            {
                Log.d("CSV", "IO Exception");
            }
        }

    }

}
