package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipes;
    private OnClickListener listener;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.context = context;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView recipeImg;
        private TextView recipeName;
        private RecyclerView needs;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.recipeImg);
            recipeName = itemView.findViewById(R.id.recipeName);
            needs = itemView.findViewById(R.id.need_ingredients);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeImg.setImageBitmap(recipe.pic);
        holder.recipeName.setText(recipe.setName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.itemView.setOnClickListener(view -> listener.onClick(position, recipe));
        holder.needs.setLayoutManager(layoutManager);
        holder.needs.setAdapter(new RecipeIngredientAdapter(recipe.ingredients));

        //頁面跳轉
//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onItemClick(recipe);
//            }
//        });
    }

    public interface OnClickListener {
        void onClick(int position, Recipe recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}
