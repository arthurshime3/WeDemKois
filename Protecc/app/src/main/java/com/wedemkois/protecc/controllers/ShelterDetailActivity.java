package com.wedemkois.protecc.controllers;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import org.w3c.dom.Text;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShelterDetailActivity extends AppCompatActivity {
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

    private FirebaseFirestore mDatabase;
    private DocumentReference mShelterRef;
    private Shelter currentShelter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_detail);
        ButterKnife.bind(this);

        shelterId = getIntent().getStringExtra("shelter_id");

        mDatabase = FirebaseFirestore.getInstance();
        mShelterRef = mDatabase.collection("shelters").document(shelterId);
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
    }

    public void onShelterLoaded(Shelter shelter) {
        shelterNameTextView.setText(shelter.getName());
        shelterCapacityTextView.setText(shelter.getCapacity());
        shelterAgeGroupTextView.setText(shelter.getAgeRange());
        shelterGenderTextView.setText(shelter.getGender());
        shelterChildrenTextView.setText(shelter.isChildrenAllowed() ? "Children allowed" : "Children not allowed");
        shelterRequirementsTextView.setText(shelter.getRequirements());
        shelterCoordinatesTextView.setText(shelter.getCoordinates().toString());
        shelterAddressTextView.setText(shelter.getAddress());
        shelterNotesTextView.setText(String.join(",", shelter.getNotes()));
        shelterPhoneTextView.setText(shelter.getPhoneNumber());


    }
}