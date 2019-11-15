package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import java.util.List;

@Dao
public interface PreprocessingDao {
    @Query("delete FROM preprocessing_results")
    void cleanDatabase();

    @Query("SELECT * FROM preprocessing_results")
    List<PreprocessingEntity> getAll();

    @Query("SELECT * FROM preprocessing_results WHERE type=:type")
    PreprocessingEntity getDataByType(int i);

    @Insert(onConflict = 1)
    void insertPreprocessing(PreprocessingEntity... preprocessingEntityArr);
}
