package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ResultsSaver {
    private static final MALogger LOGGER = new MALogger(ResultsSaver.class);
    private SleepPatternRepository mSleepPatternRepository;

    ResultsSaver(SleepPatternRepository sleepPatternRepository) {
        this.mSleepPatternRepository = sleepPatternRepository;
    }

    /* access modifiers changed from: 0000 */
    public void saveResults(Calendar calendar, int i) {
        List list;
        LOGGER.mo11957d("saveResults");
        PreprocessingEntity preprocessingResult = this.mSleepPatternRepository.getPreprocessingResult(i);
        if (preprocessingResult != null) {
            list = preprocessingResult.getResultsAsList();
        } else {
            LOGGER.mo11957d("saveResults - addPreProcessing");
            List arrayList = new ArrayList(Collections.nCopies(((((int) TimeUnit.DAYS.toHours(1)) - 18) + 12) * 2, Double.valueOf(0.0d)));
            this.mSleepPatternRepository.addPreprocessingResult(i, arrayList);
            PreprocessingEntity preprocessingEntity = new PreprocessingEntity(i, PreprocessingEntity.setResultsAsList(arrayList));
            list = arrayList;
            preprocessingResult = preprocessingEntity;
        }
        int seconds = ((int) TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis() - ((Calendar) Utils.getIntervalSetForCalendar(calendar).get(0)).getTimeInMillis())) / ((int) TimeUnit.MINUTES.toSeconds(30));
        if (seconds < 0 || seconds >= 36) {
            LOGGER.mo11959e("Index out of range");
            return;
        }
        List timeReduction = timeReduction(list);
        timeReduction.set(seconds, Double.valueOf(((Double) timeReduction.get(seconds)).doubleValue() + 1.0d));
        preprocessingResult.setResults(PreprocessingEntity.setResultsAsList(timeReduction));
        this.mSleepPatternRepository.updatePreprocessingResult(preprocessingResult);
    }

    private static List<Double> timeReduction(List<Double> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, Double.valueOf(((Double) list.get(i)).doubleValue() * 0.9d));
        }
        return list;
    }
}
