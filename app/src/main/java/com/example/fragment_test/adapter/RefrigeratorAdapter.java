package com.example.fragment_test.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.entity.RefrigeratorIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class RefrigeratorAdapter extends RecyclerView.Adapter<RefrigeratorAdapter.RefrigeratorViewHolder> {

    private List<String> kinds;
    private Map<String, List<RefrigeratorIngredient>> refrigeratorMap;
    private Context context;
    private Dialog ingredientDetail;

    public RefrigeratorAdapter(Context context, Map<String, List<RefrigeratorIngredient>> refrigeratorMap, Dialog ingredientDetail) {
        this.context = context;
        this.kinds = Arrays.asList(context.getResources().getStringArray(R.array.kinds));
        this.refrigeratorMap = refrigeratorMap;
        this.ingredientDetail = ingredientDetail;
    }

     static class RefrigeratorViewHolder extends RecyclerView.ViewHolder {

        TextView nameOKind;
        RecyclerView kindOfIngredientContainer;
        public RefrigeratorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOKind = itemView.findViewById(R.id.ingredientKindName);
            kindOfIngredientContainer = itemView.findViewById(R.id.kindOfIngredientContainer);
        }
    }
    @NonNull
    @Override
    public RefrigeratorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kind_of_ingredient_container, parent,false);
        return new RefrigeratorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorViewHolder holder, int position) {
        holder.nameOKind.setText(kinds.get(position));
        List<RefrigeratorIngredient> kindOfIngredient = refrigeratorMap.get(kinds.get(position));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.kindOfIngredientContainer.setLayoutManager(layoutManager);
        holder.kindOfIngredientContainer.setAdapter(new RefrigeratorKindAdapter((kindOfIngredient != null ? kindOfIngredient : new ArrayList<>()), ingredientDetail));
    }

    @Override
    public int getItemCount() {
        return kinds.size();
    }
}
