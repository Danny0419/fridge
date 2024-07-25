package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.DayOfWeek;

@Entity(tableName = "schedule")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "day_of_week")
    public int dayOfWeek;
    @ColumnInfo
    public String date;
    @ColumnInfo
    public int status;

    public Schedule(int id, int dayOfWeek, String date, int status) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.status = status;
    }
}
