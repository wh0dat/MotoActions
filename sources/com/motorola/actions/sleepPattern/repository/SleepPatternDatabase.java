package com.motorola.actions.sleepPattern.repository;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;

@Database(entities = {AccelEntity.class, PreprocessingEntity.class, PreviewEntity.class}, exportSchema = false, version = 1)
public abstract class SleepPatternDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "sleep_pattern_room.db";
    static final int DATABASE_VERSION = 1;
    private static SleepPatternDatabase sInstance;

    public abstract AccelDao accelDao();

    public abstract PreprocessingDao preprocessingDao();

    public abstract PreviewDao previewDao();

    public static synchronized SleepPatternDatabase getInstance(Application application) {
        SleepPatternDatabase sleepPatternDatabase;
        synchronized (SleepPatternDatabase.class) {
            if (sInstance == null) {
                sInstance = (SleepPatternDatabase) Room.databaseBuilder(application, SleepPatternDatabase.class, DATABASE_NAME).build();
            }
            sleepPatternDatabase = sInstance;
        }
        return sleepPatternDatabase;
    }

    public static synchronized void closeInstance() {
        synchronized (SleepPatternDatabase.class) {
            if (sInstance != null) {
                sInstance.close();
                sInstance = null;
            }
        }
    }
}
