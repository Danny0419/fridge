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
    public int stepId;
    @ColumnInfo(name = "r_id")
    public int rId;
    @ColumnInfo
    public int stepNum;
    @ColumnInfo
    public String stepDetail;

    public Step(int stepId, int rId, int stepNum, String stepDetail) {
        this.stepId = stepId;
        this.rId = rId;
        this.stepNum = stepNum;
        this.stepDetail = stepDetail;
    }

    protected Step(Parcel in) {
        stepId = in.readInt();
        rId = in.readInt();
        stepNum = in.readInt();
        stepDetail = in.readString();
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

        parcel.writeInt(stepId);
        parcel.writeInt(rId);
        parcel.writeInt(stepNum);
        parcel.writeString(stepDetail);
    }

}
