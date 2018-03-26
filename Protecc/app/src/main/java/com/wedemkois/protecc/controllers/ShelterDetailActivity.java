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
    * @param group true if a "group bed" is updating its vacancy
    * @return boolean array of length 2, with index 0 being true if check-in/out was valid, false if not
    *   and index 1 being true if the check in/out was a type group, false for type individual
    */
    public boolean[] updateVacancy(int users, boolean group) {
        if (users == 1 && group)
            throw new IllegalArgumentException("Error: A single user checking in is not a group");
        if (users > 1 && !group)
            throw new IllegalArgumentException("Error: More than one user checking in is a group");

        boolean[] output = {false, group};
        if (users == 0) {
            output[0] = true;
            return output;
        } else if (!group)  // 1 user checking in or any number checking out
        {
            int bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken());
            if (bedsTaken == 0 && users < 0) {
                output[0] = false;
                return output;
            }
            int vacancies = Integer.parseInt(currentShelter.getIndividualCapacity()) - bedsTaken;
            if (vacancies >= users) {
                currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                updateCapacityTextView();
                output[0] = true;
                return output;
            }
            else    // individual beds full, check group beds (this code is only reached for checking in)
            {
                bedsTaken = Integer.parseInt(currentShelter.getGroupBedsTaken());
                vacancies = Integer.parseInt(currentShelter.getGroupCapcity()) - bedsTaken;

                if (vacancies > 0)  // check for open group beds
                {
                    currentShelter.setGroupBedsTaken((Integer.parseInt(currentShelter.getGroupBedsTaken()) + 1) + "");
                    updateCapacityTextView();
                    output[0] = true;
                    return output;
                }

                return output;  // no beds found
            }
        } else {    // group checking in or any number checking out
            output[1] = true;
            int bedsTaken = Integer.parseInt(currentShelter.getGroupBedsTaken());
            if (bedsTaken == 0 && users < 0) {
                output[0] = false;
                return output;
            }
            int vacancies = Integer.parseInt(currentShelter.getGroupCapcity()) - bedsTaken;
            if (vacancies >= users) {
                currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                updateCapacityTextView();
                output[0] = true;
                return output;
            }
            else    // group beds full, check individual beds (this code is only reached for checking in
            {
                bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken());
                vacancies = Integer.parseInt(currentShelter.getIndividualCapacity()) - bedsTaken;

                if (vacancies >= users)
                {
                    currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                    updateCapacityTextView();
                    output[0] = true;
                    return output;
                }

                return output;  // no beds found
            }
        }
    }

    private void updateCapacityTextView()
    {
        int capacity = Integer.parseInt(currentShelter.getIndividualCapacity()) - Integer.parseInt(currentShelter.getIndividualBedsTaken())
                + (Integer.parseInt(currentShelter.getGroupCapcity()) - Integer.parseInt(currentShelter.getGroupBedsTaken()) * 4);

        shelterCapacityTextView.setText(capacity + "");
    }
}
