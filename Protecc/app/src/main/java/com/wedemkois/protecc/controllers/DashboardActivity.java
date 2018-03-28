package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencsv.CSVReader;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView nameView;
    private TextView userTypeView;
    private TextView currentShelterHeaderView;
    private TextView currentShelterView;
    private Button checkOutButton;

    private User currentUser;
    private Shelter currentShelter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        nameView = findViewById(R.id.nameText);
        userTypeView = findViewById(R.id.userTypeText);
        currentShelterHeaderView = findViewById(R.id.currentShelterHeader);
        currentShelterView = findViewById(R.id.currentShelter);
        checkOutButton = findViewById(R.id.shelterCheckOutButton);

        findViewById(R.id.logoutButton).setOnClickListener(this);
        findViewById(R.id.shelterViewButton).setOnClickListener(this);
        checkOutButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

//        addSheltersToDatabase();

        String uid = mAuth.getUid();

        DocumentReference docRef = mDatabase.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DashboardActivity", documentSnapshot.toString());
                currentUser = documentSnapshot.toObject(User.class);
                Log.d("DashboardActivity", currentUser.toString());
                retrieveCurrentShelter();
                updateUI(currentUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DashboardActivity", e.toString());
            }
        });



    }

    private void addSheltersToDatabase () {
        CSVReader reader = null;
        try {
            reader = new CSVReader(
                    new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.shelterdb))),
                    ',',
                    '"',
                    1);
            String[] splitLine;
            int counter = 0;
            while ((splitLine = reader.readNext()) != null) {
                Shelter newShelter = new Shelter(splitLine[1], splitLine[2], splitLine[3], splitLine[4],
                        splitLine[5], splitLine[6], splitLine[7], splitLine[8], splitLine[9], splitLine[10],
                        splitLine[11], splitLine[12], splitLine[13], splitLine[14]);
                Log.d("addSheltersToDatabase","Hi");
                mDatabase.collection("shelters").document(counter + "").set(newShelter);
                counter++;
            }

        } catch (FileNotFoundException e) {
            Log.d("addSheltersToDatabase", "FileNotFound!");
        } catch (IOException e) {
            Log.d("addSheltersToDatabase", "IOException!");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }

    }

    private void retrieveCurrentShelter() {
        if (!currentUser.getShelterId().equals("")) {
            mDatabase.collection("shelters").document(currentUser.getShelterId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    currentShelter = documentSnapshot.toObject(Shelter.class);
                    updateShelterUI(currentShelter);
                    Log.d("DashboardActivity", currentShelter.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("DashboardActivity", e.toString());
                }
            });
        } else {
            updateShelterUI(null);
        }
    }


    private void updateShelterUI(Shelter shelter) {
        if (shelter != null)
        {
            currentShelterView.setText(shelter.getName());
            currentShelterView.setVisibility(View.VISIBLE);
            checkOutButton.setVisibility(View.VISIBLE);
            currentShelterHeaderView.setVisibility(View.VISIBLE);
        }
        else
        {
            currentShelterView.setVisibility(View.INVISIBLE);
            checkOutButton.setVisibility(View.INVISIBLE);
            currentShelterHeaderView.setVisibility(View.INVISIBLE);
        }
    }
    private void updateUI(User user) {
        if(user==null) {
            return;
        }

        nameView.setText(user.getName());
        userTypeView.setText(user.getUserType().toString());
//        displayShelters();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.logoutButton) {
            mAuth.signOut();
            Intent newIntent = new Intent(DashboardActivity.this, WelcomeActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        } else if (i == R.id.shelterViewButton) {
            startActivity(new Intent(DashboardActivity.this, ShelterSearchActivity.class));
        } else if (i == R.id.shelterCheckOutButton)
        {
            if(currentUser.getShelterId()!="") {
                currentShelter.removeOccupant(currentUser.getUsername(), currentUser.getOccupantType());
                pushShelterUpdates();
                currentUser.setShelterId("");
                pushUserUpdates();
                updateUI(currentUser);
                updateShelterUI(null);
            }
        }
    }

    private void pushShelterUpdates () {
        mDatabase.collection("shelters")
                .document(currentUser.getShelterId())
                .set(currentShelter)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("pushUpdates", "shelter written successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("pushUpdates", e.toString());
                    }
                });
    }
    private void pushUserUpdates() {
        mDatabase.collection("users").document(mAuth.getUid()).set(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("pushUpdates", "user written successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("pushUpdates", e.toString());
                    }
                });
    }
}
