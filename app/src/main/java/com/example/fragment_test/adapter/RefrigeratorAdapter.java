package com.example.fragment_test.adapter;

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
import com.example.fragment_test.entity.RefrigeratorIngredientVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class RefrigeratorAdapter extends RecyclerView.Adapter<RefrigeratorAdapter.RefrigeratorViewHolder> {

    private List<String> kinds;
    private Map<String, List<RefrigeratorIngredientVO>> refrigeratorMap;
    private Context context;
    private OnClickListener onClickListener;

    public RefrigeratorAdapter(Context context, Map<String, List<RefrigeratorIngredientVO>> refrigeratorMap) {
        this.context = context;
        this.kinds = Arrays.asList(context.getResources().getStringArray(R.array.kinds_of_ingredient));
        this.refrigeratorMap = refrigeratorMap;
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
        List<RefrigeratorIngredientVO> kindOfIngredient = refrigeratorMap.get(kinds.get(position));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.kindOfIngredientContainer.setLayoutManager(layoutManager);
        RefrigeratorEachIngredientAdapter ingredientAdapter = new RefrigeratorEachIngredientAdapter((kindOfIngredient != null ? kindOfIngredient : new ArrayList<>()));
        ingredientAdapter.setOnClickListener(onClickListener);
        holder.kindOfIngredientContainer.setAdapter(ingredientAdapter);
    }

    public interface OnClickListener {
        void onClick(int position, RefrigeratorIngredient refrigeratorIngredient);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    @Override
    public int getItemCount() {
        return kinds.size();
    }
}
