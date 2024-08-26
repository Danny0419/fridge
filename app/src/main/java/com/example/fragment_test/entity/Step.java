package com.example.fragment_test.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps", foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "r_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Step implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "r_id")
    public int rId;
    @ColumnInfo
    public int order;
    @ColumnInfo
    public String content;

    public Step(int id, int rId, int order, String content) {
        this.id = id;
        this.rId = rId;
        this.order = order;
        this.content = content;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        rId = in.readInt();
        order = in.readInt();
        content = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(rId);
        parcel.writeInt(order);
        parcel.writeString(content);
    }

}
