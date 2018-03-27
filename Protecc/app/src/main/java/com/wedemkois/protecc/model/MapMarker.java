package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jeffrey on 3/27/18.
 */

public class MapMarker {
    LatLng shelterLoc;
    String shelterName;

    public MapMarker() {}
    public MapMarker(LatLng location, String shelterName) {
        shelterLoc = location;
        this.shelterName = shelterName;
    }
    public LatLng getLocation() {
        return shelterLoc;
    }
    public String getShelterName() {
        return shelterName;
    }

}
