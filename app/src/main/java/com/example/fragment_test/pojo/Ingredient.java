package com.example.fragment_test.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Ingredient implements Parcelable {
    private Integer id;
    private String name;
    private String sort;
    private String img;
    private String quantity;
    private Integer state;

    public Ingredient() {
    }

    public Ingredient(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public Ingredient(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.sort = source.readString();
        this.quantity = source.readString();
        this.state = source.readInt();
    }

    public Ingredient(Integer id, String name, String img, String sort, String quantity, Integer state) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.sort = sort;
        this.quantity = quantity;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(sort);
        dest.writeString(quantity);
        dest.writeInt(state);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
