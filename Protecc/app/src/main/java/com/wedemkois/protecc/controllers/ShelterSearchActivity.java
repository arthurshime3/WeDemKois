package com.wedemkois.protecc.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.util.Log;

import com.wedemkois.protecc.Filters;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShelterSearchActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.shelterNameEditText)
    EditText shelterNameEditText;

    @BindView(R.id.genderRadioGroup)
    RadioGroup genderRadioGroup;

    @BindView(R.id.ageRangeRadioGroup)
    RadioGroup ageRangeRadioGroup;

    @BindView(R.id.shelterSearchButton)
    Button shelterSearchButton;

    @BindView(R.id.mapSearchButton)
    Button mapSearchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelter_search);
        ButterKnife.bind(this);

        shelterSearchButton.setOnClickListener(this);
        mapSearchButton.setOnClickListener(this);

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
        int selectedButtonId = ageRangeRadioGroup.getCheckedRadioButtonId();
        switch (selectedButtonId) {
            case R.id.familyRadio:
                return Shelter.AgeRange.FAMILY;
            case R.id.adultRadio:
                return Shelter.AgeRange.ADULT;
            case R.id.yaRadio:
                return Shelter.AgeRange.YOUNGADULTS;
            case R.id.anyoneRadio:
                return Shelter.AgeRange.ALL;
            default:
                return null;
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.mapSearchButton) {
            Log.d("HELLO", "Map Search Button Clicked");
            Filters filters = createFiltersFromView();
            Intent newIntent = new Intent(ShelterSearchActivity.this, MapsActivity.class);
            newIntent.putExtra("filter_parcel", filters);
            startActivity(newIntent);
        } else if (i == R.id.shelterSearchButton) {
            Log.d("HELLO", "Shelter Search Button Clicked");
            Filters filters = createFiltersFromView();
            Intent newIntent = new Intent(ShelterSearchActivity.this, ShelterActivity.class);
            newIntent.putExtra("filter_parcel", filters);
            startActivity(newIntent);
        }
    }
}
