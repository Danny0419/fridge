package com.example.fragment_test.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class RefrigeratorAdapter extends RecyclerView.Adapter<RefrigeratorAdapter.RefrigeratorViewHolder> {

    private List<String> kinds;
    private Map<String, List<Ingredient>> ingredientMap;
    private Context context;
    private ArrayList<String> name;

    public RefrigeratorAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
//        this.kinds = Arrays.asList(context.getResources().getStringArray(R.array.kinds));
//        this.ingredientMap = ingredientMap;
    }

     static class RefrigeratorViewHolder extends RecyclerView.ViewHolder {

        TextView nameOKind;
//        RecyclerView kindOfIngredientContainer;
        public RefrigeratorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOKind = itemView.findViewById(R.id.ingredientKindName);
//            kindOfIngredientContainer = itemView.findViewById(R.id.kindOfIngredientContainer);
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
        holder.nameOKind.setText(name.get(position));
//        List<Ingredient> kindOfIngredient = ingredientMap.get(kinds.get(position));
//        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
//        holder.kindOfIngredientContainer.setLayoutManager(layoutManager);
//        holder.kindOfIngredientContainer.setAdapter(new RefrigeratorKindAdapter(kindOfIngredient != null ? kindOfIngredient : new ArrayList<>()));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kind_of_ingredient_container, parent, false);
//
//
//            viewHolder.kind = convertView.findViewById(R.id.ingredientKindText);
//            viewHolder.kindOfIngredientContainer = convertView.findViewById(R.id.kind_of_ingredient_container);
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.kind.setText(kinds.get(position));
//
//        List<Ingredient> kindOfIngredient = ingredientMap.get(kinds.get(position));
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(parent.getContext(), 5);
//        viewHolder.kindOfIngredientContainer.setLayoutManager(gridLayoutManager);
//        viewHolder.kindOfIngredientContainer.setAdapter(new RefrigeratorKindAdapter(parent.getContext(),kindOfIngredient != null ? kindOfIngredient : new ArrayList<>()));
//
//        return convertView;
//    }
}
