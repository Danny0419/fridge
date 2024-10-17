package com.example.fragment_test.ScannerList;

public class Item {
    private String name;
    private String quantity;
    private String amount;
    private String changedName;  // 新增轉換後的商品名稱
    private String expiration;   // 新增保存期限
    private String Ingredient_Name;
    private String Ingredients_category;
    public Item(String name, String quantity, String amount, String changedName, String expiration) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
        this.changedName = changedName;  // 初始化轉換後的商品名稱
        this.expiration = expiration;    // 初始化保存期限
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) { // 添加 setAmount 方法
        this.amount = amount;
    }

    public void setQuantity(String quantity) { // 添加 setQuantity 方法
        this.quantity = quantity;
    }

    public String getChangedName() {
        return changedName;   // 新增 getter
    }

    public String getExpiration() {
        return expiration;    // 新增 getter
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getIngredient_Name() {
        return Ingredient_Name;
    }
    public String getIngredients_category() {
        return Ingredients_category;
    }

    public void setIngredients_category(String ingredients_category) {
        Ingredients_category = ingredients_category;
    }
}
