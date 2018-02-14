package com.wedemkois.protecc.model;

/**
 * Created by arthurshim on 2/13/18.
 */

public class User
{
    String username, password;

    public User(String user, String pass)
    {
        username = user;
        password = pass;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
