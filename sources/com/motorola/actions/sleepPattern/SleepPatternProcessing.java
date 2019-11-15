package com.motorola.actions.sleepPattern;

import android.text.TextUtils;
import android.util.SparseArray;
import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalyticsAccess;
import com.motorola.actions.sleepPattern.preprocessing.Preprocessing;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.sleepPattern.processing.Processor;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;

public class SleepPatternProcessing {
    private static final MALogger LOGGER = new MALogger(SleepPatternProcessing.class);
    private SleepPatternAnalyticsAccess mSleepPatternAnalyticsAccess;
    private SleepPatternRepository mSleepPatternRepository;
    private SleepPatternSystemAccess mSleepPatternSystemAccess;

    @Inject
    SleepPatternProcessing(SleepPatternRepository sleepPatternRepository, SleepPatternSystemAccess sleepPatternSystemAccess, SleepPatternAnalyticsAccess sleepPatternAnalyticsAccess) {
        this.mSleepPatternRepository = sleepPatternRepository;
        this.mSleepPatternSystemAccess = sleepPatternSystemAccess;
        this.mSleepPatternAnalyticsAccess = sleepPatternAnalyticsAccess;
    }

    public void processSleepPattern(boolean z) {
        if (!isWithinActiveInterval()) {
            LOGGER.mo11957d("processSleepPattern: Outside active interval");
            performSleepPattern();
            writeToInstrumentation();
            if (z) {
                this.mSleepPatternSystemAccess.reschedule();
                return;
            }
            return;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("processSleepPattern: Within active interval:");
        sb.append(this.mSleepPatternSystemAccess.getTimestamp().getTime().toString());
        mALogger.mo11957d(sb.toString());
    }

    private void performSleepPattern() {
        LOGGER.mo11957d("performSleepPattern");
        new Preprocessing(this.mSleepPatternRepository).perform(this.mSleepPatternSystemAccess.getTimestamp().getTimeZone());
        if (!Constants.PRODUCTION_MODE) {
            this.mSleepPatternRepository.markDataAsDebugData(this.mSleepPatternRepository.getAllAccelerometerDailyLogs());
        }
        this.mSleepPatternRepository.clearDailyAccelerometerLogs();
        new Processor(this.mSleepPatternRepository, this.mSleepPatternSystemAccess).predict();
    }

    private void writeToInstrumentation() {
        if (!Constants.PRODUCTION_MODE) {
            SparseArray sparseArray = new SparseArray(4);
            sparseArray.append(1, getPreprocessingResult(1));
            sparseArray.append(2, getPreprocessingResult(2));
            sparseArray.append(3, getPreprocessingResult(3));
            sparseArray.append(4, getPreprocessingResult(4));
            this.mSleepPatternAnalyticsAccess.recordPreProcessing(sparseArray);
        }
    }

    private String getPreprocessingResult(int i) {
        String preProcessingResultString = this.mSleepPatternRepository.getPreProcessingResultString(i);
        if (!TextUtils.isEmpty(preProcessingResultString)) {
            preProcessingResultString = preProcessingResultString.replaceAll(" ", ",");
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getPreProcessByType - type: ");
        sb.append(i);
        sb.append(" - result: ");
        sb.append(preProcessingResultString);
        mALogger.mo11957d(sb.toString());
        return preProcessingResultString;
    }

    private boolean isWithinActiveInterval() {
        List allAccelerometerDailyLogs = this.mSleepPatternRepository.getAllAccelerometerDailyLogs();
        if (allAccelerometerDailyLogs != null && allAccelerometerDailyLogs.size() > 0) {
            Calendar timestamp = this.mSleepPatternSystemAccess.getTimestamp();
            List intervalSetForCalendar = Utils.getIntervalSetForCalendar(new TimeSlot(timestamp.getTimeZone(), ((AccelEntity) allAccelerometerDailyLogs.get(0)).getTimeSlotId()).getHalfTime());
            if (timestamp.before(intervalSetForCalendar.get(1)) && timestamp.after(intervalSetForCalendar.get(0))) {
                return true;
            }
        }
        return false;
    }
}
