package com.example.fragment_test.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDAO {
    @Insert
    long insertSchedule(Schedule schedule);

    @Query("""
            SELECT id, day_of_week, date, status
            from schedule
            where id = :id
            """)
    Schedule getSchedule(int id);

    @Query("""
    select id, day_of_week, date, status
    from schedule
    where status = 0
    """)
    List<Schedule> getSchedules();
}
