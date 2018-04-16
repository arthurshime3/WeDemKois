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

    Shelter shelterALL = new Shelter("", "", "",
            "", "", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");

    private int users = 1;
    private boolean[] output = {};

    @Test
    public void checkBasicQualificationsTest() {
        assertEquals(shelterALL.checkInOneOrCheckOut(users, output), true);
    }


}