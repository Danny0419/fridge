package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "prepared_recipes", foreignKeys =
@ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "r_id")
)
public class PreparedRecipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "r_id")
    public Integer rId;
    @ColumnInfo
    public String src;
    @ColumnInfo(name = "s_r_id")
    public Integer sRId;

    @ColumnInfo
    public int scheduled;

    public PreparedRecipe(int id, Integer rId, String src, int scheduled) {
        this.id = id;
        this.rId = rId;
        this.src = src;
        this.scheduled = scheduled;
    }

    @Ignore


    public PreparedRecipe(int id, Integer rId, String src, Integer sRId, int scheduled) {
        this.id = id;
        this.rId = rId;
        this.src = src;
        this.sRId = sRId;
        this.scheduled = scheduled;
    }
}
