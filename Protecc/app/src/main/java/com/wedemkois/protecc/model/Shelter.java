package com.wedemkois.protecc.model;

public class Shelter {
    private String name, gender, address, phoneNumber;
    private String capacity;
    private String longitude, latitude;

    public Shelter(String n, String c, String g, String lo, String la, String a, String p)
    {
        name = n;
        gender = g;
        address = a;
        phoneNumber = p;
        capacity = c;
        longitude = lo;
        latitude = la;
    }

    public String getName()
    { return name; }

    public String getGender()
    { return gender; }

    public String getAddress()
    { return address; }

    public String getPhoneNumber()
    { return phoneNumber; }

    public String getCapacity()
    { return capacity; }

    public String getLongitude()
    { return longitude; }

    public String getLatitude()
    { return latitude; }

}
