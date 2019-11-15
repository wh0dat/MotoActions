package com.motorola.actions.sleepPattern;

import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.DiscoveryManager.FDNDelayMode;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.utils.MALogger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SleepPatternReady {
    private static final double ACCEPTABLE_MAX_PERCENTAGE = 0.7d;
    private static final int ACCEPTABLE_MAX_SLOTS = 6;
    private static final MALogger LOGGER = new MALogger(SleepPatternReady.class);
    public static final int MIN_DAYS_DATA_COLLECTED = 10;

    private static class PreprocessingInput {
        private double mFrequency;
        private int mSlot;

        PreprocessingInput(int i, double d) {
            this.mSlot = i;
            this.mFrequency = d;
        }

        public int getSlot() {
            return this.mSlot;
        }

        public double getFrequency() {
            return this.mFrequency;
        }
    }

    private static boolean isSlotsAcceptableDifference(PreprocessingEntity preprocessingEntity) {
        boolean z = false;
        if (preprocessingEntity == null) {
            return false;
        }
        List resultsAsList = preprocessingEntity.getResultsAsList();
        if (resultsAsList.isEmpty()) {
            return false;
        }
        DoubleAdder doubleAdder = new DoubleAdder();
        int i = 7;
        double d = 0.0d;
        Iterator it = ((List) IntStream.range(0, resultsAsList.size()).mapToObj(new SleepPatternReady$$Lambda$0(resultsAsList, doubleAdder)).sorted(Comparator.comparingDouble(SleepPatternReady$$Lambda$1.$instance).reversed()).collect(Collectors.toList())).iterator();
        int i2 = 36;
        int i3 = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            PreprocessingInput preprocessingInput = (PreprocessingInput) it.next();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("data frequency: ");
            sb.append(preprocessingInput.getFrequency());
            sb.append(" - slot: ");
            sb.append(preprocessingInput.getSlot());
            mALogger.mo11957d(sb.toString());
            d += preprocessingInput.getFrequency() / doubleAdder.sum();
            i3 = Math.max(i3, preprocessingInput.getSlot());
            i2 = Math.min(i2, preprocessingInput.getSlot());
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("maximalSlot: ");
            sb2.append(i3);
            sb2.append(" - minimalSlot: ");
            sb2.append(i2);
            sb2.append(" - cumulativePercentage: ");
            sb2.append(d);
            mALogger2.mo11957d(sb2.toString());
            if (d >= ACCEPTABLE_MAX_PERCENTAGE) {
                i = Math.abs(i3 - i2);
                break;
            }
        }
        MALogger mALogger3 = LOGGER;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("isSlotsAcceptableDifference - slotsDifference: ");
        sb3.append(i);
        mALogger3.mo11957d(sb3.toString());
        if (i <= 6) {
            z = true;
        }
        return z;
    }

    static final /* synthetic */ PreprocessingInput lambda$isSlotsAcceptableDifference$0$SleepPatternReady(List list, DoubleAdder doubleAdder, int i) {
        PreprocessingInput preprocessingInput = new PreprocessingInput(i, ((Double) list.get(i)).doubleValue());
        doubleAdder.add(((Double) list.get(i)).doubleValue());
        return preprocessingInput;
    }

    public static void updateSleepPatternIsReady(SleepPatternRepository sleepPatternRepository) {
        int daysRunningSleepPattern = Persistence.getDaysRunningSleepPattern();
        boolean z = DiscoveryManager.getInstance().getFDNDelayMode() == FDNDelayMode.DEBUG.ordinal();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateSleepPatternIsReady - daysRunning: ");
        sb.append(daysRunningSleepPattern);
        mALogger.mo11957d(sb.toString());
        if ((z || daysRunningSleepPattern > 10) && !Persistence.isSleepPatternReady() && sleepPatternRepository != null) {
            boolean isSlotsAcceptableDifference = isSlotsAcceptableDifference(sleepPatternRepository.getPreprocessingResult(1));
            boolean isSlotsAcceptableDifference2 = isSlotsAcceptableDifference(sleepPatternRepository.getPreprocessingResult(2));
            if (isSlotsAcceptableDifference && isSlotsAcceptableDifference2) {
                Persistence.setIsSleepPatternReady(true);
            }
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateSleepPatternIsReady - Retire: ");
            sb2.append(isSlotsAcceptableDifference);
            sb2.append(" - Wakeup: ");
            sb2.append(isSlotsAcceptableDifference2);
            mALogger2.mo11957d(sb2.toString());
        }
    }

    public static void incrementSleepPatternDaysRunning() {
        Persistence.setDaysRunningSleepPattern(Persistence.getDaysRunningSleepPattern() + 1);
    }
}
