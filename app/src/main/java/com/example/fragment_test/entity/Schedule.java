package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules")
public class Schedule {
    @PrimaryKey
    public int date;
    @ColumnInfo(name = "day_of_week")
    public int dayOfWeek;
    @ColumnInfo
    public int status;

    public Schedule(int date, int dayOfWeek, int status) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.status = status;
    }
}
