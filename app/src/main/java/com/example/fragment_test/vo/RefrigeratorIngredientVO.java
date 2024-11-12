package com.example.fragment_test.vo;

import androidx.room.Ignore;

public class RefrigeratorIngredientVO {
    public String name;
    public String sort;
    public String img;
    public int earlyEx;
    public int lastEx;
    public int sumQuantity;
    public String unit;

    public RefrigeratorIngredientVO(String name, String sort, String img, int earlyEx, int lastEx, int sumQuantity, String unit) {
        this.name = name;
        this.sort = sort;
        this.img = img;
        this.earlyEx = earlyEx;
        this.lastEx = lastEx;
        this.sumQuantity = sumQuantity;
        this.unit = unit;
    }

    @Ignore
    public RefrigeratorIngredientVO(String name, String sort, String img, int earlyEx, int lastEx, int sumQuantity) {
        this.name = name;
        this.sort = sort;
        this.img = img;
        this.earlyEx = earlyEx;
        this.lastEx = lastEx;
        this.sumQuantity = sumQuantity;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }

    public String getLastEx(){
        String stringLastEx = String.valueOf(lastEx);

        String year = stringLastEx.substring(2, 4);
        String month = stringLastEx.substring(4, 6);
        String day = stringLastEx.substring(6, 8);

        stringLastEx = year + "/" + month + "/" + day;
        return stringLastEx;
    }
}
