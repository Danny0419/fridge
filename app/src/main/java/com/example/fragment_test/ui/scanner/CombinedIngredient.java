package com.example.fragment_test.ui.scanner;

public class CombinedIngredient {
    private int Supermarket_ingredient_ID;// 超市商品的唯一識別 ID
    private int Ingredient_ID;// 原始成分的唯一識別 ID
    private String supermarket_ingredientcol_name;// 超市商品的名稱或標籤名稱
    private String unit;// 商品的計量單位（例如：kg、g、包裝等）
    private String Grams;// 商品的重量或克數，以字符串形式存儲（例如："500g"）
    private String Ingredient_Name; // 成分名稱，用於表明商品的原始成分名稱
    private String Ingredients_category;// 成分類別，表示此成分所屬的類別（例如：蔬菜、水果、肉類等）
    private int expiration;// 商品的保存期限或過期日期
    private String Ingredient_pictures;

    // Getter 和 Setter 方法
    public int getSupermarket_ingredient_ID() {
        return Supermarket_ingredient_ID;
    }

    public void setSupermarket_ingredient_ID(int supermarket_ingredient_ID) {
        this.Supermarket_ingredient_ID = supermarket_ingredient_ID;
    }

    public int getIngredient_ID() {
        return Ingredient_ID;
    }

    public void setIngredient_ID(int ingredient_ID) {
        this.Ingredient_ID = ingredient_ID;
    }

    public String getSupermarket_ingredientcol_name() {
        return supermarket_ingredientcol_name;
    }

    public void setSupermarket_ingredientcol_name(String supermarket_ingredientcol_name) {
        this.supermarket_ingredientcol_name = supermarket_ingredientcol_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGrams() {
        return Grams;
    }

    public void setGrams(String grams) {
        this.Grams = grams;
    }

    public String getIngredient_Name() {
        return Ingredient_Name;
    }

    public void setIngredient_Name(String ingredient_Name) {
        this.Ingredient_Name = ingredient_Name;
    }

    public String getIngredients_category() {
        return Ingredients_category;
    }

    public void setIngredients_category(String ingredients_category) {
        this.Ingredients_category = ingredients_category;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = Integer.parseInt(expiration);
    }

    public String getIngredient_pictures() {
        return Ingredient_pictures;
    }
}
