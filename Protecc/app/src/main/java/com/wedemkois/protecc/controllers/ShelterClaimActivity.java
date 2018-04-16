package com.wedemkois.protecc.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelterClaimActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.sc_shelterName)
    private TextView shelterNameTextView;

    @BindView(R.id.sc_claimBedsButton)
    private Button claimBedsButton;

    @BindView(R.id.sc_numOfPeople)
    private TextView numOfUsers;

    @BindView(R.id.sc_maleCheckBox)
    private CheckBox maleCheckBox;

    @BindView(R.id.sc_femaleCheckBox)
    private CheckBox femaleCheckBox;

    @BindView(R.id.sc_nonBinaryCheckBox)
    private CheckBox nonBinaryCheckBox;

    @BindView(R.id.sc_childrenCheckBox)
    private CheckBox childrenCheckBox;

    @BindView(R.id.sc_youngAdultCheckBox)
    private CheckBox youngAdultCheckBox;

    @BindView(R.id.sc_adultCheckBox)
    private CheckBox adultCheckBox;

    @BindView(R.id.sc_inputError)
    private TextView inputErrorMessage;

    @BindView(R.id.sc_userError)
    private TextView userErrorMessage;

    private Shelter currentShelter;
    private User currentUser;
    private String shelterId;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_claim);
        ButterKnife.bind(this);

        claimBedsButton.setOnClickListener(this);

        shelterId = getIntent().getStringExtra("shelter_id");

        mDatabase = FirebaseFirestore.getInstance();
        DocumentReference mShelterRef = mDatabase.collection("shelters").document(shelterId);
        mShelterRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentShelter = documentSnapshot.toObject(Shelter.class);
                Log.d("DashboardActivity", currentShelter.toString());
                onShelterLoaded(currentShelter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DashboardActivity", e.toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        @SuppressLint("RestrictedApi") String uid = mAuth.getUid();

        DocumentReference docRef = mDatabase.collection("users").document(uid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DashboardActivity", documentSnapshot.toString());
                currentUser = documentSnapshot.toObject(User.class);
                Log.d("DashboardActivity", currentUser.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DashboardActivity", e.toString());
            }
        });
    }

    private void onShelterLoaded(Shelter shelter)
    {
        currentShelter = shelter;
        shelterNameTextView.setText(currentShelter.getName());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sc_claimBedsButton) {
            if (!"".equals(currentUser.getShelterId()))   // user is already checked into a shelter
            {
                userErrorMessage.setVisibility(View.VISIBLE);
            }
            else if (checkIn())
            {
                // beds successfully claimed
                if (inputErrorMessage.getVisibility() == View.VISIBLE) {
                    inputErrorMessage.setVisibility(View.INVISIBLE);
                }
                if (userErrorMessage.getVisibility() == View.VISIBLE) {
                    userErrorMessage.setVisibility(View.INVISIBLE);
                }

                pushUpdates();
                Intent newIntent = new Intent(ShelterClaimActivity.this, DashboardActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra("shelter_id", shelterId);
                startActivity(newIntent);
            }
            else
            {
//                Toast toast = Toast.makeText(getApplicationContext(), "checkInput() failed", Toast.LENGTH_SHORT);
//                toast.show();
                //display warning
                inputErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private void pushUpdates() {
        mDatabase.collection("shelters").document(shelterId).set(currentShelter)
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
    @SuppressWarnings("FeatureEnvy")
    private boolean checkIn()
    {
        int agesCheckedCount = getAgesCheckedCount();
        int gendersCheckedCount = getGendersCheckedCount();

        if ((gendersCheckedCount == 0) || (agesCheckedCount == 0) || numOfUsers.getText().toString().trim().isEmpty()) {
            return false;
        }

        if (nonBinaryCheckBox.isChecked()) {
            gendersCheckedCount--;
        }
        String[] genders = getGenders(gendersCheckedCount);

        boolean children = false;
        if (childrenCheckBox.isChecked())
        {
            agesCheckedCount--;
            children = true;
        }
        String[] ages = getAges(agesCheckedCount);


        int numOfPeople = Integer.parseInt(numOfUsers.getText().toString());
        Log.d("checkInput", "We got past checkInput");
        boolean[] vacancyData = currentShelter.updateVacancy(numOfPeople, numOfPeople > 1);

        if (currentShelter.checkQualifications(ages, genders, children) && vacancyData[0])
        {
            Log.d("Check In", "Adding shelter " + currentShelter.getName() + " to user");
            currentUser.setShelterId(shelterId);
            Log.d("Check In", currentUser.getShelterId() + " has been added");
            if (vacancyData[1]) //group
            {
                currentUser.setOccupantType(User.OccupantType.GROUP);
            } else {
                currentUser.setOccupantType(User.OccupantType.INDIVIDUAL);
            }

            currentShelter.addOccupant(currentUser.getUsername(), numOfPeople);

            return true;
        }
        return false;
    }

    private int getGendersCheckedCount()
    {
        int gendersCheckedCount = 0;
        if (maleCheckBox.isChecked()) {
            gendersCheckedCount++;
        }
        if (femaleCheckBox.isChecked()) {
            gendersCheckedCount++;
        }
        if (nonBinaryCheckBox.isChecked()) {
            gendersCheckedCount++;
        }
        return gendersCheckedCount;
    }

    private int getAgesCheckedCount()
    {
        int agesCheckedCount = 0;
        if (childrenCheckBox.isChecked()) {
            agesCheckedCount++;
        }
        if (youngAdultCheckBox.isChecked()) {
            agesCheckedCount++;
        }
        if (adultCheckBox.isChecked()) {
            agesCheckedCount++;
        }
        return agesCheckedCount;
    }

    private String[] getGenders(int gendersCheckedCount)
    {
        String[] genders = new String[gendersCheckedCount];

        int i = 0;
        if (maleCheckBox.isChecked())
        {
            genders[i] = "MEN";
//            i++;
        }
        if (femaleCheckBox.isChecked())
        {
            genders[i] = "WOMEN";
//            i++;
        }

        return  genders;
    }

    private String[] getAges(int agesCheckedCount)
    {
        String[] ages = new String[agesCheckedCount];

        int i = 0;
        if (youngAdultCheckBox.isChecked())
        {
            ages[i] = "YOUNGADULTS";
//            i++;
        }
        if (adultCheckBox.isChecked())
        {
            ages[i] = "ADULT";
//            i++;
        }

        return ages;
    }
}
