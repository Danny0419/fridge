package com.example.fragment_test.database;

import androidx.room.ExperimentalRoomApi;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@ExperimentalRoomApi
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ScheduleDAOTest extends TestCase {

    public FridgeDatabase database;
    public ScheduleDAO scheduleDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        FridgeDatabase.class
                ).allowMainThreadQueries()
                .build();
        scheduleDAO = database.scheduleDAO();
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void insertAScheduleReturnRowIdOne() {
        Schedule schedule = new Schedule(0, 5, 0);
        long l = scheduleDAO.insertSchedule(schedule);
        assertEquals(1, l);

    }

    @Test
    public void getFirstScheduleShouldIdShouldEqualOne() {
        Schedule schedule = new Schedule(0, 5,  0);
        scheduleDAO.insertSchedule(schedule);
        Schedule schedule1 = scheduleDAO.getSchedule(1);
        assertEquals(1, schedule1.date);

    }

    @Test
    public void getAllNotFinishedSchedule_sizeShouldEqualTwo() {
        List<Schedule> schedules = List.of(
                new Schedule(0, 5,  0),
                new Schedule(0, 5, 1),
                new Schedule(0, 5, 1),
                new Schedule(0, 5, 0),
                new Schedule(0, 5,  1)
        );

        schedules.forEach((schedule) -> scheduleDAO.insertSchedule(schedule));
        List<Schedule> querySchedules = scheduleDAO.getSchedulesStatusIsZero();
        assertEquals(2, querySchedules.size());
    }

    @Test
    public void updateScheduleStatusToOne(){
        List<Schedule> schedules = List.of(
                new Schedule(0, 5, 0),
                new Schedule(0, 5,  1),
                new Schedule(0, 5,  1),
                new Schedule(0, 5,  0),
                new Schedule(0, 5,  1)
        );
        schedules.forEach((schedule) -> scheduleDAO.insertSchedule(schedule));
        scheduleDAO.updateSchedule(1);

        List<Schedule> schedules1 = scheduleDAO.getSchedulesStatusIsZero();
        assertEquals(1, schedules1.size());
    }
}