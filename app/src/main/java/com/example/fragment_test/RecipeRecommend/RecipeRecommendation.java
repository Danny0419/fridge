package com.example.fragment_test.RecipeRecommend;

import android.content.Context;
import android.util.Log;

import com.example.fragment_test.ServerAPI.ApiService;
import com.example.fragment_test.ServerAPI.Recipe;
import com.example.fragment_test.ServerAPI.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRecommendation {

    private List<Recipe> recipes;
    private Context context;
    private static final Set<String> SEASONINGS = new HashSet<>(Arrays.asList("鹽", "油", "醬油", "蠔油", "米酒", "香油", "烏醋", "糖", "胡椒粉"));

    // 无参构造函数
    public RecipeRecommendation() {
        this.recipes = new ArrayList<>();
    }

    // 初始化方法，调用API获取食谱数据
    public void init(Context context, InitCallback recommendationActivity) {
        this.context = context;
        loadRecipesFromAPI(); // 从API加载食谱
    }

    // 从API加载食谱
    private void loadRecipesFromAPI() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipes = response.body();
                    preprocessRecipes();
                } else {
                    Log.e("RecipeRecommendation", "API响应错误");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("RecipeRecommendation", "加载食谱失败: " + t.getMessage(), t); // 增加日志打印错误堆栈
            }
        });
    }

    // 预处理食谱
    private void preprocessRecipes() {
        recipes.forEach(this::preprocessRecipe);
        Log.d("RecipeRecommendation", "预处理后的食谱数量：" + recipes.size());
    }

    // 处理食谱信息
    private void preprocessRecipe(Recipe recipe) {
        // 这里可以进一步处理API返回的食谱信息，如提取或转换字段
    }

    // 获取冰箱中的食材
    public Map<String, FridgeIngredient> getFridgeIngredients() {
        Calendar today = Calendar.getInstance();
        Map<String, FridgeIngredient> ingredients = new HashMap<>();

        // 模拟冰箱中的食材
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

    // 获取推荐食谱
    public List<Recommendation> getRecommendations(Map<String, FridgeIngredient> fridgeIngredients) {
        return recipes.stream()
                .map(recipe -> new Recommendation(recipe, calculateScores(recipe, fridgeIngredients)))
                .sorted(Comparator.comparing(Recommendation::getCombinedScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public interface InitCallback {
        void onSuccess(Map<String, FridgeIngredient> fridgeIngredients);

        void onError(String errorMessage);
    }

    // 计算评分
    private Map<String, Float> calculateScores(Recipe recipe, Map<String, FridgeIngredient> fridgeIngredients) {
        long matchedIngredientsCount = recipe.getIngredients().stream()
                .filter(ingredient -> {
                    if (SEASONINGS.contains(ingredient.getIngredient_name())) return false;
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getIngredient_name());
                    return fridgeIngredient != null && Float.parseFloat(ingredient.getIngredient_need()) <= fridgeIngredient.getQuantity();
                })
                .count();

        double rareIngredientWeight = recipe.getIngredients().stream()
                .filter(ingredient -> {
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getIngredient_name());
                    return fridgeIngredient != null && Float.parseFloat(ingredient.getIngredient_need()) <= fridgeIngredient.getQuantity();
                })
                .mapToDouble(ingredient -> calculateIngredientWeight(ingredient.getIngredient_name()))
                .sum();

        float matchRate = (float) (matchedIngredientsCount * rareIngredientWeight) /
                recipe.getIngredients().stream().filter(ingredient -> !SEASONINGS.contains(ingredient.getIngredient_name())).count();
        float matchScore = matchRate * 3;

        float totalExpiryScore = (float) recipe.getIngredients().stream()
                .filter(ingredient -> !SEASONINGS.contains(ingredient.getIngredient_name()))
                .mapToInt(ingredient -> {
                    FridgeIngredient fridgeIngredient = fridgeIngredients.get(ingredient.getIngredient_name());
                    return fridgeIngredient != null ? calculateExpiryScore(fridgeIngredient.getExpiryDays()) : 0;
                })
                .sum();

        float averageExpiryScore = matchedIngredientsCount > 0 ? totalExpiryScore / matchedIngredientsCount : 0;
        float smoothedExpiryScore = smoothFactor(averageExpiryScore, matchedIngredientsCount);

        float combinedScore = (float) ((matchScore * 2.5f) + smoothedExpiryScore + rareIngredientWeight * 1.2f);

        Map<String, Float> scores = new HashMap<>();
        scores.put("matchScore", matchScore);
        scores.put("expiryScore", smoothedExpiryScore);
        scores.put("combinedScore", combinedScore);

        return scores;
    }

    private float smoothFactor(float score, long ingredientCount) {
        return (float) (score / Math.sqrt(ingredientCount + 1));
    }

    private double calculateIngredientWeight(String ingredientName) {
        Map<String, Double> rarityMap = new HashMap<>();
        rarityMap.put("魚子", 1.5);
        rarityMap.put("松露", 2.0);
        rarityMap.put("牛肝菌", 1.7);
        return rarityMap.getOrDefault(ingredientName, 1.0);
    }

    private int calculateExpiryScore(long daysRemaining) {
        if (daysRemaining > 7) return 1;
        if (daysRemaining > 3) return 2;
        return 3;
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

        public Recipe getRecipe() { return recipe; }
        public float getMatchScore() { return matchScore; }
        public float getExpiryScore() { return expiryScore; }
        public float getCombinedScore() { return combinedScore; }
    }
}
