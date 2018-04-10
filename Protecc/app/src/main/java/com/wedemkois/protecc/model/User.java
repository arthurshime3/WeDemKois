package com.wedemkois.protecc.model;

/**
 * Created by Arthur Shim on 2/13/18.
 */

public class User
{
    private String username;
    private String firstName;
    private String lastName;
    private UserType userType;
    private OccupantType occupantType;
    private String shelterId;

    public enum UserType
    {
        USER, ADMIN
    }

    public enum OccupantType
    {
        INDIVIDUAL, GROUP
    }

    // Default no-arg constructor needed to retrieve a User from firestore
    public User()
    {

    }

    public User(String user, UserType uT, String firstName, String lastName)
    {
        this.username = user;
        this.userType = uT;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shelterId = "";
    }

    public String getUsername()
    {
        return username;
    }

    public UserType getUserType() {return userType;}

    public CharSequence getName() {return firstName + " " + lastName;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public OccupantType getOccupantType()
    {
        return occupantType;
    }

    public void setOccupantType(OccupantType o)
    {
        occupantType = o;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }

    @Override
    public String toString() {
        return userType + " " + getName() + " " + username;
    }
}
