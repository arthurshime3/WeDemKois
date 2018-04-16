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

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShelterDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String shelterId;

    @BindView(R.id.shelterDetailShelterName)
    private TextView shelterNameTextView;

    @BindView(R.id.shelterDetailCapacity)
    private TextView shelterCapacityTextView;

    @BindView(R.id.shelterDetailAgeGroupTextView)
    private TextView shelterAgeGroupTextView;

    @BindView(R.id.shelterDetailGenderTextView)
    private TextView shelterGenderTextView;

    @BindView(R.id.shelterDetailChildrenAllowedTextView)
    private TextView shelterChildrenTextView;

    @BindView(R.id.shelterDetailRequirementsTextView)
    private TextView shelterRequirementsTextView;

    @BindView(R.id.shelterDetailCoordinatesTextView)
    private TextView shelterCoordinatesTextView;

    @BindView(R.id.shelterDetailAddressTextView)
    private TextView shelterAddressTextView;

    @BindView(R.id.shelterDetailNotesTextView)
    private TextView shelterNotesTextView;

    @BindView(R.id.shelterDetailPhoneTextView)
    private TextView shelterPhoneTextView;

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
                Log.d("DashboardActivity", currentShelter.toString());
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
        shelterChildrenTextView.setText(shelter.isChildrenAllowed() ? "Children allowed" : "Children not allowed");
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
        int totalCapacity = Integer.parseInt(currentShelter.getIndividualCapacity()) + (Integer.parseInt(currentShelter.getGroupCapacity()) * 4);
        int bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken()) + (Integer.parseInt(currentShelter.getGroupBedsTaken()) * 4);
        shelterCapacityTextView.setText((totalCapacity - bedsTaken) + " out of " + totalCapacity + " total beds");
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
}

