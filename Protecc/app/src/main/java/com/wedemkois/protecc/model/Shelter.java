package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Shelter {
    private String name;
    private String individualCapacity;
    private String groupCapacity;
    private String individualBedsTaken;
    private String groupBedsTaken;
    private String ageRange;
    private String gender;
    private boolean childrenAllowed;
    private String requirements;
    private GeoPoint coordinates;
    private String address;
    private List<String> notes;
    private String phoneNumber;
    private HashMap<String, User.OccupantType> occupants;

    public enum Gender {
        MEN, WOMEN, BOTH
    }

    public enum AgeRange {
        ADULT, FAMILY, YOUNGADULTS, ALL
    }

    public Shelter() { }

    // The parser constructor, calls the full param constructor
    public Shelter(String name, String individualCapacity, String groupCapacity, String individualBedsTaken, String groupBedsTaken,
                   String ageRange, String gender, String childrenAllowed, String requirements, String longitude, String latitude,
                   String address, String notes, String phone)
    {
        this(
                name.trim(),
                individualCapacity,
                groupCapacity,
                individualBedsTaken,
                groupBedsTaken,
                ageRange.replaceAll("\\s+","").toUpperCase(),
                gender.replaceAll("\\s+","").toUpperCase(),
                childrenAllowed.replaceAll("\\s+","").equals("T"),
                requirements,
                new GeoPoint(Double.parseDouble(longitude), Double.parseDouble(latitude)),
                address,
                new ArrayList<>(Arrays.asList(notes.split(","))),
                phone);
    }

    public Shelter(String name, String individualCapacity, String groupCapacity, String individualBedsTaken, String groupBedsTaken,
                   String ageRange, String gender, boolean childrenAllowed, String requirements, GeoPoint coordinates,
                   String address, List<String> notes, String phone) {
        this.name = name;
        this.individualCapacity = individualCapacity;
        this.groupCapacity = groupCapacity;
        this.individualBedsTaken =  individualBedsTaken;
        this.groupBedsTaken = groupBedsTaken;
        this.ageRange = ageRange;
        this.gender = gender;
        this.childrenAllowed = childrenAllowed;
        this.requirements = requirements;
        this.coordinates = coordinates;
        this.address = address;
        this.notes = notes;
        this.phoneNumber = phone;

        occupants = new HashMap<>();
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

    public String getIndividualCapacity()
    { return individualCapacity; }

    public void setIndividualCapacity(String capacity) {
        this.individualCapacity = capacity;
    }

    public String getGroupCapacity() { return groupCapacity; }

    public void setGroupCapacity(String capacity) { this.groupCapacity = capacity; }

    public String getIndividualBedsTaken() { return individualBedsTaken; }

    public void setIndividualBedsTaken(String taken) { this.individualBedsTaken = taken; }

    public String getGroupBedsTaken() { return groupBedsTaken; }

    public void setGroupBedsTaken(String taken) { this.groupBedsTaken = taken; }

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

    public void addOccupant(String name, User.OccupantType type)
    {
        occupants.put(name, type);
    }

    public HashMap<String, User.OccupantType> getOccupants()
    {
        return occupants;
    }
}