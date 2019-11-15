package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.sleepPattern.SleepPatternReady;
import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TimeZone;

public class Preprocessing {
    private static final MALogger LOGGER = new MALogger(Preprocessing.class);
    private static final int WEEK = 0;
    private static final int WEEKEND = 1;
    private Analyser mAnalyser;
    private EventsReader mReader;
    private SleepPatternRepository mSleepPatternRepository;

    private boolean isWeek(int i) {
        return i == 0;
    }

    public Preprocessing(SleepPatternRepository sleepPatternRepository) {
        this.mReader = new EventsReader(sleepPatternRepository);
        this.mSleepPatternRepository = sleepPatternRepository;
    }

    public void perform(TimeZone timeZone) {
        LOGGER.mo11957d("Performing preprocessing");
        this.mAnalyser = new Analyser();
        this.mReader.readAccelerometerLogs(timeZone);
        this.mReader.getGroupIntervalSets(this.mReader.getWeekData());
        processSleepResult(0);
        processSleepResult(1);
        clearMembers();
    }

    private void clearMembers() {
        this.mReader = null;
        this.mAnalyser = null;
    }

    private void processSleepResult(int i) {
        ResultsSaver resultsSaver = new ResultsSaver(this.mSleepPatternRepository);
        SortedMap weekData = isWeek(i) ? this.mReader.getWeekData() : this.mReader.getWeekendData();
        for (List list : this.mReader.getGroupIntervalSets(weekData)) {
            List identifySleepPeriod = this.mAnalyser.identifySleepPeriod(weekData.subMap(list.get(0), list.get(1)));
            if (list.size() > 0) {
                int i2 = 2;
                if (identifySleepPeriod.size() == 2 && isValidPeriod(identifySleepPeriod, weekData)) {
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Performing Week preprocessing - saving results - week type: ");
                    sb.append(i);
                    mALogger.mo11957d(sb.toString());
                    resultsSaver.saveResults((Calendar) identifySleepPeriod.get(0), isWeek(i) ? 1 : 3);
                    Calendar calendar = (Calendar) identifySleepPeriod.get(1);
                    if (!isWeek(i)) {
                        i2 = 4;
                    }
                    resultsSaver.saveResults(calendar, i2);
                }
            }
        }
        if (isWeek(i)) {
            SleepPatternReady.incrementSleepPatternDaysRunning();
            SleepPatternReady.updateSleepPatternIsReady(this.mSleepPatternRepository);
            if (Persistence.isSleepPatternReady()) {
                DiscoveryManager.getInstance().onFDNEvent(FeatureKey.NIGHT_DISPLAY);
            }
        }
    }

    private boolean isValidPeriod(List<Calendar> list, SortedMap<Calendar, Integer> sortedMap) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isValidPeriod: Start: ");
        sb.append(((Calendar) list.get(0)).getTime());
        sb.append(" - End: ");
        sb.append(((Calendar) list.get(1)).getTime());
        mALogger.mo11957d(sb.toString());
        List timeSlotsOfInterval = this.mReader.getTimeSlotsOfInterval(new TimeSlot((Calendar) list.get(0)));
        if (timeSlotsOfInterval.isEmpty()) {
            LOGGER.mo11957d("isValidPeriod: Ignoring process, timeSlots are Empty!");
            return false;
        }
        Calendar halfTime = ((TimeSlot) timeSlotsOfInterval.get(0)).getHalfTime();
        Calendar halfTime2 = ((TimeSlot) timeSlotsOfInterval.get(timeSlotsOfInterval.size() - 1)).getHalfTime();
        if ((((Calendar) list.get(0)).compareTo(halfTime) != 0 || ((Integer) sortedMap.get(halfTime)).intValue() != 0) && (((Calendar) list.get(1)).compareTo(halfTime2) != 0 || ((Integer) sortedMap.get(halfTime2)).intValue() != 0)) {
            return true;
        }
        LOGGER.mo11957d("isValidPeriod: Ignoring process, not valid data from accelerometer.");
        return false;
    }
}
