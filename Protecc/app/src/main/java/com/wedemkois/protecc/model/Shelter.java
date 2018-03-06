package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shelter {
    private String name, address, phoneNumber;
    private String capacity;
    private GeoPoint coordinates;
    private String restrictions;
    private List<String> notes;

    public Shelter(String name, String capacity, String restrictions, String longitude, String latitude, String address, String notes, String phone)
    {
        this.name = name;
        this.restrictions = restrictions;
        this.address = address;
        this.phoneNumber = phone;
        this.capacity = capacity;
        this.notes = new ArrayList<String>(Arrays.asList(notes.split(",")));
        coordinates = new GeoPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
    }

    public String getName()
    { return name; }

    public String getRestrictions()
    { return restrictions; }

    public String getAddress()
    { return address; }

    public String getPhoneNumber()
    { return phoneNumber; }

    public String getCapacity()
    { return capacity; }

    public GeoPoint getCoordinates()
    { return coordinates; }

    public List<String> getNotes()
    { return notes; }

}