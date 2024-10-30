package com.example.fragment_test.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Invoice {
    @PrimaryKey
    @NonNull  // 主键必须为非空
    public String id;

    public String date;

    // 构造函数
    public Invoice(@NonNull String id, String date) {
        this.id = id;
        this.date = date;
    }

    // Getters 和 Setters
    @NonNull
    public String getId() { return id; }

    public void setId(@NonNull String id) { this.id = id; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
