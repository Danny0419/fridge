package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String img;
    @ColumnInfo
    public int serving;
    @ColumnInfo(name = "s_id")
    public int SId;

    public Recipe(int id, String name, String img, int serving, int SId) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.serving = serving;
        this.SId = SId;
    }
}
