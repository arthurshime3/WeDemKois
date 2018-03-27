package com.wedemkois.protecc.model;

/**
 * Created by arthurshim on 2/13/18.
 */

public class User
{
    private String username, firstName, lastName, shelter;
    private UserType userType;
    private OccupantType occupantType;

    public enum UserType
    {
        USER, ADMIN
    }

    public enum OccupantType
    {
        INDIVIDUAL, GROUP
    }

    public User() {

    }

    public User(String user, UserType uT, String firstName, String lastName)
    {
        this.username = user;
        this.userType = uT;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername()
    {
        return username;
    }

    public UserType getUserType() {return userType;}

    public String getName() {return firstName + " " + lastName;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setShelter(String shelterName)
    {
        shelter = shelterName;
    }

    public String getShelter()
    {
        return shelter;
    }

    public OccupantType getOccupantType()
    {
        return occupantType;
    }

    public void setOccupantType(OccupantType o)
    {
        occupantType = o;
    }

    @Override
    public String toString() {
        return userType + " " + getName() + " " + username;
    }
}
