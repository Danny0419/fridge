package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "preparedRecipes", foreignKeys =
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

    public PreparedRecipe(int id, Integer rId) {
        this.id = id;
        this.rId = rId;
    }
}
