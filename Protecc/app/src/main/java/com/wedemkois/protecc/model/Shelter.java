package com.wedemkois.protecc.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shelter {
    private String name;
    private String capacity;
    private String ageRange;
    private String gender;
    private boolean childrenAllowed;
    private String requirements;
    private GeoPoint coordinates;
    private String address;
    private List<String> notes;
    private String phoneNumber;
<<<<<<< HEAD
    private String groupCapacity;
    private String bedsTaken;
    private String groupsTaken;
=======
    private HashMap<String, Integer> occupants; // maps username to number of people in their group
>>>>>>> master

    public enum Gender {
        MEN, WOMEN, BOTH
    }

    public enum AgeRange {
        ADULT, FAMILY, YOUNGADULTS, ALL
    }

    public Shelter() { }

    // The parser constructor, calls the full param constructor
<<<<<<< HEAD
    public Shelter(String name, String capacity, String ageRange, String gender,
                   String childrenAllowed, String requirements, String longitude,
                   String latitude, String address, String notes, String phone,
                   String groupCapacity, String bedsTaken, String groupsTaken)
=======
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
>>>>>>> master
    {
        this(
                name.trim(),
                capacity,
                ageRange.replaceAll("\\s+","").toUpperCase(),
                gender.replaceAll("\\s+","").toUpperCase(),
                childrenAllowed.replaceAll("\\s+","").equals("T"),
                requirements,
                new GeoPoint(Double.parseDouble(longitude), Double.parseDouble(latitude)),
                address,
                new ArrayList<>(Arrays.asList(notes.split(","))),
                phone, groupCapacity, bedsTaken, groupsTaken);
    }

<<<<<<< HEAD
    public Shelter(String name, String capacity, String ageRange, String gender,
                   boolean childrenAllowed, String requirements, GeoPoint coordinates,
                   String address, List<String> notes, String phone, String groupCapacity,
                   String bedsTaken, String groupsTaken) {
=======
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
>>>>>>> master
        this.name = name;
        this.capacity = capacity;
        this.ageRange = ageRange;
        this.gender = gender;
        this.childrenAllowed = childrenAllowed;
        this.requirements = requirements;
        this.coordinates = coordinates;
        this.address = address;
        this.notes = notes;
        this.phoneNumber = phone;
<<<<<<< HEAD
        this.groupCapacity = groupCapacity;
        this.bedsTaken = bedsTaken;
        this.groupsTaken = groupsTaken;
=======

        occupants = new HashMap<>();
    }

    @SuppressWarnings("OverlyComplexMethod")
    public boolean checkQualifications(String[] ageGroup,
                                       String[] gender, boolean childrenAllowed) {
        boolean checkAge = false;
        boolean checkGender = false;
        if ("ALL".equals(this.ageRange)) {
            checkAge = true;
        } else {
            for (String anAgeGroup : ageGroup) {
                if (anAgeGroup.equals(getAgeRange()) || "ALL".equals(anAgeGroup)) {
                    checkAge = true;
                }
            }
        }
        if ("BOTH".equals(this.gender)) {
            checkGender = true;
        } else {
            for (String aGender : gender) {
                if (aGender.equals(getGender()) || "BOTH".equals(aGender)) {
                    checkGender = true;
                }
            }
        }
        return checkAge && checkGender && (childrenAllowed == isChildrenAllowed());
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

    private boolean[] checkInGroupOrCheckOut(int users, boolean[] output)
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
>>>>>>> master
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

    public String getCapacity()
    { return capacity; }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

<<<<<<< HEAD
=======
    public String getGroupCapacity() { return groupCapacity; }

    public void setGroupCapacity(String capacity) { this.groupCapacity = capacity; }

    public String getIndividualBedsTaken() { return individualBedsTaken; }

    private void setIndividualBedsTaken(String taken) { this.individualBedsTaken = taken; }

    public String getGroupBedsTaken() { return groupBedsTaken; }

    private void setGroupBedsTaken(String taken) { this.groupBedsTaken = taken; }

>>>>>>> master
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
<<<<<<< HEAD

    public String getGroupCapacity() {
        return groupCapacity;
    }

    public void setGroupCapacity(String groupCapacity) {
        this.groupCapacity = groupCapacity;
    }

    public String getBedsTaken() {
        return bedsTaken;
    }

    public void setBedsTaken(String bedsTaken) {
        this.bedsTaken = bedsTaken;
    }

    public String getGroupsTaken() {
        return groupsTaken;
    }

    public void setGroupsTaken(String groupsTaken) {
        this.groupsTaken = groupsTaken;
    }
=======
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public void setOccupants(HashMap<String, Integer> map) {
        occupants = map;
    }
    public void addOccupant(String user, int num)
    {
        occupants.put(user, num);
    }

    public Map<String,Integer> getOccupants()
    {
        return Collections.unmodifiableMap(occupants);
    }

    public void removeOccupant(String username, User.OccupantType occupantType)
    {
        if (!occupants.keySet().contains(username)) {
            return;
        }

        updateVacancy(occupants.get(username) * -1, occupantType == User.OccupantType.GROUP);
        occupants.remove(username);
    }

>>>>>>> master
}