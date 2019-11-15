package com.motorola.actions.sleepPattern.processing;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.sleepPattern.SleepPatternSystemAccess;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Processor {
    private static final MALogger LOGGER = new MALogger(Processor.class);
    private SleepPatternRepository mSleepPatternRepository;
    private SleepPatternSystemAccess mSleepPatternSystemAccess;

    public Processor(SleepPatternRepository sleepPatternRepository, SleepPatternSystemAccess sleepPatternSystemAccess) {
        this.mSleepPatternRepository = sleepPatternRepository;
        this.mSleepPatternSystemAccess = sleepPatternSystemAccess;
    }

    public void predict() {
        LOGGER.mo11957d("Predicting");
        if (Utils.isWeekend(Utils.calculateStartTime(this.mSleepPatternSystemAccess.getTimestamp()))) {
            performPrediction(3);
            performPrediction(4);
            return;
        }
        performPrediction(1);
        performPrediction(2);
    }

    private void performPrediction(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("performPrediction - type: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        List retrievePreprocessingData = retrievePreprocessingData(i);
        if (retrievePreprocessingData != null && !retrievePreprocessingData.isEmpty()) {
            double doubleValue = ((Double) Collections.max(retrievePreprocessingData)).doubleValue();
            if (retrievePreprocessingData.contains(Double.valueOf(doubleValue))) {
                addCalendarPreview(i, calculateIndexCalendar(retrievePreprocessingData.indexOf(Double.valueOf(doubleValue))));
            }
        }
    }

    private Calendar calculateIndexCalendar(int i) {
        Calendar baseTime = Utils.setBaseTime(this.mSleepPatternSystemAccess.getTimestamp());
        baseTime.add(12, i * 30);
        return baseTime;
    }

    private List<Double> retrievePreprocessingData(int i) {
        PreprocessingEntity preprocessingResult = this.mSleepPatternRepository.getPreprocessingResult(i);
        if (preprocessingResult != null) {
            return preprocessingResult.getResultsAsList();
        }
        return null;
    }

    private void addCalendarPreview(int i, Calendar calendar) {
        this.mSleepPatternRepository.updatePreviewResult(i, calendar);
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.KEY_PREVIEW_BASE);
        sb.append(i);
        SharedPreferenceManager.putLong(sb.toString(), calendar.getTimeInMillis());
    }
}
