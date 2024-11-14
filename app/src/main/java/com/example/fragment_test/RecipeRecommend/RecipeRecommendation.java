package com.example.fragment_test.RecipeRecommend;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.fragment_test.ServerAPI.ApiService;
import com.example.fragment_test.ServerAPI.Recipe;
import com.example.fragment_test.ServerAPI.RetrofitClient;
import com.example.fragment_test.repository.RefrigeratorIngredientRepository;
import com.example.fragment_test.vo.RefrigeratorIngredientVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRecommendation {
    private static final String TAG = "RecipeRecommendation";
    private static final int MAX_RETRIES = 3;  // 最大重試次數
    private static final int RETRY_DELAY_MS = 1000;
    private static final int COOKING_TIME_THRESHOLD = 60; // 分鐘
    private static final float MATCH_WEIGHT = 1.5f;
    private static final float EXPIRY_WEIGHT = 1.2f;
    private static final float TIME_PENALTY_WEIGHT = 0.5f;

    private static final Set<String> SEASONINGS = new HashSet<>(Arrays.asList(
            "鹽", "油", "醬油", "蠔油", "米酒", "香油", "烏醋", "糖", "胡椒粉"
    ));

    private List<Recipe> recipes;
    private Context context;
    private RefrigeratorIngredientRepository repository;

    // 建構函數
    public RecipeRecommendation() {
        this.recipes = new ArrayList<>();
    }

    // 初始化方法
    public void init(Context context, InitCallback callback) {
        this.context = context;
        this.repository = RefrigeratorIngredientRepository.getInstance(context);
        loadRecipesAndIngredients(callback);
    }

    // 載入食譜和食材數據
    private void loadRecipesAndIngredients(InitCallback callback) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        attemptLoadRecipes(apiService, callback, 0);
    }

    private void attemptLoadRecipes(ApiService apiService, InitCallback callback, int retryCount) {
        Call<List<Recipe>> call = apiService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipes = response.body();
                    loadFridgeIngredients(callback);
                    Log.i(TAG, "Successfully loaded " + recipes.size() + " recipes");
                } else {
                    handleLoadError(apiService, callback, retryCount,
                            new Exception("API response not successful: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Recipe loading failed: " + t.getMessage(), t);
                handleLoadError(apiService, callback, retryCount, t);
            }
        });
    }

    private void handleLoadError(ApiService apiService, InitCallback callback,
                                 int retryCount, Throwable error) {
        if (retryCount < MAX_RETRIES) {
            Log.w(TAG, "Retrying recipe load attempt " + (retryCount + 1) +
                    " of " + MAX_RETRIES);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                attemptLoadRecipes(apiService, callback, retryCount + 1);
            }, RETRY_DELAY_MS);
        } else {
            Log.e(TAG, "All retry attempts failed", error);
            callback.onError("載入食譜失敗: " + error.getMessage() +
                    "\n請檢查網路連接並重試");
        }
    }

    // 載入冰箱食材
    private void loadFridgeIngredients(InitCallback callback) {
        Maybe.fromCallable(() -> repository.getRefrigeratorIngredientsSortedBySort())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Map<String, List<RefrigeratorIngredientVO>>>() {
                    @Override
                    public void onSuccess(Map<String, List<RefrigeratorIngredientVO>> ingredientMap) {
                        Map<String, FridgeIngredient> convertedIngredients = convertToFridgeIngredients(ingredientMap);
                        callback.onSuccess(convertedIngredients);
                        Log.i("TAG", convertedIngredients.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError("載入冰箱食材失敗: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        callback.onError("未找到冰箱食材");
                    }
                });
    }

    // 轉換食材格式
    private Map<String, FridgeIngredient> convertToFridgeIngredients(
            Map<String, List<RefrigeratorIngredientVO>> ingredientMap) {
        Map<String, FridgeIngredient> result = new HashMap<>();
        Calendar today = Calendar.getInstance();

        for (List<RefrigeratorIngredientVO> ingredients : ingredientMap.values()) {
            for (RefrigeratorIngredientVO vo : ingredients) {
                result.put(vo.getName(), new FridgeIngredient(
                        vo.getName(),
                        vo.sumQuantity,
                        today,
                        calculateDaysToExpiry(today.getTime())
                ));
            }
        }
        return result;
    }

    // 計算到期天數
    private int calculateDaysToExpiry(Date expirationDate) {
        if (expirationDate == null) return Integer.MAX_VALUE;
        return (int) ((expirationDate.getTime() - System.currentTimeMillis())
                / (1000 * 60 * 60 * 24));
    }

    // 獲取推薦食譜
    public List<Recommendation> getRecommendations(Map<String, FridgeIngredient> fridgeIngredients) {
        return recipes.stream()
                .map(recipe -> new Recommendation(recipe, calculateScores(recipe, fridgeIngredients)))
                .sorted(Comparator.comparing(Recommendation::getCombinedScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // 計算評分
    private Map<String, Float> calculateScores(Recipe recipe,
                                               Map<String, FridgeIngredient> fridgeIngredients) {
        List<IngredientMatch> matches = calculateIngredientMatches(recipe, fridgeIngredients);
        float matchScore = calculateMatchScore(matches, recipe);
        float expiryScore = calculateExpiryScore(matches);

        long machedIngredientCount = matches.stream()
                .filter(match -> match.matchScore > 0) // 有匹配到的食材
                .count();

        int requiredIngredientCount = (int) recipe.getIngredients().stream()
                .filter(ingredient -> !SEASONINGS.contains(ingredient.getIngredient_name()))
                .count();

        float finalScore = calculateFinalScore(
                matchScore,
                expiryScore,
                recipe.getCooking_time(),
                "普通",
                (int) machedIngredientCount,
                requiredIngredientCount
        );
//        Log.i("TAG","requierIng"+requiredIngredientCount);
        Map<String, Float> scores = new HashMap<>();
        scores.put("matchScore", matchScore);
        scores.put("expiryScore", expiryScore);
        scores.put("combinedScore", finalScore);
        return scores;
    }

    private float parseIngredientAmount(String amount) {
        // 去除所有的非数字和小数点字符
        String numericValue = amount.replaceAll("[^\\d.]", "");
        try {
            return Float.parseFloat(numericValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f; // 默认返回0
        }
    }

    // 計算食材匹配情況
    private List<IngredientMatch> calculateIngredientMatches(Recipe recipe,
                                                             Map<String, FridgeIngredient> fridgeIngredients) {
        return recipe.getIngredients().stream()
                .filter(ingredient -> !SEASONINGS.contains(ingredient.getIngredient_name()))
                .map(ingredient -> {
                    FridgeIngredient fridgeIngredient =
                            fridgeIngredients.get(ingredient.getIngredient_name());
                    Log.i("TAG","category"+ingredient.getIngredient_category());
                    if (fridgeIngredient == null) {
                        return new IngredientMatch(ingredient.getIngredient_name(), 0, 0, 0);
                    }

                    // 使用 parseIngredientAmount 方法来解析所需数量
                    float neededAmount = parseIngredientAmount(ingredient.getIngredient_need());
                    float availableAmount = fridgeIngredient.getQuantity();
                    float ratio = availableAmount / neededAmount;
                    float matchScore = calculateMatchScoreByRatio(ratio);

                    return new IngredientMatch(
                            ingredient.getIngredient_name(),
                            matchScore,
                            fridgeIngredient.getExpiryDays(),
                            ratio
                    );
                })
                .collect(Collectors.toList());
    }


    // 根據比例計算匹配分數
    private float calculateMatchScoreByRatio(float ratio) {
        if (ratio >= 1.0) return 2.0f;
        if (ratio >= 0.8) return 1.6f;
        if (ratio >= 0.6) return 1.2f;
        if (ratio >= 0.4) return 0.8f;
        if (ratio >= 0.2) return 0.4f;
        return 0f;
    }

    // 計算總匹配分數
    private float calculateMatchScore(List<IngredientMatch> matches, Recipe recipe) {
        float totalScore = matches.stream()
                .map(match -> match.matchScore)
                .reduce(0f, Float::sum);

        int totalRequiredIngredients = (int) recipe.getIngredients().stream()
                .filter(i -> !SEASONINGS.contains(i.getIngredient_name()))
                .count();

        return totalRequiredIngredients > 0 ? totalScore / totalRequiredIngredients : 0;
    }

    // 計算過期分數
    private float calculateExpiryScore(List<IngredientMatch> matches) {
        if (matches.isEmpty()) return 0;
        return matches.stream()
                .map(match -> calculateExpiryScoreByDays(match.expiryDays))
                .reduce(0f, Float::sum) / matches.size();
    }

    // 根據剩餘天數計算過期分數
    private float calculateExpiryScoreByDays(long days) {
//        Log.i("TAG", String.valueOf(days));
        if (days <= 1) return 2.0f;
        if (days <= 3) return 1.6f;
        if (days <= 7) return 1.2f;
        if (days <= 14) return 0.6f;
        return 0f;
    }

    // 計算最終分數
    private float calculateFinalScore(float matchScore, float expiryScore,
                                      int cookingTime, String difficulty, int ingredientCount, int requiredIngredientCount) {
        float baseScore = (matchScore * MATCH_WEIGHT) + (expiryScore * EXPIRY_WEIGHT);
        float timeDeduction = (cookingTime / (float) COOKING_TIME_THRESHOLD) * TIME_PENALTY_WEIGHT;
        float difficultyMultiplier = getDifficultyMultiplier(difficulty);
        float ingredientCountAdjustment = getIngredientCountAdjustment(ingredientCount);
        float tooMuchIng;

        if (requiredIngredientCount>8){tooMuchIng=0;}else{tooMuchIng=1;}

        return (baseScore - timeDeduction) * difficultyMultiplier*ingredientCountAdjustment*tooMuchIng;
    }

    // 根據食材數量計算調整因子
    private float getIngredientCountAdjustment(int ingredientCount) {
//        Log.i("IngredientCountAdjustment", "getIngredientCountAdjustment"+0.2f*ingredientCount);
        float result = 0;
        if (ingredientCount <= 3) {result =ingredientCount*0.2f;}
        if(ingredientCount>=4){
                result = (float) (0.6 + ((ingredientCount - 3) * 0.1f));
            }
        return result; // 其他情況不變
    }

    // 取得難度乘數
    private float getDifficultyMultiplier(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "困難" -> 0.85f;  // 扣15%
            case "簡易" -> 1.1f;   // 加10%
            default -> 1.0f;     // 中等難度不變
        };
    }

    // 內部類：食材匹配資訊
    private static class IngredientMatch {
        final String name;
        final float matchScore;
        final long expiryDays;
        final float ratio;

        IngredientMatch(String name, float matchScore, long expiryDays, float ratio) {
            this.name = name;
            this.matchScore = matchScore;
            this.expiryDays = expiryDays;
            this.ratio = ratio;
        }
    }

    // 內部類：冰箱食材
    public static class FridgeIngredient {
        private String name;
        private float quantity;
        private long expiryDays;

        public FridgeIngredient(String name, float quantity, long expiryDays) {
            this.name = name;
            this.quantity = quantity;
            this.expiryDays = expiryDays;
        }

        public FridgeIngredient(String name, float quantity, Calendar today, int daysToAdd) {
            this.name = name;
            this.quantity = quantity;
            Calendar expiryDate = (Calendar) today.clone();
            expiryDate.add(Calendar.DAY_OF_YEAR, daysToAdd);
            this.expiryDays = (expiryDate.getTimeInMillis() - today.getTimeInMillis())
                    / (1000 * 60 * 60 * 24);
        }

        public String getName() {
            return name;
        }

        public float getQuantity() {
            return quantity;
        }

        public long getExpiryDays() {
            return expiryDays;
        }

        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }

        public void setExpiryDays(long expiryDays) {
            this.expiryDays = expiryDays;
        }
    }

    // 內部類：推薦結果
    public static class Recommendation {
        private final Recipe recipe;
        private final float matchScore;
        private final float expiryScore;
        private final float combinedScore;

        public Recommendation(Recipe recipe, Map<String, Float> scores) {
            this.recipe = recipe;
            this.matchScore = scores.get("matchScore");
            this.expiryScore = scores.get("expiryScore");
            this.combinedScore = scores.get("combinedScore");
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public float getMatchScore() {
            return matchScore;
        }

        public float getExpiryScore() {
            return expiryScore;
        }

        public float getCombinedScore() {
            return combinedScore;
        }
    }

    // 接口：初始化回調
    public interface InitCallback {
        void onSuccess(Map<String, FridgeIngredient> fridgeIngredients);
        void onError(String errorMessage);
    }
}