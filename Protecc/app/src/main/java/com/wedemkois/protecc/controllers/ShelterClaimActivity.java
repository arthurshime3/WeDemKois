package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wedemkois.protecc.Filters;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;
import com.wedemkois.protecc.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelterClaimActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.sc_shelterName)
    TextView shelterNameTextView;

    @BindView(R.id.sc_claimBedsButton)
    Button claimBedsButton;

    @BindView(R.id.sc_name)
    TextView userName;

    @BindView(R.id.sc_numOfPeople)
    TextView numOfUsers;

    @BindView(R.id.sc_maleCheckBox)
    CheckBox maleCheckBox;

    @BindView(R.id.sc_femaleCheckBox)
    CheckBox femaleCheckBox;

    @BindView(R.id.sc_nonBinaryCheckBox)
    CheckBox nonBinaryCheckBox;

    @BindView(R.id.sc_childrenCheckBox)
    CheckBox childrenCheckBox;

    @BindView(R.id.sc_youngAdultCheckBox)
    CheckBox youngAdultCheckBox;

    @BindView(R.id.sc_adultCheckBox)
    CheckBox adultCheckBox;

    private Shelter currentShelter;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_claim);
        ButterKnife.bind(this);

        claimBedsButton.setOnClickListener(this);

    }

    // Don't know if I should have this method
    public void onShelterLoaded(Shelter shelter)
    {
        currentShelter = shelter;
        shelterNameTextView.setText(currentShelter.getName());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sc_claimBedsButton) {
            if (checkInput())
            {
                // beds successfully claimed
                Intent newIntent = new Intent(ShelterClaimActivity.this, ShelterDetailActivity.class);
                startActivity(newIntent);
            }
            else
            {
                //display warning
            }
        }
    }

    private boolean checkInput()
    {
        int gendersCheckedCount = 0, agesCheckedCount = 0;
        if (maleCheckBox.isChecked()) gendersCheckedCount++;
        if (femaleCheckBox.isChecked()) gendersCheckedCount++;
        if (nonBinaryCheckBox.isChecked()) gendersCheckedCount++;

        if (gendersCheckedCount == 0) return false;

        if (childrenCheckBox.isChecked()) agesCheckedCount++;
        if (youngAdultCheckBox.isChecked()) agesCheckedCount++;
        if (adultCheckBox.isChecked()) agesCheckedCount++;

        if (agesCheckedCount == 0) return false;

        if (userName.getText().toString().trim().isEmpty() || numOfUsers.getText().toString().trim().isEmpty())
            return false;

        if (nonBinaryCheckBox.isChecked())
            gendersCheckedCount--;
        String[] genders = new String[gendersCheckedCount];
        int i = 0;
        if (maleCheckBox.isChecked())
        {
            genders[i] = "Men";
            i++;
        }
        if (femaleCheckBox.isChecked())
        {
            genders[i] = "Women";
            i++;
        }

        boolean children = false;
        if (childrenCheckBox.isChecked())
        {
            agesCheckedCount--;
            children = true;
        }
        String[] ages = new String[agesCheckedCount];
        i = 0;
        if (youngAdultCheckBox.isChecked())
        {
            ages[i] = "Young adults";
            i++;
        }
        if (adultCheckBox.isChecked())
        {
            ages[i] = "Adult";
            i++;
        }

        int numOfPeople = Integer.parseInt(numOfUsers.getText().toString());
        return checkQualifications(ages, genders, children) && updateVacancy(numOfPeople, Math.abs(numOfPeople) != 1)[0];
    }

    private boolean checkQualifications(String[] ageGroup, String[] gender, boolean childrenAllowed) {
        if (!(currentShelter.getAgeRange().equals("All"))) {
            for (int i = 0; i < ageGroup.length; i++) {
                if (!(ageGroup[i].equals(currentShelter.getAgeRange()))) {
                    return false;
                }
            }
        }
        if (!(currentShelter.getGender().equals("Both"))) {
            for (int i = 0; i < gender.length; i++) {
                if (!(gender[i].equals(currentShelter.getGender()))) {
                    return false;
                }
            }
        }
        if (!(childrenAllowed == currentShelter.isChildrenAllowed())) return false;
        return true;
    }

    /*
    * Method that updates number of vacant beds at the shelter if possible.
    * @param users can be positive (checking in) or negative (checking out)
    * @param group true if a "group bed" is updating its vacancy
    * @return boolean array of length 2, with index 0 being true if check-in/out was valid, false if not
    *   and index 1 being true if the check in/out was a type group, false for type individual
    */
    private boolean[] updateVacancy(int users, boolean group) {
        if (users == 1 && group)
            throw new IllegalArgumentException("Error: A single user checking in is not a group");
        if (users > 1 && !group)
            throw new IllegalArgumentException("Error: More than one user checking in is a group");

        boolean[] output = {false, group};
        if (users == 0) {
            output[0] = true;
            return output;
        } else if (!group)  // 1 user checking in or any number checking out
        {
            int bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken());
            if (bedsTaken == 0 && users < 0) {
                output[0] = false;
                return output;
            }
            int vacancies = Integer.parseInt(currentShelter.getIndividualCapacity()) - bedsTaken;
            if (vacancies >= users) {
                currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                output[0] = true;
                return output;
            }
            else    // individual beds full, check group beds (this code is only reached for checking in)
            {
                bedsTaken = Integer.parseInt(currentShelter.getGroupBedsTaken());
                vacancies = Integer.parseInt(currentShelter.getGroupCapacity()) - bedsTaken;

                if (vacancies > 0)  // check for open group beds
                {
                    currentShelter.setGroupBedsTaken((Integer.parseInt(currentShelter.getGroupBedsTaken()) + 1) + "");
                    output[0] = true;
                    return output;
                }

                return output;  // no beds found
            }
        } else {    // group checking in or any number checking out
            output[1] = true;
            int bedsTaken = Integer.parseInt(currentShelter.getGroupBedsTaken());
            if (bedsTaken == 0 && users < 0) {
                output[0] = false;
                return output;
            }
            int vacancies = Integer.parseInt(currentShelter.getGroupCapacity()) - bedsTaken;
            if (vacancies >= users) {
                currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                output[0] = true;
                return output;
            }
            else    // group beds full, check individual beds (this code is only reached for checking in
            {
                bedsTaken = Integer.parseInt(currentShelter.getIndividualBedsTaken());
                vacancies = Integer.parseInt(currentShelter.getIndividualCapacity()) - bedsTaken;

                if (vacancies >= users)
                {
                    currentShelter.setIndividualBedsTaken((Integer.parseInt(currentShelter.getIndividualBedsTaken()) + users) + "");
                    output[0] = true;
                    return output;
                }

                return output;  // no beds found
            }
        }
    }
}
