package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import java.util.List;

@Dao
public interface PreviewDao {
    @Query("delete FROM preview_results")
    void cleanDatabase();

    @Query("SELECT * FROM preview_results")
    List<PreviewEntity> getAll();

    @Query("SELECT * FROM preview_results WHERE type=:type")
    PreviewEntity getDataByType(int i);

    @Insert(onConflict = 1)
    void insertPreview(PreviewEntity... previewEntityArr);
}
