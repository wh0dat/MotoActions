package com.motorola.actions.sleepPattern.repository;

import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class SleepPatternDataSource implements SleepPatternRepository {
    private static final MALogger LOGGER = new MALogger(SleepPatternDataSource.class);
    private AccelDao mAccelDao;
    private PreprocessingDao mPreprocessingDao;
    private PreviewDao mPreviewDao;

    @Inject
    public SleepPatternDataSource(PreviewDao previewDao, PreprocessingDao preprocessingDao, AccelDao accelDao) {
        this.mPreviewDao = previewDao;
        this.mPreprocessingDao = preprocessingDao;
        this.mAccelDao = accelDao;
    }

    public void addOrUpdateAccelerometerLog(TimeSlot timeSlot, int i) {
        LOGGER.mo11957d("addOrUpdateAccelerometerLog");
        AccelEntity specificDaily = this.mAccelDao.getSpecificDaily(timeSlot.get());
        if (specificDaily == null) {
            this.mAccelDao.insert(new AccelEntity(timeSlot.get(), i, false));
            return;
        }
        specificDaily.addEventCount(i);
        this.mAccelDao.update(specificDaily);
    }

    public List<AccelEntity> getAllAccelerometerDailyLogs() {
        return this.mAccelDao.getAllDaily();
    }

    public void clearDailyAccelerometerLogs() {
        LOGGER.mo11957d("clearDailyAccelerometerLogs");
        this.mAccelDao.cleanDailyDatabase();
    }

    public void addPreprocessingResult(int i, List<Double> list) {
        LOGGER.mo11957d("addPreprocessingResult");
        PreprocessingEntity preprocessingEntity = new PreprocessingEntity(i, PreprocessingEntity.setResultsAsList(list));
        this.mPreprocessingDao.insertPreprocessing(preprocessingEntity);
    }

    public PreprocessingEntity getPreprocessingResult(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getPreprocessingResult(): type=");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return this.mPreprocessingDao.getDataByType(i);
    }

    public String getPreProcessingResultString(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getPreProcessingResultString(): type=");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        PreprocessingEntity preprocessingResult = getPreprocessingResult(i);
        return preprocessingResult != null ? preprocessingResult.getResults() : "";
    }

    public void updatePreprocessingResult(PreprocessingEntity preprocessingEntity) {
        this.mPreprocessingDao.insertPreprocessing(preprocessingEntity);
    }

    public void updatePreviewResult(int i, Calendar calendar) {
        LOGGER.mo11957d("updatePreviewResult");
        PreviewEntity previewEntity = new PreviewEntity(i, calendar.getTimeInMillis());
        this.mPreviewDao.insertPreview(previewEntity);
    }

    public Calendar getPreviewResult(int i) {
        PreviewEntity dataByType = this.mPreviewDao.getDataByType(i);
        if (dataByType == null) {
            return null;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(dataByType.getResults());
        return instance;
    }

    public void markDataAsDebugData(List<AccelEntity> list) {
        this.mAccelDao.update((List) list.stream().map(SleepPatternDataSource$$Lambda$0.$instance).collect(Collectors.toList()));
    }

    static final /* synthetic */ AccelEntity lambda$markDataAsDebugData$0$SleepPatternDataSource(AccelEntity accelEntity) {
        return new AccelEntity(accelEntity.getTimeSlotId(), accelEntity.getEventCount(), true);
    }

    public List<AccelEntity> getAccelerometerLogs() {
        return this.mAccelDao.getAll();
    }
}
