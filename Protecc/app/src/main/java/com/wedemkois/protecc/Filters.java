package com.wedemkois.protecc;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wedemkois.protecc.model.Shelter;

public class Filters implements Parcelable {
    private String name = null;
    @Nullable
    private Shelter.Gender gender = null;
    @Nullable
    private Shelter.AgeRange ageRange = null;

    public Filters() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean hasName() { return !TextUtils.isEmpty(name); }

    @Nullable
    public Shelter.Gender getGender() { return gender; }
    public void setGender(@Nullable Shelter.Gender gender) { this.gender = gender; }
    public boolean hasGender() { return gender != null;}

    @Nullable
    public Shelter.AgeRange getAgeRange() { return ageRange; }
    public void setAgeRange(@Nullable Shelter.AgeRange ageRange) { this.ageRange = ageRange; }
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
