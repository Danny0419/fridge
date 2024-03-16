package com.example.fragment_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment_test.R;
import com.example.fragment_test.pojo.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class RefrigeratorAdapter extends BaseAdapter {

    private List<String> kinds;
    private Map<String, List<Ingredient>> ingredientMap;
    private Context context;

    public RefrigeratorAdapter(Context context, Map<String, List<Ingredient>> ingredientMap) {
        this.kinds = Arrays.asList(context.getResources().getStringArray(R.array.kinds));
        this.ingredientMap = ingredientMap;
    }

    class ViewHolder {
        TextView kind;
        RecyclerView kindOfIngredientContainer;
    }

    @Override
    public int getCount() {
        return kinds.size();
    }

    @Override
    public Object getItem(int position) {
        return kinds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kind_of_ingredient_container, parent, false);

            viewHolder.kind = convertView.findViewById(R.id.ingredientKindText);
            viewHolder.kindOfIngredientContainer = convertView.findViewById(R.id.kind_of_ingredient_container);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.kind.setText(kinds.get(position));

        List<Ingredient> kindOfIngredient = ingredientMap.get(kinds.get(position));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(parent.getContext(), 5);
        viewHolder.kindOfIngredientContainer.setLayoutManager(gridLayoutManager);
        viewHolder.kindOfIngredientContainer.setAdapter(new RefrigeratorKindAdapter(parent.getContext(),kindOfIngredient != null ? kindOfIngredient : new ArrayList<>()));

        return convertView;
    }
}
