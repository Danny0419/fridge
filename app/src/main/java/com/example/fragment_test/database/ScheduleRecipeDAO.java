package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.ScheduleRecipe;

import java.util.List;

@Dao
public interface ScheduleRecipeDAO {
    @Insert
    long insertScheduleRecipe(ScheduleRecipe scheduleRecipe);

    @Query("""
            SELECT id, r_id, s_id, day_of_week, status
            FROM schedule_recipes
            WHERE s_id = :sId
            """)
    List<ScheduleRecipe> queryScheduleRecipesBySId(Integer sId);

    @Query("""
            SELECT id, r_id, s_id, day_of_week, status
            FROM schedule_recipes
            WHERE s_id = :sId AND status = 0
            """)
    List<ScheduleRecipe> queryIsNotDoneScheduleRecipesBySId(Integer sId);

    @Query("""
            SELECT id, r_id, s_id, day_of_week, status
            FROM schedule_recipes
            WHERE status = 0
            """)
    List<ScheduleRecipe> queryAllIsNotDoneScheduleRecipes();

    @Query("""
            UPDATE schedule_recipes SET status = 1
            WHERE id = :id
            """)
    void updateScheduleRecipeStatusById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateScheduleRecipe(ScheduleRecipe scheduleRecipe);
}
