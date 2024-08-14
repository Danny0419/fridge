package com.example.fragment_test.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @ColumnInfo
    public int serving;
    @ColumnInfo
    public int collected;

    @Ignore
    public List<Step> steps;
    @Ignore
    public List<RecipeIngredient> ingredients;

    public Recipe(int id, String name, String img, int serving, int collected) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.collected = collected;
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
    }

    @Ignore
    public Recipe(int id, String name, String img, int serving, int collected,List<Step> steps, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.collected = collected;
        this.steps = steps;
        this.ingredients = ingredients;
    }


    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        img = in.readString();
        serving = in.readInt();
        collected = in.readInt();
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(img);
        parcel.writeInt(serving);
        parcel.writeInt(collected);
        parcel.writeTypedList(steps);
    }


}
