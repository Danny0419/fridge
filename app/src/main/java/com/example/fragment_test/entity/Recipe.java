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

@Entity(tableName = "recipes")
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

    @Ignore
    public Recipe(int id, String name, String img, int serving) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
    }

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

    public Recipe() {

    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        img = in.readString();
        serving = in.readInt();
        collected = in.readInt();
        steps = in.createTypedArrayList(Step.CREATOR);
        ingredients = in.createTypedArrayList(RecipeIngredient.CREATOR);
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
        parcel.writeTypedList(ingredients);
    }

    //每三行換一次行
    public StringBuilder setName() {
        StringBuilder formattedName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            formattedName.append(name.charAt(i));
            if ((i + 1) % 3 == 0 && i != name.length() - 1) {
                formattedName.append("\n");
            }
        }
        return formattedName;
    }
}
