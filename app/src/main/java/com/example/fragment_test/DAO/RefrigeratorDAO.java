package com.example.fragment_test.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fragment_test.pojo.RefrigeratorIngredient;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface RefrigeratorDAO {
    @Query("SELECT id, name, img, sort, quantity, expiration  from refrigerator" +
            " WHERE expired = 0")
    List<RefrigeratorIngredient> query();
}
