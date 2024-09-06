package com.example.fragment_test.RecipeRecommend;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment_test.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class RecipeRecommendationActivity extends AppCompatActivity {
    private RecipeRecommendation recipeRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_recommendation);

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = ((AssetManager) assetManager).open("recipe.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        recipeRecommendation = new RecipeRecommendation();
        recipeRecommendation.init(this, "recipe.json"); // 假設你的 json 文件名為 recipes.json

        Map<String, RecipeRecommendation.FridgeIngredient> fridgeIngredients = recipeRecommendation.getFridgeIngredients();
        List<RecipeRecommendation.Recommendation> recommendations = recipeRecommendation.getRecommendations(fridgeIngredients);

        ListView listView = findViewById(R.id.recommendations_list_view);
        RecommendationAdapter adapter = new RecommendationAdapter(this, recommendations);
        listView.setAdapter(adapter);
    }
}