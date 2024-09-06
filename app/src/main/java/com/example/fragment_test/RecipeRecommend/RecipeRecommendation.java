package com.example.fragment_test.RecipeRecommend;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RecipeRecommendation {

    private List<Recipe> recipes;
    private Context context;
    private static final Set<String> SEASONINGS = new HashSet<>(Arrays.asList("鹽", "油", "醬油", "蠔油", "米酒", "香油", "烏醋", "糖", "胡椒粉"));

    // 無參數構造函數
    public RecipeRecommendation() {
        this.recipes = new ArrayList<>();
    }

    // 初始化方法
    public void init(Context context, String recipesFile) {
        this.context = context;
        this.recipes = loadRecipes(recipesFile);
        preprocessRecipes();
    }

    private List<Recipe> loadRecipes(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = context.getAssets().open(filePath)) {
            List<Map<String, Object>> rawRecipes = mapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            return rawRecipes.stream().map(Recipe::new).collect(Collectors.toList());
        } catch (IOException e) {
            Log.e("RecipeRecommendation", "Error loading recipes", e);
            return new ArrayList<>();
        }
    }

    private void preprocessRecipes() {
        recipes.forEach(Recipe::preprocess);
        Log.d("RecipeRecommendation", "預處理後的食譜數量：" + recipes.size());
    }

    public Map<String, FridgeIngredient> getFridgeIngredients() {
        Calendar today = Calendar.getInstance();
        Map<String, FridgeIngredient> ingredients = new HashMap<>();

        ingredients.put("雞蛋", new FridgeIngredient("雞蛋", 10, today, 10));
        ingredients.put("牛奶", new FridgeIngredient("牛奶", 2000, today, 5));
        ingredients.put("雞胸肉", new FridgeIngredient("雞胸肉", 500, today, 3));
        ingredients.put("牛肉", new FridgeIngredient("牛肉", 1000, today, 7));
        ingredients.put("豬肉", new FridgeIngredient("豬肉", 800, today, 4));
        ingredients.put("魚片", new FridgeIngredient("魚片", 300, today, 2));
        ingredients.put("蝦仁", new FridgeIngredient("蝦仁", 200, today, 2));
        ingredients.put("豆腐", new FridgeIngredient("豆腐", 400, today, 3));
        ingredients.put("麵條", new FridgeIngredient("麵條", 500, today, 180));
        ingredients.put("米飯", new FridgeIngredient("米飯", 1000, today, 2));
        ingredients.put("馬鈴薯", new FridgeIngredient("馬鈴薯", 500, today, 30));
        ingredients.put("紅蘿蔔", new FridgeIngredient("紅蘿蔔", 300, today, 20));
        ingredients.put("洋蔥", new FridgeIngredient("洋蔥", 200, today, 15));
        ingredients.put("花椰菜", new FridgeIngredient("花椰菜", 400, today, 7));
        ingredients.put("菠菜", new FridgeIngredient("菠菜", 250, today, 5));
        ingredients.put("青椒", new FridgeIngredient("青椒", 200, today, 10));
        ingredients.put("玉米", new FridgeIngredient("玉米", 300, today, 15));
        ingredients.put("茄子", new FridgeIngredient("茄子", 300, today, 7));
        ingredients.put("豆芽", new FridgeIngredient("豆芽", 200, today, 3));
        ingredients.put("蘑菇", new FridgeIngredient("蘑菇", 150, today, 5));
        ingredients.put("牛蒡", new FridgeIngredient("牛蒡", 100, today, 7));
        ingredients.put("蒟蒻", new FridgeIngredient("蒟蒻", 200, today, 14));

        return ingredients;
    }

    public List<Recommendation> getRecommendations(Map<String, FridgeIngredient> fridgeIngredients) {
        return recipes.stream()
                .map(recipe -> new Recommendation(recipe, calculateScores(recipe, fridgeIngredients)))
                .sorted(Comparator.comparing(Recommendation::getCombinedScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<String, Float> calculateScores(Recipe recipe, Map<String, FridgeIngredient> fridgeIngredients) {
        // 匹配的食材數量和計算權重匹配率
        long matchedIngredientsCount = recipe.getIngredients().stream()
                .filter(ingredient -> {
                    if (SEASONINGS.contains(ingredient.getName())) return false;
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getName());
                    return fridgeIngredient != null && ingredient.getQuantity() <= fridgeIngredient.getQuantity();
                })
                .count();

        // 计算稀有食材匹配权重
        double rareIngredientWeight = recipe.getIngredients().stream()
                .filter(ingredient -> {
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getName());
                    return fridgeIngredient != null && ingredient.getQuantity() <= fridgeIngredient.getQuantity();
                })
                .mapToDouble(ingredient -> calculateIngredientWeight(ingredient.getName()))
                .sum();

        // 匹配率公式：考慮了食材數量、稀有度、總量歸一化等
        float matchRate = (float) (matchedIngredientsCount * rareIngredientWeight) /
                recipe.getIngredients().stream().filter(ingredient -> !SEASONINGS.contains(ingredient.getName())).count();
        float matchScore = matchRate * 3;  // 擴展權重調節因子

        // 計算食材保質期總分與平滑新鮮度得分
        float totalExpiryScore = (float) recipe.getIngredients().stream()
                .filter(ingredient -> !SEASONINGS.contains(ingredient.getName())) // 排除調味料
                .mapToInt(ingredient -> {
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getName());
                    return fridgeIngredient != null ? calculateExpiryScore(fridgeIngredient.getExpiryDays()) : 0;
                })
                .sum();

        // 平滑因子影響新鮮度得分
        float averageExpiryScore = (float) (matchedIngredientsCount > 0 ? totalExpiryScore / matchedIngredientsCount : 0);
        float smoothedExpiryScore = smoothFactor(averageExpiryScore, matchedIngredientsCount); // 增加平滑因子

        // 綜合分數: 更加複雜的權重配分，考慮多個因素
        float combinedScore = (float) ((matchScore * 2.5f) + smoothedExpiryScore + rareIngredientWeight * 1.2f);

        Map<String, Float> scores = new HashMap<>();
        scores.put("matchScore", matchScore);
        scores.put("expiryScore", smoothedExpiryScore);
        scores.put("combinedScore", combinedScore);

        return scores;
    }

    // 新增平滑因子函数
    private float smoothFactor(float score, long ingredientCount) {
        // 平滑處理，根據食材數量以及匹配權重調整
        return (float) (score / Math.sqrt(ingredientCount + 1));
    }

    // 计算稀有食材的权重
    private double calculateIngredientWeight(String ingredientName) {
        // 稀有度數據可能來自外部的食材稀有度資料庫
        Map<String, Double> rarityMap = new HashMap<>();
        rarityMap.put("魚子", 1.5);
        rarityMap.put("松露", 2.0);
        rarityMap.put("牛肝菌", 1.7);
        // 默認稀有度為1
        return rarityMap.getOrDefault(ingredientName, 1.0);
    }



    // 期限配分
    private int calculateExpiryScore(long daysRemaining) {
        if (daysRemaining > 7) return 1;
        if (daysRemaining > 3) return 2;
        return 3;
    }

    // 內部類
    public static class Recipe {
        private String name;
        private String difficulty;
        private int totalTime;
        private int servings;
        private List<Ingredient> ingredients;
        private float calories;
        private float rating;

        public Recipe(Map<String, Object> rawRecipe) {
            this.name = (String) rawRecipe.get("標題");
            this.difficulty = (String) rawRecipe.get("難易度");
            this.totalTime = extractFirstInt((String) rawRecipe.get("總共時間"));
            this.servings = extractFirstInt((String) rawRecipe.get("份量"));
            this.ingredients = processIngredients((List<String>) rawRecipe.get("食材"));
            this.calories = extractFirstFloat(((Map<String, String>) rawRecipe.get("營養價值")).get("卡路里"));
            this.rating = processRating(rawRecipe.get("評分"));
        }

        public void preprocess() {
            this.difficulty = this.difficulty.split(" ")[this.difficulty.split(" ").length - 1];
        }

        // Getters
        public String getName() { return name; }
        public String getDifficulty() { return difficulty; }
        public int getTotalTime() { return totalTime; }
        public int getServings() { return servings; }
        public List<Ingredient> getIngredients() { return ingredients; }
        public float getCalories() { return calories; }
        public float getRating() { return rating; }
    }

    public static class Ingredient {
        private String name;
        private float quantity;

        public Ingredient(String name, float quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        // Getters
        public String getName() { return name; }
        public float getQuantity() { return quantity; }
    }

    public static class FridgeIngredient {
        private String name;
        private float quantity;
        private long expiryDays;

        public FridgeIngredient(String name, float quantity, Calendar today, int daysToAdd) {
            this.name = name;
            this.quantity = quantity;
            Calendar expiryDate = (Calendar) today.clone();
            expiryDate.add(Calendar.DAY_OF_YEAR, daysToAdd);
            this.expiryDays = (expiryDate.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        }

        // Getters
        public String getName() { return name; }
        public float getQuantity() { return quantity; }
        public long getExpiryDays() { return expiryDays; }
    }

    public static class Recommendation {
        private Recipe recipe;
        private float matchScore;
        private float expiryScore;
        private float combinedScore;

        public Recommendation(Recipe recipe, Map<String, Float> scores) {
            this.recipe = recipe;
            this.matchScore = scores.get("matchScore");
            this.expiryScore = scores.get("expiryScore");
            this.combinedScore = scores.get("combinedScore");
        }

        // Getters
        public Recipe getRecipe() { return recipe; }
        public float getMatchScore() { return matchScore; }
        public float getExpiryScore() { return expiryScore; }
        public float getCombinedScore() { return combinedScore; }
    }

    // Utility methods
    private static int extractFirstInt(String text) {
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
    }

    private static float extractFirstFloat(String text) {
        Matcher matcher = Pattern.compile("\\d+(\\.\\d+)?").matcher(text);
        return matcher.find() ? Float.parseFloat(matcher.group()) : 0.0f;
    }

    private static List<Ingredient> processIngredients(List<String> rawIngredients) {
        return rawIngredients.stream()
                .map(item -> {
                    String[] parts = item.trim().split(" ");
                    if (parts.length > 1) {
                        try {
                            float quantity = Float.parseFloat(parts[0]);
                            String ingredientName = parts[parts.length - 1];
                            if (!SEASONINGS.contains(ingredientName)) {
                                return new Ingredient(ingredientName, quantity);
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static float processRating(Object rating) {
        try {
            return extractFirstFloat(rating.toString());
        } catch (Exception e) {
            return 0.0f;
        }
    }
}