package com.example.fragment_test.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

import java.time.DayOfWeek;

public class RecipeWithScheduledId implements Parcelable {
    @Embedded
    public Recipe recipe;
    public int sRId;
    public int dayOfWeek;


    public RecipeWithScheduledId(Recipe recipe, int sRId, int dayOfWeek) {
        this.recipe = recipe;
        this.sRId = sRId;
        this.dayOfWeek = dayOfWeek;
    }

    protected RecipeWithScheduledId(Parcel in) {
        recipe = in.readParcelable(Recipe.class.getClassLoader());
        sRId = in.readInt();
        dayOfWeek = in.readInt();
    }

    public static final Creator<RecipeWithScheduledId> CREATOR = new Creator<RecipeWithScheduledId>() {
        @Override
        public RecipeWithScheduledId createFromParcel(Parcel in) {
            return new RecipeWithScheduledId(in);
        }

        @Override
        public RecipeWithScheduledId[] newArray(int size) {
            return new RecipeWithScheduledId[size];
        }
    };

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of(dayOfWeek);
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(recipe, i);
        parcel.writeInt(sRId);
        parcel.writeInt(dayOfWeek);
    }
}
