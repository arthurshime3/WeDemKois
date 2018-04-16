package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing a Shelter
 *
 * @version 1.0
 * @author Miguel De lod Reyes
 */
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
    private HashMap<String, Integer> occupants; // maps username to number of people in their group

    public enum Gender {
        MEN, WOMEN, BOTH
    }

    public enum AgeRange {
        ADULT, FAMILY, YOUNGADULTS, ALL
    }
    /**
     * Creates a Shelter class with nothing in it
     */
    public Shelter() { }

    /**
     * Shelter Constructor
     * @param name name of the shelter
     * @param individualCapacity max number of individual occupants allowed
     * @param groupCapacity max number of group occupants allowed
     * @param individualBedsTaken number of individual beds used
     * @param groupBedsTaken number of group beds used
     * @param ageRange range of the ages allowed
     * @param gender genders allowed in the shelter
     * @param childrenAllowed if children are allowed
     * @param requirements extra requirements
     * @param longitude longitude of the shelter
     * @param latitude latitude of the shelter
     * @param address address of the shelter
     * @param notes notes about the shelter
     * @param phone phone number of the shelter
     */
    // The parser constructor, calls the full param constructor
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Shelter(String name,
                   String individualCapacity,
                   String groupCapacity,
                   String individualBedsTaken,
                   String groupBedsTaken,
                   String ageRange,
                   String gender,
                   String childrenAllowed,
                   String requirements,
                   String longitude,
                   String latitude,
                   String address,
                   String notes,
                   String phone)
    {
        this(
                name.trim(),
                individualCapacity,
                groupCapacity,
                individualBedsTaken,
                groupBedsTaken,
                ageRange.replaceAll("\\s+","").toUpperCase(),
                gender.replaceAll("\\s+","").toUpperCase(),
                "T".equals(childrenAllowed.replaceAll("\\s+", "")),
                requirements,
                new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
                address,
                new ArrayList<>(Arrays.asList(notes.split(","))),
                phone);
    }

    /**
     * Shelter Constructor
     *
     * @param name name of the shelter
     * @param individualCapacity max number of individual occupants allowed
     * @param groupCapacity max number of group occupants allowed
     * @param individualBedsTaken number of individual beds used
     * @param groupBedsTaken number of group beds used
     * @param ageRange range of the ages allowed
     * @param gender genders allowed in the shelter
     * @param childrenAllowed if children are allowed
     * @param requirements extra requirements
     * @param coordinates coordinates of the shelter
     * @param address address of the shelter
     * @param notes notes about the shelter
     * @param phone phone number of the shelter
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Shelter(String name,
                   String individualCapacity,
                   String groupCapacity,
                   String individualBedsTaken,
                   String groupBedsTaken,
                   String ageRange,
                   String gender,
                   boolean childrenAllowed,
                   String requirements,
                   GeoPoint coordinates,
                   String address,
                   List<String> notes,
                   String phone) {
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
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        this.notes = notes;
        this.phoneNumber = phone;

        occupants = new HashMap<>();
    }

    /**
     * Checks to see if all person are within the shelter requirements
     * @param ageGroup ageGroup of the people
     * @param gender genders of the people
     * @param childrenAllowed states if children are allowed
     * @return if the requirements are met
     */
    @SuppressWarnings("OverlyComplexMethod")
    public boolean checkQualifications(String[] ageGroup,
                                       String[] gender, boolean childrenAllowed) {
        if (!"ALL".equals(this.ageRange)) {
            for (String anAgeGroup : ageGroup) {
                if (!(anAgeGroup.equals(getAgeRange()))) {
                    return false;
                }
            }
        }
        if (!"BOTH".equals(this.gender)) {

            for (String aGender : gender) {
                if (!(aGender.equals(getGender()))) {
                    return false;
                }
            }
        }
        return childrenAllowed == isChildrenAllowed();
    }

    /**
    * Method that updates number of vacant beds at the shelter if possible.
    * @param users can be positive (checking in) or negative (checking out)
    * @param group true if a "group bed" is updating its vacancy
    * @return boolean array of length 2, with index 0 being true if check-in/out was valid,
    * false if not
     * and index 1 being true if the check in/out was a type group,
    * false for type individual
    */
    public boolean[] updateVacancy(int users, boolean group) {
        if ((users == 1) && group) {
            throw new IllegalArgumentException("Error: A single user checking in is not a group");
        }
        if ((users > 1) && !group) {
            throw new IllegalArgumentException("Error: More than one user checking in is a group");
        }

        boolean[] output = {false, group};
        if (users == 0) {
            output[0] = true;
            return output;
        } else if (!group)  // 1 user checking in or any number checking out
        {
            return checkInOneOrCheckOut(users, output);
        } else {    // group checking in or any number checking out
            return checkInGroupOrCheckOut(users, output);
        }
    }

    /**
     * Checks in and out individuals
     * @param users the numbers of individual users checking in or out
     * @param output the boolean to be returned if the method was activated
     * @return the check that the method happened
     */
    public boolean[] checkInOneOrCheckOut(int users, boolean[] output)
    {
        int bedsTaken = Integer.parseInt(getIndividualBedsTaken());
        if ((bedsTaken == 0) && (users < 0)) {
            output[0] = false;
            return output;
        }
        int vacancies = Integer.parseInt(getIndividualCapacity()) - bedsTaken;
        if (vacancies >= users) {
            setIndividualBedsTaken((Integer.parseInt(getIndividualBedsTaken()) + users) + "");
            output[0] = true;
            return output;
        }
        else  // individual beds full, check group beds (this code is only reached for checking in)
        {
            bedsTaken = Integer.parseInt(getGroupBedsTaken());
            vacancies = Integer.parseInt(getGroupCapacity()) - bedsTaken;

            if (vacancies > 0)  // check for open group beds
            {
                setGroupBedsTaken((Integer.parseInt(getGroupBedsTaken()) + 1) + "");
                output[0] = true;
                return output;
            }

            return output;  // no beds found
        }
    }
    /**
     * Checks in and out groups
     * @param users the numbers of group users checking in or out
     * @param output the boolean to be returned if the method was activated
     * @return the check that the method happened
     */
    public boolean[] checkInGroupOrCheckOut(int users, boolean[] output)
    {
        output[1] = true;
        int bedsTaken = Integer.parseInt(getGroupBedsTaken());
        if ((bedsTaken == 0) && (users < 0)) {
            output[0] = false;
            return output;
        }
        int vacancies = Integer.parseInt(getGroupCapacity()) - bedsTaken;
        if (vacancies >= users) {
            setGroupBedsTaken((Integer.parseInt(getGroupBedsTaken()) + users) + "");
            output[0] = true;
            return output;
        }
        else    // group beds full, check individual beds (this code is only reached for checking in
        {
            bedsTaken = Integer.parseInt(getIndividualBedsTaken());
            vacancies = Integer.parseInt(getIndividualCapacity()) - bedsTaken;

            if (vacancies >= users)
            {
                setIndividualBedsTaken((Integer.parseInt(getIndividualBedsTaken()) + users) + "");
                output[0] = true;
                return output;
            }

            return output;  // no beds found
        }
    }

    /**
     *
     * @return name of shelter
     */
    public String getName()
    { return name; }

    /**
     * changes shelter name the parameter name
     * @param name name of the shelter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the character sequence of the requirements
     */
    public CharSequence getRequirements()
    { return requirements; }

    /**
     * sets requirements to the shelter
     * @param requirements requirements of the shelter
     */
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    /**
     *
     * @return this shelter address
     */
    public CharSequence getAddress()
    { return address; }

    /**
     * sets the address to the parameter
     * @param address address of shelter
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return shelter phone number
     */
    public String getPhoneNumber()
    { return phoneNumber; }

    /**
     * sets the shelter's phone number
     * @param phoneNumber takes in a phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return the max capacity of the individual beds
     */
    public String getIndividualCapacity()
    { return individualCapacity; }

    /**
     * sets the individual capacity of beds of the shelter
     * @param capacity number of individual bed spots
     */
    public void setIndividualCapacity(String capacity) {
        this.individualCapacity = capacity;
    }
    /**
     *
     * @return the max capacity of the group beds
     */
    public String getGroupCapacity() { return groupCapacity; }

    /**
     * sets the group capacity of beds of the shelter
     * @param capacity number of group bed spots
     */
    public void setGroupCapacity(String capacity) { this.groupCapacity = capacity; }

    /**
     *
     * @return number of individual bed spots taken
     */
    public String getIndividualBedsTaken() { return individualBedsTaken; }

    /**
     * sets the individual beds taken
     * @param taken number of individual beds taken
     */
    public void setIndividualBedsTaken(String taken) { this.individualBedsTaken = taken; }

    /**
     *
     * @return number of group bed spots taken
     */
    public String getGroupBedsTaken() { return groupBedsTaken; }

    /**
     * sets the group beds taken
     * @param taken number of group beds taken
     */
    public void setGroupBedsTaken(String taken) { this.groupBedsTaken = taken; }

    /**
     *
     * @return the coordinates of the shelter
     */
    public GeoPoint getCoordinates()
    { return coordinates; }

    /**
     * sets the coordinates of the shelter
     * @param coordinates coordinates of the shelter
     */
    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    /**
     *
     * @return the notes of the shelter
     */
    public Iterable<String> getNotes()
    { return Collections.unmodifiableList(notes); }

    /**
     * sets notes of the shelter
     * @param notes notes about the shelter
     */
    public void setNotes(List<String> notes) {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        this.notes = notes;
    }

    /**
     *
     * @return age range allowed
     */
    @SuppressWarnings("TypeMayBeWeakened")
    public String getAgeRange() {
        return ageRange;
    }

    /**
     * sets the age range allowed
     * @param ageRange age range allowed
     */
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    /**
     *
     * @return gender allowed
     */
    @SuppressWarnings("TypeMayBeWeakened")
    public String getGender() {
        return gender;
    }

    /**
     * sets gender of the shelter
     * @param gender gender allowed
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return if children are allowed
     */
    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    /**
     * sets the boolean for children allowed
     * @param childrenAllowed if children are allowed
     */
    public void setChildrenAllowed(boolean childrenAllowed) {
        this.childrenAllowed = childrenAllowed;
    }

    /**
     *  sets the occupants of the shelter
     * @param map the occupants of the shelter
     */
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public void setOccupants(HashMap<String, Integer> map) {
        occupants = map;
    }

    /**
     * adds an occupant to a designated bed
     * @param user the user who needs the bed
     * @param num number of bed spots needed
     */
    public void addOccupant(String user, int num)
    {
        occupants.put(user, num);
    }

    /**
     *
     * @return the occupants in the hash map
     */
    public Map<String,Integer> getOccupants()
    {
        return Collections.unmodifiableMap(occupants);
    }

    /**
     * removes an occupant from the shelter
     * @param username username of the occupant
     * @param occupantType type of the user
     */
    public void removeOccupant(String username, User.OccupantType occupantType)
    {
        if (!occupants.keySet().contains(username)) {
            return;
        }

        updateVacancy(occupants.get(username) * -1, occupantType == User.OccupantType.GROUP);
        occupants.remove(username);
    }

}