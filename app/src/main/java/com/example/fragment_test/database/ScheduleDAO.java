package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fragment_test.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDAO {
    @Query("""
            SELECT date, day_of_week, status
            from schedules
            where date = :date
            """)
    Schedule getSchedule(int date);

    @Query("""
            SELECT date, day_of_week, status
            from schedules
            where date = :date AND status = 0
            """)
    Schedule getScheduleStatusIsZero(int date);

    @Query("""
    select date, day_of_week, status
    from schedules
    where status = 0
    """)
    List<Schedule> getSchedulesStatusIsZero();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSchedule(Schedule schedule);

    @Query("""
            update schedules set status = 1
            where date = :date
            """)
    void updateSchedule(int date);
}
