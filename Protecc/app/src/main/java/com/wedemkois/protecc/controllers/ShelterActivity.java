package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.wedemkois.protecc.Filters;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.adapters.ShelterAdapter;

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

    private Filters filters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelters);
        ButterKnife.bind(this);

        filters = getIntent().getExtras().getParcelable("filter_parcel");

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
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Something went wrong, check logs", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        filterShelters();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onShelterSelected(DocumentSnapshot shelter) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, ShelterDetailActivity.class);
        intent.putExtra("shelter_id", shelter.getId());

        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    public void filterShelters() {
        Query query = mDatabase.collection("shelters");

        if(filters.hasName()) {
            query = query.whereEqualTo("name", filters.getName());
        }

        if(filters.hasGender()) {
            query = query.whereEqualTo("gender", filters.getGender().toString());
        }

        if(filters.hasAgeRange()) {
            query = query.whereEqualTo("ageRange", filters.getAgeRange().toString());
        }

        // Limit items
        query = query.limit(LIMIT);

        // Update the query
        mAdapter.setQuery(query);
    }

}
