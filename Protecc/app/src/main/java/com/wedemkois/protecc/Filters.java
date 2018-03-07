package com.wedemkois.protecc;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

public class Filters implements Parcelable {
    private String name = null;
    private Gender gender = null;
    private List<AgeRange> ageRanges = null;

    public enum Gender {
        MALE, FEMALE, NONBINARY
    }

    public enum AgeRange {
        NEWBORNS, CHILDREN, YOUNG_ADULTS, ANYONE
    }

    public Filters() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean hasName() { return !TextUtils.isEmpty(name); }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public boolean hasGender() { return gender != null;}

    public List<AgeRange> getAgeRanges() { return ageRanges; }
    public void setAgeRanges(List<AgeRange> ageRanges) { this.ageRanges = ageRanges; }
    public boolean hasAgeRanges() {return !(ageRanges==null || ageRanges.isEmpty()); }

    // Parcelization stuff

    Filters(Parcel p) {
        this.name = p.readString();
        int genderIndex = p.readInt();
        this.gender = genderIndex == -1 ? null : Filters.Gender.values()[genderIndex];
        p.readList(this.ageRanges, Filters.AgeRange.class.getClassLoader());
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
        parcel.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        parcel.writeList(ageRanges);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
