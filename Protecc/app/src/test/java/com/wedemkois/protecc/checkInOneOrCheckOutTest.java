package com.wedemkois.protecc;

import org.junit.Test;
import com.wedemkois.protecc.model.Shelter;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class checkInOneOrCheckOutTest {

    Shelter shelterALL1 = new Shelter("", "", "",
            "0", "", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL2 = new Shelter("", "5", "",
            "0", "", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL3 = new Shelter("", "", "5",
            "0", "0", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    Shelter shelterALL4 = new Shelter("", "0", "0",
            "0", "0", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    @Test
    public void branchOneTest() {
        int users = -1;
        boolean[] output = {};
        assertEquals(shelterALL1.checkInOneOrCheckOut(users, output), false);
    }

    @Test
    public void branchTwoTest() {
        int users = 1;
        boolean[] output = {};
        assertEquals(shelterALL2.checkInOneOrCheckOut(users, output), true);
    }

    @Test
    public void branchThreeTest() {
        int users = 1;
        boolean[] output = {};
        assertEquals(shelterALL3.checkInOneOrCheckOut(users, output), true);
    }

    @Test
    public void branchFourTest() {
        int users = 1;
        boolean[] output = {false};
        assertEquals(shelterALL4.checkInOneOrCheckOut(users, output), false);
    }

}