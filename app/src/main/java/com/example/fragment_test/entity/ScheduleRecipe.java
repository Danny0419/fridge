package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.DayOfWeek;

@Entity(tableName = "schedule_recipes", foreignKeys =
        {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "r_id",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        })
public class ScheduleRecipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "r_id")
    public Integer rid;
    @ColumnInfo
    public int date;
    @ColumnInfo(name = "day_of_week")
    public int dayOfWeek;
    @ColumnInfo(defaultValue = "0")
    public int status;
    @Ignore
    Recipe recipe;

    public ScheduleRecipe(int id, Integer rid, int date, int dayOfWeek, int status) {
        this.id = id;
        this.rid = rid;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.status = status;
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of(dayOfWeek);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
