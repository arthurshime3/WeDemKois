package com.wedemkois.protecc;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wedemkois.protecc.model.Shelter;

/**
 * Class that handles filtering of the shelters for use in searching
 */
public class Filters implements Parcelable {
    private String name = null;
    @Nullable
    private Shelter.Gender gender = null;
    @Nullable
    private Shelter.AgeRange ageRange = null;

    /**
     * Default no-arg constructor
     */
    public Filters() {}

    /**
     * Gets name
     * @return name
     */
    public String getName() { return name; }

    /**
     * Sets name
     * @param name new name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Checks if filter has name set
     * @return true if name is not an empty string
     */
    public boolean hasName() { return !TextUtils.isEmpty(name); }

    /**
     * Gets gender restriction
     * @return gender restriction
     */
    @Nullable
    public Shelter.Gender getGender() { return gender; }

    /**
     * Sets gender restriction
     * @param gender new gender restriction
     */
    public void setGender(@Nullable Shelter.Gender gender) { this.gender = gender; }

    /**
     * Checks if filter has gender restrictions
     * @return true if gender is non-null
     */
    public boolean hasGender() { return gender != null;}

    /**
     * Gets age range
     * @return age range
     */
    @Nullable
    public Shelter.AgeRange getAgeRange() { return ageRange; }

    /**
     * Sets age range restrictions
     * @param ageRange new age range
     */
    public void setAgeRange(@Nullable Shelter.AgeRange ageRange) { this.ageRange = ageRange; }

    /**
     * Checks if filter has age range restrictions set
     * @return true if ageRange is non-null
     */
    public boolean hasAgeRange() {return ageRange!=null; }

    // Parcelization stuff

    private Filters(Parcel p) {
        this.name = p.readString();
        int genderIndex = p.readInt();
        this.gender = (genderIndex == -1) ? null : Shelter.Gender.values()[genderIndex];
        int ageRangeIndex = p.readInt();
        this.ageRange = (ageRangeIndex == -1) ? null : Shelter.AgeRange.values()[ageRangeIndex];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Filters(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Object[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt((this.gender == null) ? -1 : this.gender.ordinal());
        parcel.writeInt((this.ageRange == null) ? -1 : this.ageRange.ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
