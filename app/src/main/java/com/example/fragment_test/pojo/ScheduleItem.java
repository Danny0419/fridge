package com.example.fragment_test.pojo;

public class ScheduleItem {
    public int id;
    public String dayOWeek;
    public Recipe recipe;

    public ScheduleItem(int id, String dayOWeek, Recipe recipe) {
        this.id = id;
        this.dayOWeek = dayOWeek;
        this.recipe = recipe;
    }
}
