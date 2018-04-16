package com.wedemkois.protecc.model;

/**
<<<<<<< HEAD
 * Created by arthurshim on 2/13/18.
 */
=======
 * A class representing a User
 *
 * @version 1.0
 * @author Arthur Shim
 * */
>>>>>>> master

public class User
{
    private String username, firstName, lastName;
    private UserType userType;

    public enum UserType
    {
        USER, ADMIN
    }

<<<<<<< HEAD
    public User() {
=======
    public enum OccupantType
    {
        INDIVIDUAL, GROUP
    }

    /**
     * Default no-arg constructor needed to retrieve a User from firestore
     */

    public User()
    {
>>>>>>> master

    }

    /**
     * user constructor
     * @param user username of the user
     * @param uT user type of the user
     * @param firstName first name of the user
     * @param lastName last name of the suer
     */
    public User(String user, UserType uT, String firstName, String lastName)
    {
        this.username = user;
        this.userType = uT;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     *
     * @return username
     */
    public String getUsername()
    {
        return username;
    }

<<<<<<< HEAD

    public UserType getUserType() {return userType;}

    public String getName() {return firstName + " " + lastName;}
=======
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
>>>>>>> master

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

<<<<<<< HEAD
=======
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

>>>>>>> master
    @Override
    public String toString() {
        return userType + " " + getName() + " " + username;
    }

}
