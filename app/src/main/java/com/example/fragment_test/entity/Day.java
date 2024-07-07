package com.example.fragment_test.entity;

import java.util.ArrayList;

public enum Day {
    MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"),
    FRIDAY("Friday"),SATURDAY("Saturday"),SUNDAY("Sunday");
    private String day;
    private ArrayList<Ingredient> ingredient;

    Day(String day) {
        this.day = day;
    }

    Day(String day, ArrayList<Ingredient> ingredient) {
        this.day = day;
        this.ingredient = ingredient;
    }

    public String getDay() {
        return day;
    }

    public ArrayList<Ingredient> getFood() {
        return ingredient;
    }

    public void setFood(ArrayList<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }
}
