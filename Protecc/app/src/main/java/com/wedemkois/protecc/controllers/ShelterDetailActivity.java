package com.wedemkois.protecc.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShelterDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String shelterId;

    @BindView(R.id.shelterDetailShelterName)
    TextView shelterNameTextView;

    @BindView(R.id.shelterDetailCapacity)
    TextView shelterCapacityTextView;

    @BindView(R.id.shelterDetailAgeGroupTextView)
    TextView shelterAgeGroupTextView;

    @BindView(R.id.shelterDetailGenderTextView)
    TextView shelterGenderTextView;

    @BindView(R.id.shelterDetailChildrenAllowedTextView)
    TextView shelterChildrenTextView;

    @BindView(R.id.shelterDetailRequirementsTextView)
    TextView shelterRequirementsTextView;

    @BindView(R.id.shelterDetailCoordinatesTextView)
    TextView shelterCoordinatesTextView;

    @BindView(R.id.shelterDetailAddressTextView)
    TextView shelterAddressTextView;

    @BindView(R.id.shelterDetailNotesTextView)
    TextView shelterNotesTextView;

    @BindView(R.id.shelterDetailPhoneTextView)
    TextView shelterPhoneTextView;

<<<<<<< HEAD
    @BindView(R.id.shelterDetailGroupTextView)
    TextView shelterGroupTextView;

    @BindView(R.id.shelterDetailBedsTakenTextView)
    TextView shelterBedsTakenTextView;

    @BindView(R.id.shelterDetailGroupsTakenTextView)
    TextView shelterGroupsTakenView;

    private FirebaseFirestore mDatabase;
    private DocumentReference mShelterRef;
=======
>>>>>>> master
    private Shelter currentShelter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_detail);
        ButterKnife.bind(this);

        shelterId = getIntent().getStringExtra("shelter_id");

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        DocumentReference mShelterRef = mDatabase.collection("shelters").document(shelterId);
        mShelterRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentShelter = documentSnapshot.toObject(Shelter.class);
                Log.d("DashboardActivity", Objects.requireNonNull(currentShelter).toString());
                onShelterLoaded(currentShelter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DashboardActivity", e.toString());
            }
        });

        findViewById(R.id.shelterDetailClaimBedsButton).setOnClickListener(this);
    }

    @SuppressWarnings("FeatureEnvy")
    private void onShelterLoaded(Shelter shelter) {
        currentShelter = shelter;

        shelterNameTextView.setText(shelter.getName());
        updateCapacityTextView();
        shelterAgeGroupTextView.setText(shelter.getAgeRange());
        shelterGenderTextView.setText(shelter.getGender());
        shelterChildrenTextView.setText(
                shelter.isChildrenAllowed() ? "Children allowed" : "Children not allowed");
        shelterRequirementsTextView.setText(shelter.getRequirements());
        shelterCoordinatesTextView.setText(shelter.getCoordinates().toString());
        shelterAddressTextView.setText(shelter.getAddress());
        shelterNotesTextView.setText(String.join(",", shelter.getNotes()));
        shelterPhoneTextView.setText(shelter.getPhoneNumber());
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("FeatureEnvy")
    private void updateCapacityTextView()
    {
        int totalCapacity = Integer.parseInt(currentShelter.getIndividualCapacity())
                + (Integer.parseInt(currentShelter.getGroupCapacity()) * 4);
        int bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken())
                + (Integer.parseInt(currentShelter.getGroupBedsTaken()) * 4);
        shelterCapacityTextView.setText(
                (totalCapacity - bedsTaken) + " out of " + totalCapacity + " total beds");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.shelterDetailClaimBedsButton) {
            Intent newIntent = new Intent(ShelterDetailActivity.this, ShelterClaimActivity.class);
            newIntent.putExtra("shelter_id", shelterId);
            startActivity(newIntent);
        }
    }
<<<<<<< HEAD

    public boolean checkQualifications(String[] ageGroup, String[] gender, boolean childrenAllowed) {
        if (!(currentShelter.getAgeRange().equals("All"))) {
            for (int i = 0; i < ageGroup.length; i++) {
                if (!(ageGroup[i].equals(currentShelter.getAgeRange()))) {
                    return false;
                }
            }
        }
        if (!(currentShelter.getGender().equals("Both"))) {
            for (int i = 0; i < gender.length; i++) {
                if (!(gender[i].equals(currentShelter.getGender()))) {
                    return false;
                }
            }
        }
        if (!(childrenAllowed == currentShelter.isChildrenAllowed())) return false;
        return true;
    }
    /*
    * Method that updates number of vacant beds at the shelter if possible.
    * @param users can be positive (checking in) or negative (checking out)
    * @return true if check-in/out was valid, false if not
    */

    public boolean updateVacancy(int users) {
        if (users == 0) {
            return true;
        } else if (users == 1 || users == -1) {
            int unavailable = Integer.parseInt(shelterBedsTakenTextView.getText());
            if (unavailable == 0 && users == -1) {
               return false;
            }
            int vacancies = Integer.parseInt(shelterCapacityTextView.getText()) - unavailable;
            if (vacancies >= users) {
                shelterBedsTakenTextView.setText(unavailable + users);
                return true;
            }
        } else {
            int unavailable = Integer.parseInt(shelterGroupsTakenTextView.getText());
            if (unavailable == 0 && users < 0) {
                return false;
            }
            int vacancies = Integer.parseInt(shelterGroupTextView.getText()) - unavailable;
            if (vacancies >= users) {
                shelterGroupsTakenTextView.setText(unavailable + users);
                return true;
            }
        }
        return false;
    }

=======
>>>>>>> master
}

