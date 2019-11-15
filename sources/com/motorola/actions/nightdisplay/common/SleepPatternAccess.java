package com.motorola.actions.nightdisplay.common;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.p009pi.scheduling.TimeUtil;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SleepPatternAccess {
    private static final MALogger LOGGER = new MALogger(SleepPatternAccess.class);

    private static Optional<Integer> getPersistentSleepTimeInMinutes(Calendar calendar, String str) {
        long j;
        Optional<Integer> empty = Optional.empty();
        Calendar calculateStartTime = Utils.calculateStartTime(calendar);
        if (Utils.RETIRE.equals(str)) {
            if (Utils.isWeekend(calculateStartTime)) {
                j = SharedPreferenceManager.getLong("sleep_pattern_preview_3", 0);
            } else {
                j = SharedPreferenceManager.getLong("sleep_pattern_preview_1", 0);
            }
        } else if (Utils.isWeekend(calculateStartTime)) {
            j = SharedPreferenceManager.getLong("sleep_pattern_preview_4", 0);
        } else {
            j = SharedPreferenceManager.getLong("sleep_pattern_preview_2", 0);
        }
        if (j > 0) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(j);
            empty = Optional.of(Integer.valueOf(TimeUtil.getMinutesOfDay(instance)));
        }
        if (empty.isPresent()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("getPersistentSleepTimeInMinutes - ");
            sb.append(str);
            sb.append(" - hours: ");
            sb.append(((double) ((Integer) empty.get()).intValue()) / 60.0d);
            mALogger.mo11957d(sb.toString());
        } else {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getPersistentSleepTimeInMinutes - ");
            sb2.append(str);
            sb2.append(" - unavailable.");
            mALogger2.mo11957d(sb2.toString());
        }
        return empty;
    }

    public static int getSleepPatternTimeInMinutes(Calendar calendar, String str) {
        int i;
        boolean isSleepPatternReady = Persistence.isSleepPatternReady();
        Optional persistentSleepTimeInMinutes = getPersistentSleepTimeInMinutes(calendar, str);
        if (!persistentSleepTimeInMinutes.isPresent() || !isSleepPatternReady) {
            LOGGER.mo11963w("No valid data, using manual values.");
            if (Utils.WAKEUP.equals(str)) {
                i = Persistence.getTerminationTimeInMinutes();
            } else {
                i = Persistence.getInitialTimeInMinutes();
            }
        } else {
            int minutes = (int) TimeUnit.DAYS.toMinutes(1);
            if (Utils.RETIRE.equals(str)) {
                i = ((((Integer) persistentSleepTimeInMinutes.get()).intValue() - NightDisplayConstants.SLEEP_DELTA_IN_MINUTES) + minutes) % minutes;
            } else {
                i = ((Integer) persistentSleepTimeInMinutes.get()).intValue();
            }
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getSleepPatternTimeInMinutes - ");
        sb.append(str);
        sb.append(" - hours: ");
        sb.append(((double) i) / 60.0d);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    public static boolean isReady() {
        boolean isSleepPatternReady = Persistence.isSleepPatternReady();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isReady = ");
        sb.append(isSleepPatternReady);
        mALogger.mo11957d(sb.toString());
        return isSleepPatternReady;
    }
}
