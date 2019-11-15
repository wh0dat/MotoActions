package com.motorola.actions.sleepPattern.repository;

import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import java.util.Calendar;
import java.util.List;

public interface SleepPatternRepository {
    void addOrUpdateAccelerometerLog(TimeSlot timeSlot, int i);

    void addPreprocessingResult(int i, List<Double> list);

    void clearDailyAccelerometerLogs();

    List<AccelEntity> getAccelerometerLogs();

    List<AccelEntity> getAllAccelerometerDailyLogs();

    String getPreProcessingResultString(int i);

    PreprocessingEntity getPreprocessingResult(int i);

    Calendar getPreviewResult(int i);

    void markDataAsDebugData(List<AccelEntity> list);

    void updatePreprocessingResult(PreprocessingEntity preprocessingEntity);

    void updatePreviewResult(int i, Calendar calendar);
}
