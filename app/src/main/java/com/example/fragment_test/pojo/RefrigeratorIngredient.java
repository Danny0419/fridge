package com.example.fragment_test.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "refrigerator")
public class RefrigeratorIngredient extends Ingredient{
    @ColumnInfo
    public String img;
    @ColumnInfo
    public Integer savingDay;
    @ColumnInfo
    public String expiration;
    @ColumnInfo
    public Integer expired;

    public RefrigeratorIngredient(Integer id, String name, String img, Integer quantity, String sort, Integer savingDay, String expiration) {
        super(id, name, sort, quantity);
        this.img = img;
        this.savingDay = savingDay;
        this.expiration = expiration;
    }
}
