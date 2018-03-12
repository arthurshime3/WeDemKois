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

        DocumentReference docRef = mDatabase.collection("users").document(uid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
                Log.d("DashboardActivity", currentUser.toString());
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
            Intent newIntent = new Intent(DashboardActivity.this, BaseActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        } else if (i == R.id.shelterViewButton) {
            startActivity(new Intent(DashboardActivity.this, ShelterSearchActivity.class));
        }
    }
}
