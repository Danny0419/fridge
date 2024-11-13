package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.Step;

import java.util.List;

@Dao
public interface StepDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertStep(Step step);

    @Query("""
            SELECT *
            FROM steps
            WHERE r_id = :rId
            ORDER BY `stepNum`
            """)
    List<Step> getStepsByRid(int rId);
}
