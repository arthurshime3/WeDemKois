package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shelter {
    private String name;
    private String capacity;
    private String ageRange;
    private String gender;
    private boolean childrenAllowed;
    private String requirements;
    private GeoPoint coordinates;
    private String address;
    private List<String> notes;
    private String phoneNumber;
    private String groupCapacity;
    private String bedsTaken;
    private String groupsTaken;

    public enum Gender {
        MEN, WOMEN, BOTH
    }

    public enum AgeRange {
        ADULT, FAMILY, YOUNGADULTS, ALL
    }

    public Shelter() { }

    // The parser constructor, calls the full param constructor
    public Shelter(String name, String capacity, String ageRange, String gender,
                   String childrenAllowed, String requirements, String longitude,
                   String latitude, String address, String notes, String phone,
                   String groupCapacity, String bedsTaken, String groupsTaken)
    {
        this(
                name.trim(),
                capacity,
                ageRange.replaceAll("\\s+","").toUpperCase(),
                gender.replaceAll("\\s+","").toUpperCase(),
                childrenAllowed.replaceAll("\\s+","").equals("T"),
                requirements,
                new GeoPoint(Double.parseDouble(longitude), Double.parseDouble(latitude)),
                address,
                new ArrayList<>(Arrays.asList(notes.split(","))),
                phone, groupCapacity, bedsTaken, groupsTaken);
    }

    public Shelter(String name, String capacity, String ageRange, String gender,
                   boolean childrenAllowed, String requirements, GeoPoint coordinates,
                   String address, List<String> notes, String phone, String groupCapacity,
                   String bedsTaken, String groupsTaken) {
        this.name = name;
        this.capacity = capacity;
        this.ageRange = ageRange;
        this.gender = gender;
        this.childrenAllowed = childrenAllowed;
        this.requirements = requirements;
        this.coordinates = coordinates;
        this.address = address;
        this.notes = notes;
        this.phoneNumber = phone;
        this.groupCapacity = groupCapacity;
        this.bedsTaken = bedsTaken;
        this.groupsTaken = groupsTaken;
    }

    public String getName()
    { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirements()
    { return requirements; }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
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

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    public void setChildrenAllowed(boolean childrenAllowed) {
        this.childrenAllowed = childrenAllowed;
    }

    public String getGroupCapacity() {
        return groupCapacity;
    }

    public void setGroupCapacity(String groupCapacity) {
        this.groupCapacity = groupCapacity;
    }

    public String getBedsTaken() {
        return bedsTaken;
    }

    public void setBedsTaken(String bedsTaken) {
        this.bedsTaken = bedsTaken;
    }

    public String getGroupsTaken() {
        return groupsTaken;
    }

    public void setGroupsTaken(String groupsTaken) {
        this.groupsTaken = groupsTaken;
    }
}