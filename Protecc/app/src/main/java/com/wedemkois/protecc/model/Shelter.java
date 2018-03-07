package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shelter {
    private String name, address, phoneNumber;
    private String capacity;
    private GeoPoint coordinates;
    private String restrictions;
    private List<String> notes;


    public Shelter() { }

    public Shelter(String name, String capacity, String restrictions, GeoPoint coordinates, String address, List<String> notes, String phone) {
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.coordinates = coordinates;
        this.address = address;
        this.notes = notes;
        this.phoneNumber = phone;
    }

    // The parser constructor, calls the full param constructor
    public Shelter(String name, String capacity, String restrictions, String longitude, String latitude, String address, String notes, String phone)
    {
        this(name,
                capacity,
                restrictions,
                new GeoPoint(Double.parseDouble(longitude), Double.parseDouble(latitude)),
                address,
                new ArrayList<String>(Arrays.asList(notes.split(","))),
                phone);
    }

    public String getName()
    { return name; }

    public String getRestrictions()
    { return restrictions; }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getAddress()
    { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber()
    { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCapacity()
    { return capacity; }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public GeoPoint getCoordinates()
    { return coordinates; }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getNotes()
    { return notes; }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}