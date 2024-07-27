package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "step", foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "r_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Step {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "r_id")
    public int rId;
    @ColumnInfo
    public String content;

    public Step(int rId, String content) {
        this.rId = rId;
        this.content = content;
    }
}
