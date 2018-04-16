package com.wedemkois.protecc.model;

/**
 * Created by arthurshim on 2/13/18.
 */

public class User
{
    private String username, firstName, lastName;
    private UserType userType;

    public enum UserType
    {
        USER, ADMIN
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

    @Override
    public String toString() {
        return userType + " " + getName() + " " + username;
    }

}
