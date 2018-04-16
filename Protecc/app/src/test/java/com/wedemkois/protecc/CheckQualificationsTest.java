package com.wedemkois.protecc;

import org.junit.Test;
import com.wedemkois.protecc.model.Shelter;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class CheckQualificationsTest {

    Shelter shelterALL = new Shelter("", "", "",
            "", "", "ALL",
            "BOTH", "T", "",
            "0", "0", "", "", "");


    private String[] ageGroups = new String[3];

    String[] genders = new String[2];
    boolean childrenAllowed = true;

    @Test
    public void checkBasicQualificationsTest() {
        assertEquals(true,
                shelterALL.checkQualifications(ageGroups, genders, childrenAllowed));
    }

    @Test
    public void checkAdultOnlyQualificationsTest() {
        ageGroups[0] = Shelter.AgeRange.ADULT.name();
        ageGroups[1] = Shelter.AgeRange.YOUNGADULTS.name();
        ageGroups[2] = Shelter.AgeRange.FAMILY.name();
        genders[0] = Shelter.Gender.BOTH.name();
        genders[1] = Shelter.Gender.MEN.name();
        shelterALL.setAgeRange(Shelter.AgeRange.ADULT.name());
        shelterALL.setChildrenAllowed(false);

        assertEquals(false,
                shelterALL.checkQualifications(ageGroups, genders, childrenAllowed));
    }

    @Test
    public void checkFamilyQualificationsTest() {
        ageGroups[0] = Shelter.AgeRange.ALL.name();
        ageGroups[1] = Shelter.AgeRange.YOUNGADULTS.name();
        ageGroups[2] = Shelter.AgeRange.FAMILY.name();
        shelterALL.setAgeRange(Shelter.AgeRange.FAMILY.name());
        shelterALL.setGender(Shelter.Gender.BOTH.name());
        shelterALL.setChildrenAllowed(true);
        assertEquals(true,
                shelterALL.checkQualifications(ageGroups, genders, childrenAllowed));
    }

    @Test
    public void checkMenOnlyQualificationsTest() {
        ageGroups[0] = Shelter.AgeRange.ALL.name();
        ageGroups[1] = Shelter.AgeRange.YOUNGADULTS.name();
        ageGroups[2] = Shelter.AgeRange.FAMILY.name();
        genders[0] = Shelter.Gender.MEN.name();
        genders[1] = Shelter.Gender.MEN.name();
        shelterALL.setAgeRange("ADULT");
        shelterALL.setChildrenAllowed(false);
        childrenAllowed = false;
        assertEquals(true,
                shelterALL.checkQualifications(ageGroups, genders, childrenAllowed));

    }
}