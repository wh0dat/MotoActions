package com.motorola.actions.sleepPattern.instrumentation;

import android.util.SparseArray;

public interface SleepPatternAnalyticsAccess {
    void recordPreProcessing(SparseArray<String> sparseArray);
}
