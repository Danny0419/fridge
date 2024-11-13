package com.example.fragment_test.RecipeRecommend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fragment_test.R;

import java.util.List;

public class RecommendationAdapter extends ArrayAdapter<RecipeRecommendation.Recommendation> {

    public RecommendationAdapter(Context context, List<RecipeRecommendation.Recommendation> recommendations) {
        super(context, 0, recommendations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recommendation, parent, false);
        }

        // 获取当前推荐的食谱项
        RecipeRecommendation.Recommendation recommendation = getItem(position);

        // 绑定视图和数据
        TextView recipeName = convertView.findViewById(R.id.recipe_name);
        TextView matchScore = convertView.findViewById(R.id.match_score);
        TextView expiryScore = convertView.findViewById(R.id.expiry_score);
        TextView combinedScore = convertView.findViewById(R.id.combined_score);

        if (recommendation != null) {
            recipeName.setText(recommendation.getRecipe().getRecipe_name());
            matchScore.setText(String.format("Match Score: %.2f", recommendation.getMatchScore()));
            expiryScore.setText(String.format("Expiry Score: %.2f", recommendation.getExpiryScore()));
            combinedScore.setText(String.format("Combined Score: %.2f", recommendation.getCombinedScore()));
            Log.i("TAG", recommendation.getRecipe().getPicture());
        }

        return convertView;
    }
}
