package com.example.fragment_test.RecipeRecommend;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment_test.R;
import java.util.List;
import java.util.Map;

public class RecommendationActivity extends AppCompatActivity {
    private RecipeRecommendation recipeRecommendation;
    private ListView listView;
    private RecommendationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_recommendation);

        listView = findViewById(R.id.recommendations_list_view);
        recipeRecommendation = new RecipeRecommendation();

        // 异步初始化数据
        recipeRecommendation.init(this, new RecipeRecommendation.InitCallback() {
            @Override
            public void onSuccess(Map<String, RecipeRecommendation.FridgeIngredient> fridgeIngredients) {
                // 获取推荐食谱并更新UI
                getRecommendationsAndUpdateUI(fridgeIngredients);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("RecommendationActivity", "Error initializing RecipeRecommendation: " + errorMessage);
            }
        });
    }

    private void getRecommendationsAndUpdateUI(Map<String, RecipeRecommendation.FridgeIngredient> fridgeIngredients) {
        List<RecipeRecommendation.Recommendation> recommendations = recipeRecommendation.getRecommendations(fridgeIngredients);

        if (recommendations != null && !recommendations.isEmpty()) {
            adapter = new RecommendationAdapter(this, recommendations);
            listView.setAdapter(adapter);
        } else {
            Log.e("RecommendationActivity", "No recommendations found.");
        }
    }
}
