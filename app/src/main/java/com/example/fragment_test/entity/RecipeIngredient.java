package com.example.fragment_test.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "recipe_needs",
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id", childColumns = "r_id",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class RecipeIngredient extends Ingredient implements Parcelable {
    @ColumnInfo
    public String img;
    @ColumnInfo(name = "r_id")
    public Integer rId;

    @Ignore
    public RecipeIngredient(@NonNull String name, int quantity) {
        super(name, quantity);
    }

    @Ignore
    public RecipeIngredient(@NonNull String name, int quantity, String img, Integer rId) {
        super(name, quantity);
        this.img = img;
        this.rId = rId;
    }

    public RecipeIngredient(Integer id, @NonNull String name, int quantity, String img, Integer rId) {
        super(id, name, quantity);
        this.img = img;
        this.rId = rId;
    }

    protected RecipeIngredient(Parcel in) {
        super(in);
        img = in.readString();
        if (in.readByte() == 0) {
            rId = null;
        } else {
            rId = in.readInt();
        }
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(img);
        if (rId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(rId);
        }
    }
}
