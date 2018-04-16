package com.wedemkois.protecc;

import com.wedemkois.protecc.model.Shelter;

import org.junit.Rule;
import org.junit.Test;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.rules.ExpectedException;

//Sam Loop

public class UpdateVacancyTest {
    public Shelter shelter;
    private boolean[] ff = {false, false};
    private boolean[] ft = {false, true};
    private boolean[] tf = {true, false};
    private boolean[] tt = {true, true};


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testExceptionSingleUser() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Error: A single user checking in is not a group");
        shelter.updateVacancy(1, true);
    }

    @Test
    public void testExceptionMultipleUsers() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Error: More than one user checking in is a group");
        shelter.updateVacancy(2, false);
    }

    @Test
    public void testCheckZeroUser() {
        assertThat(shelter.updateVacancy(0, true), is (tt));
        assertThat(shelter.updateVacancy(0, false), is (tf));
    }

    @Test
    public void testCheckInUserPass() {
        shelter.setIndividualBedsTaken("0");
        shelter.setIndividualCapacity("1");
        boolean[] expected = {true, false};
        boolean[] actual = shelter.checkInOneOrCheckOut(1, ff);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckInUserFail() {
        shelter.setIndividualBedsTaken("1");
        shelter.setIndividualCapacity("1");
        boolean[] expected = {false, false};
        boolean[] actual = shelter.checkInOneOrCheckOut(1, ff);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckInGroupPass() {
        shelter.setGroupBedsTaken("0");
        shelter.setGroupCapacity("7");
        boolean[] expected = {true, true};
        boolean[] actual = shelter.checkInOneOrCheckOut(5, ft);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckInGroupFail() {
        shelter.setGroupBedsTaken("1");
        shelter.setGroupCapacity("1");
        boolean[] expected = {false, true};
        boolean[] actual = shelter.checkInGroupOrCheckOut(5, ft);
        assertEquals(expected, actual);
    }

}
