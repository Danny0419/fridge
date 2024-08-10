package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fragment_test.entity.ScheduleRecipe;

import java.util.List;

@Dao
public interface ScheduleRecipeDAO {
    @Insert
    long insertScheduleRecipe(ScheduleRecipe scheduleRecipe);

    @Query("""
            SELECT id, r_id, s_id, status
            FROM schedule_recipe
            WHERE s_id = :sId
            """)
    List<ScheduleRecipe> queryScheduleRecipesBySId(Integer sId);

    @Query("""
            SELECT id, r_id, s_id, status
            FROM schedule_recipe
            WHERE s_id = :sId AND status = 0
            """)
    List<ScheduleRecipe> queryIsNotDoneScheduleRecipesBySId(Integer sId);

    @Query("""
            UPDATE schedule_recipe SET status = 1
            WHERE id = :id
            """)
    void updateScheduleRecipeStatusById(Integer id);
}
