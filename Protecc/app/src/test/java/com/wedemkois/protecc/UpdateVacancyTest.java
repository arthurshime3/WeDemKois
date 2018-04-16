package com.wedemkois.protecc;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.rules.ExpectedException;

//Sam Loop

public class UpdateVacancyTest {
	public Shelter shelter;

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
		assertThat(shelter.updateVacancy(0, true), is ({true, true}));
		assertThat(shelter.updateVacancy(0, false), is ({true, false}));
	}

	@Test
	public void testCheckInUserPass() {
		shelter.setIndividualBedsTaken("0");
		shelter.setIndividualCapacity("1");
		boolean[] expected = {true, false};
		boolean[] actual = shelter.checkInOneOrCheckOut(1, {false, false});
		assertEqual(expected, actual);
	}

	@Test
	public void testCheckInUserFail() {
		shelter.setIndividualBedsTaken("1");
		shelter.setIndividualCapacity("1");
		boolean[] expected = {false, false};
		boolean[] actual = shelter.checkInOneOrCheckOut(1, {false, false});
		assertEqual(expected, actual);
	}

	@Test
	public void testCheckInGroupPass() {
		shelter.setGroupBedsTaken("0");
		shelter.setGroupCapacity("7");
		boolean[] expected = {true, true};
		boolean[] actual = checkInOneOrCheckOut(5, {false, true});
		assertEqual(expected, actual);
	}

	@Test
	public void testCheckInGroupFail() {
		shelter.setGroupBedsTaken("1");
		shelter.setGroupCapacity("1");
		boolean[] expected = {false, true};
		boolean[] actual = checkInGroupOrCheckOut(5, {false, true});
		assertEqual(expected, actual);
	}

}

