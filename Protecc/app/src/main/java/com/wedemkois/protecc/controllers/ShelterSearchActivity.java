package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wedemkois.protecc.Filters;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelterSearchActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.shelterNameEditText)
    EditText shelterNameEditText;

    @BindView(R.id.genderRadioGroup)
    RadioGroup genderRadioGroup;

    @BindView(R.id.shelterSearchButton)
    Button shelterSearchButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_search);
        ButterKnife.bind(this);

        shelterSearchButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Filters createFiltersFromView() {
        Filters out = new Filters();
        out.setName(shelterNameEditText.getText().toString());
        out.setAgeRange(getSelectedAgeRange());
        out.setGender(getSelectedGender());
        return out;
    }

    private Shelter.Gender getSelectedGender() {
        int selectedButtonId = genderRadioGroup.getCheckedRadioButtonId();
        switch (selectedButtonId) {
            case R.id.maleRadio:
                return Shelter.Gender.MEN;
            case R.id.femaleRadio:
                return Shelter.Gender.WOMEN;
            case R.id.nonbinaryRadio:
                return Shelter.Gender.BOTH;
            default:
                return null;
        }
    }

    private Shelter.AgeRange getSelectedAgeRange() {
        if (((CheckBox) findViewById(R.id.familyCheckBox)).isChecked()) {
            return Shelter.AgeRange.FAMILY;
        } else if (((CheckBox) findViewById(R.id.adultCheckBox)).isChecked()) {
            return Shelter.AgeRange.ADULTS;
        } else if (((CheckBox) findViewById(R.id.yaCheckBox)).isChecked()) {
            return Shelter.AgeRange.YOUNGADULTS;
        }else if (((CheckBox) findViewById(R.id.anyoneCheckBox)).isChecked()) {
            return Shelter.AgeRange.ALL;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.shelterSearchButton) {
            Filters filters = createFiltersFromView();
            Intent newIntent = new Intent(ShelterSearchActivity.this, ShelterActivity.class);
            newIntent.putExtra("filter_parcel", filters);
            startActivity(newIntent);
        }
    }
}
