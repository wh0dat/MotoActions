package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.List;

@Dao
public interface AccelDao {
    @Query("delete FROM accelerometer_logs WHERE debug=0")
    void cleanDailyDatabase();

    @Query("SELECT * FROM accelerometer_logs")
    List<AccelEntity> getAll();

    @Query("SELECT * FROM accelerometer_logs WHERE debug=0")
    List<AccelEntity> getAllDaily();

    @Query("SELECT * FROM accelerometer_logs WHERE time_slot_id=:timeSlotId AND debug=0")
    AccelEntity getSpecificDaily(long j);

    @Insert(onConflict = 1)
    void insert(AccelEntity... accelEntityArr);

    @Update(onConflict = 1)
    void update(List<AccelEntity> list);

    @Update(onConflict = 1)
    void update(AccelEntity... accelEntityArr);
}
