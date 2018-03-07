package com.wedemkois.protecc.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.adapters.ShelterAdapter;
import com.wedemkois.protecc.model.Shelter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelterActivity extends AppCompatActivity implements
        ShelterAdapter.OnShelterSelectedListener {
    private final int LIMIT = 50;

    @BindView(R.id.shelterRecyclerView)
    RecyclerView mRecyclerView;

    private ShelterAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseFirestore mDatabase;
    private Query mQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelters);
        ButterKnife.bind(this);

        mDatabase = FirebaseFirestore.getInstance();

        // Get ${LIMIT} shelters
        mQuery = mDatabase.collection("shelters")
                .orderBy("name", Query.Direction.DESCENDING)
                .limit(LIMIT);

        // Optimization purposes (butter knife already bound this to its view)
        mRecyclerView.setHasFixedSize(true);

        // RecyclerView
        mAdapter = new ShelterAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // TODO eventually we want to change this to have an empty query result view
                if(getItemCount() == 0) {
                    Log.d("ShelterActivity", "So there's no shelters for some reason");
                }
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a toast on errors
                Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong, check logs", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    private void getSheltersFromDatabase() {
        mDatabase.collection("shelters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Shelter> out = new ArrayList<>();
                            for(DocumentSnapshot document : task.getResult()) {
                                out.add(document.toObject(Shelter.class));
                            }
                            Log.d("getShelters", "There was no error getting shelters good job");
                        } else {
                            Log.d("getShelters", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onShelterSelected(DocumentSnapshot Shelter) {

    }

}
