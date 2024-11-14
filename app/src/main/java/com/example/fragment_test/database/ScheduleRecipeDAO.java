package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.Recipe;
import com.example.fragment_test.entity.RecipeWithScheduledId;
import com.example.fragment_test.entity.ScheduleRecipe;

import java.util.List;

@Dao
public interface ScheduleRecipeDAO {
    @Insert
    long insertScheduleRecipe(ScheduleRecipe scheduleRecipe);

    @Query("""
            SELECT id, r_id, date, day_of_week, status
            FROM schedule_recipes
            WHERE date = :date
            """)
    List<ScheduleRecipe> queryScheduleRecipesByDate(Integer date);

    @Query("""
            SELECT id, r_id, date, day_of_week, status
            FROM schedule_recipes
            WHERE date = :date AND status = 0
            """)
    List<ScheduleRecipe> queryIsNotDoneScheduleRecipesByDate(Integer date);

    @Query("""
            SELECT r.id, r.name, r.src, r.serving, r.collected, s_r.id as sRId, s_r.day_of_week as dayOfWeek
            FROM schedule_recipes s_r join recipes r
            on s_r.r_id = r.id
            WHERE date >= :today AND status = 0
            """)
    List<RecipeWithScheduledId> queryAllNotDoneAndUnexpiredScheduleRecipes(int today);

    @Query("""
            UPDATE schedule_recipes SET status = 1
            WHERE id = :id
            """)
    void updateScheduleRecipeStatusById(Integer id);

    @Query(
            """
            SELECT r.id, r.name, r.src, r.serving, r.collected
            FROM schedule_recipes s_r join recipes r
            ON s_r.r_id = r.id
            WHERE s_r.date = :date AND s_r.status = 0
            """
    )
    List<Recipe> queryScheduleRecipesByDate(int date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateScheduleRecipe(ScheduleRecipe scheduleRecipe);

    @Query(
            """
            DELETE FROM schedule_recipes
            WHERE date = :date AND r_id = :id
            """
    )
    void deleteScheduleRecipeStatusByDateAndRecipeId(int date, int id);
}
