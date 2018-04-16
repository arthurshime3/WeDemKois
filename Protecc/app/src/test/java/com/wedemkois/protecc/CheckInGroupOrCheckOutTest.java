package com.wedemkois.protecc;

//Arthur Shim junit

import org.junit.Test;
import com.wedemkois.protecc.model.Shelter;
import static org.junit.Assert.assertEquals;

public class CheckInGroupOrCheckOutTest
{
    Shelter shelterALL1 = new Shelter("", "0", "0",
            "0", "0", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL2 = new Shelter("", "0", "5",
            "0", "5", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL3 = new Shelter("", "5", "5",
            "0", "0", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL4 = new Shelter("", "5", "0",
            "0", "0", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    @Test
    public void checkInvalidCheckout() {
        int users = -2;
        boolean[] output = {false, false};
        output = shelterALL1.checkInOneOrCheckOut(users, output);
        assertEquals(false, output[0]);
        assertEquals("0", shelterALL1.getGroupBedsTaken());
    }

    @Test
    public void checkValidCheckout() {
        int users = -2;
        boolean[] output = {false, false};
        output = shelterALL2.checkInOneOrCheckOut(users, output);
        assertEquals(true, output[0]);
        assertEquals("3", shelterALL2.getGroupBedsTaken());

    }

    @Test
    public void checkGroupBedsAvailableCheckIn() {
        int users = 2;
        boolean[] output = {false, false};
        output = shelterALL3.checkInOneOrCheckOut(users, output);
        assertEquals(true, output[0]);
        assertEquals("2", shelterALL3.getGroupBedsTaken());

    }

    @Test
    public void checkIndividualBedsAvailableCheckIn() {
        int users = 2;
        boolean[] output = {false, false};
        output = shelterALL4.checkInOneOrCheckOut(users, output);
        assertEquals(true, output[0]);
        assertEquals("2", shelterALL3.getIndividualBedsTaken());

    }

    @Test
    public void checkFailedCheckIn() {
        int users = 2;
        boolean[] output = {false, false};
        output = shelterALL1.checkInOneOrCheckOut(users, output);
        assertEquals(false, output[0]);
        assertEquals("0", shelterALL1.getGroupBedsTaken());
    }
}
