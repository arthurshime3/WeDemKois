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
        out.setAgeRanges(getSelectedAgeRange());
        out.setGender(getSelectedGender());
        return out;
    }

    private Filters.Gender getSelectedGender() {
        int selectedButtonId = genderRadioGroup.getCheckedRadioButtonId();
        switch (selectedButtonId) {
            case R.id.maleRadio:
                return Filters.Gender.MALE;
            case R.id.femaleRadio:
                return Filters.Gender.FEMALE;
            case R.id.nonbinaryRadio:
                return Filters.Gender.NONBINARY;
            default:
                return null;
        }
    }

    private List<Filters.AgeRange> getSelectedAgeRange() {
        List<Filters.AgeRange> out = new ArrayList<>();
        if (((CheckBox) findViewById(R.id.childrenCheckBox)).isChecked()) {
            out.add(Filters.AgeRange.CHILDREN);
        }
        if (((CheckBox) findViewById(R.id.familyCheckBox)).isChecked()) {
            out.add(Filters.AgeRange.NEWBORNS);
        }
        if (((CheckBox) findViewById(R.id.yaCheckBox)).isChecked()) {
            out.add(Filters.AgeRange.YOUNG_ADULTS);
        }
        if (((CheckBox) findViewById(R.id.anyoneCheckBox)).isChecked()) {
            out.add(Filters.AgeRange.ANYONE);
        }
        return out;
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
