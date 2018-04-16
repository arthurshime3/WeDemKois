package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView nameView;
    private TextView userTypeView;

    private User currentUser;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        nameView = findViewById(R.id.nameText);
        userTypeView = findViewById(R.id.userTypeText);

        findViewById(R.id.logoutButton).setOnClickListener(this);
        findViewById(R.id.shelterViewButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        String uid = mAuth.getUid();

<<<<<<< HEAD
        DocumentReference docRef = mDatabase.collection("users").document(uid);

=======
        DocumentReference docRef = mDatabase.collection("users")
                .document(Objects.requireNonNull(uid));
>>>>>>> master
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
<<<<<<< HEAD
                Log.d("DashboardActivity", currentUser.toString());
=======
                Log.d("DashboardActivity", Objects.requireNonNull(currentUser).toString());
                retrieveCurrentShelter();
>>>>>>> master
                updateUI(currentUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DashboardActivity", e.toString());
            }
        });

        addSheltersToDatabase();

    }

    private void addSheltersToDatabase () {
        CSVReader reader = null;
        try {
<<<<<<< HEAD
            reader = new CSVReader(
                    new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.shelterdb))),
                    ',',
                    '"',
                    1);
            String[] splitLine;
            int counter = 0;
            while ((splitLine = reader.readNext()) != null) {
                Shelter newShelter = new Shelter(splitLine[1], splitLine[2], splitLine[3], splitLine[4],
                        splitLine[5], splitLine[6], splitLine[7], splitLine[8], splitLine[9], splitLine[10], splitLine[11]);
=======
            reader = new CSVReaderBuilder(
                    new InputStreamReader(getResources().openRawResource(R.raw.shelterdb)))
                    .withCSVParser(new CSVParserBuilder()
                            .withQuoteChar('"')
                            .withSeparator(',')
                            .build())
                    .withSkipLines(1)
                    .build();
            String[] splitLine = reader.readNext();
            int counter = 0;
            while (splitLine != null) {
                Shelter newShelter = new Shelter(splitLine[1], splitLine[2], splitLine[3],
                        splitLine[4], splitLine[5], splitLine[6], splitLine[7], splitLine[8],
                        splitLine[9], splitLine[10], splitLine[11], splitLine[12], splitLine[13],
                        splitLine[14]);
>>>>>>> master
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
<<<<<<< HEAD
                reader.close();
            } catch (IOException e) {

=======
                Objects.requireNonNull(reader).close();
            } catch (IOException ignored) {
                Log.d("addSheltersToDatabase", ignored.toString());
>>>>>>> master
            }
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
<<<<<<< HEAD
        if (i == R.id.logoutButton) {
            mAuth.signOut();
            Intent newIntent = new Intent(DashboardActivity.this, BaseActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        } else if (i == R.id.shelterViewButton) {
            startActivity(new Intent(DashboardActivity.this, ShelterSearchActivity.class));
        }
    }
}
=======
        switch (i) {
            case R.id.logoutButton:
                mAuth.signOut();
                Intent newIntent = new Intent(DashboardActivity.this, WelcomeActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                break;
            case R.id.shelterViewButton:
                startActivity(new Intent(DashboardActivity.this, ShelterSearchActivity.class));
                break;
            case R.id.shelterCheckOutButton:
                if (!"".equals(currentUser.getShelterId())) {
                    currentShelter.removeOccupant(currentUser.getUsername(),
                            currentUser.getOccupantType());
                    pushShelterUpdates();
                    currentUser.setShelterId("");
                    pushUserUpdates();
                    updateUI(currentUser);
                    updateShelterUI(null);
                }
                break;
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
    @SuppressLint("RestrictedApi")
    private void pushUserUpdates() {
        mDatabase.collection("users")
                .document(Objects.requireNonNull(mAuth.getUid())).set(currentUser)
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
>>>>>>> master
