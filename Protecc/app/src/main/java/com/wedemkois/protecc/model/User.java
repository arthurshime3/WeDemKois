package com.wedemkois.protecc.model;

/**
 * A class representing a User
 *
 * @version 1.0
 * @author Arthur Shim
 * */

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

    /**
     * Default no-arg constructor needed to retrieve a User from firestore
     */

    public User()
    {

    }

    /**
     * user constructor
     * @param user username of the user
     * @param uT user type of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     */
    public User(String user, UserType uT, String firstName, String lastName)
    {
        this.username = user;
        this.userType = uT;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shelterId = "";
    }

    /**
     *
     * @return username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     *
     * @return user type
     */
    public UserType getUserType() {return userType;}

    /**
     *
     * @return first and last name
     */
    public CharSequence getName() {return firstName + " " + lastName;}

    /**
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return occupant type
     */
    public OccupantType getOccupantType()
    {
        return occupantType;
    }

    /**
     * sets occupant type
     * @param o occupant type of the user
     */
    public void setOccupantType(OccupantType o)
    {
        occupantType = o;
    }

    /**
     *
     * @return shelter id that the user is currently in
     */
    public String getShelterId() {
        return shelterId;
    }

    /**
     * sets the shelter id for the user
     * @param shelterId shelter id of the shelter
     */
    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }

    @Override
    public String toString() {
        return userType + " " + getName() + " " + username;
    }

}
