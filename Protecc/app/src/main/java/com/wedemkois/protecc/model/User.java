package com.wedemkois.protecc.model;
import java.util.Arrays;
import java.util.List;

/**
 * Created by arthurshim on 2/13/18.
 */

public class User
{
    private String username, password, firstName, lastName;
    private UserType userType;

    public enum UserType
    {
        USER, ADMIN;
    }

    public User(String user, String pass, UserType uT, String firstName, String lastName)
    {
        username = user;
        password = pass;
        userType = uT;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public UserType getUserType() {return userType;}

    public String getName() {return firstName + " " + lastName;}
}
