package com.wedemkois.protecc;

import org.junit.Test;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * Created by jeffrey on 4/16/18.
 */


@SuppressWarnings("ALL")
public class RemoveOccupantTest {
    Shelter shelter = new Shelter("", "10", "10",
            "1", "3", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");
    User user = new User("", User.UserType.USER, "", "");
    HashMap<String, Integer> occupants = new HashMap<String, Integer>(){{
        put("User1", 3);
        put("User2", 1);
        put("User3", 3);
    }};

    @Test
    public void removeFromGroupVacancy() {
        shelter.setOccupants(occupants);

        assertEquals("3", shelter.getGroupBedsTaken());
        shelter.removeOccupant("User1", User.OccupantType.GROUP);
        assertEquals(false, occupants.containsValue("User1"));
        assertEquals("0", shelter.getGroupBedsTaken());
    }
    @Test
    public void removeFromIndividualVacancy() {
        shelter.setOccupants(occupants);
        assertEquals("1", shelter.getIndividualBedsTaken());
        shelter.removeOccupant("User2", User.OccupantType.INDIVIDUAL);
        assertEquals(false, occupants.containsValue("User2"));
        assertEquals("0", shelter.getIndividualBedsTaken());
    }

}
