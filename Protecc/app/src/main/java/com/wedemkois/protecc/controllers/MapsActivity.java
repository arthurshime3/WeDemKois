package com.wedemkois.protecc.controllers;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.location.Criteria;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wedemkois.protecc.Filters;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.adapters.PermissionUtils;
import com.wedemkois.protecc.model.Shelter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMarkerClickListener
{

    private GoogleMap mMap;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION =
            android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Boolean mLocationPermissionsGranted = false;

    private FirebaseFirestore mDatabase;
//    private Query mQuery;
    private Filters filters;

    private List<Shelter> shelters;
    private List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        filters = Objects.requireNonNull(getIntent().getExtras()).getParcelable("filter_parcel");

        mDatabase = FirebaseFirestore.getInstance();

        shelters = new ArrayList<>();
        markers = new ArrayList<>();

    }
    private void initMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        getLocationPermission();
        enableMyLocation();
        mMap.setOnMarkerClickListener(this);
        filterShelters();
    }

    /**
     * place to add the markers
     */
    @SuppressWarnings("FeatureEnvy")
    private void addMarkersToMap() {
        for (Shelter shelter: shelters) {
            Marker newMarker = mMap.addMarker(new MarkerOptions()
            .position(new LatLng(shelter.getCoordinates().getLatitude(),
                    shelter.getCoordinates().getLongitude()))
            .title(shelter.getName())
            .snippet(shelter.getPhoneNumber()));
            markers.add(newMarker);
        }

        // Set the camera bounds correctly
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.moveCamera(cu);
//        Marker mySistersHouse = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(33.780174, -84.410142))
//                .title("My Sister's House")
//                .snippet("Phone Number: (404) 367-2465"));
    }

    @SuppressWarnings("FeatureEnvy")
    private void filterShelters() {
        Query query = mDatabase.collection("shelters");

        if (filters.hasName()) {
            query = query.whereEqualTo("name", filters.getName());
        }

        if (filters.hasGender()) {
            assert filters.getGender() != null;
            query = query.whereEqualTo("gender", filters.getGender().toString());
        }

        if (filters.hasAgeRange()) {
            assert filters.getAgeRange() != null;
            query = query.whereEqualTo("ageRange", filters.getAgeRange().toString());
        }

        // Limit items
        final int LIMIT = 50;
        query = query.limit(LIMIT);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        shelters.add(document.toObject(Shelter.class));
                        addMarkersToMap();
                    }
                } else {
                    Log.d("MapsActivity", Objects.requireNonNull(task.getException()).toString());
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

    /**
     * When the cross hair is clicked, the map zooms toward my location
     * @return false so that we don't consume the event and the default behavior still occurs
     * (I don't know what this means)
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current Location button clicked", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, true));
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate point = CameraUpdateFactory.newLatLng(myLocation);
            mMap.moveCamera(point);
            mMap.animateCamera(point);

            mMap.setMyLocationEnabled(true);
            //noinspection MagicNumber
            float displayBuffer = 15f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, displayBuffer));
        }

        return false;
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    /**
     * Permission requests for my location to work
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getLocationPermission(){
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }
}
