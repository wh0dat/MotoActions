package com.motorola.actions.sleepPattern.common;

import java.util.concurrent.TimeUnit;

public class Settings {
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 0;
    public static final long FIVE_MINUTES_IN_MILLISECONDS = TimeUnit.MINUTES.toMillis(5);
    public static final int INTERVAL_END_HOUR = 12;
    public static final int INTERVAL_START_HOUR = 18;
    public static final double MEMORY_RELEVANCE = 0.9d;
    public static final int NUMBER_INTERVALS = 36;
    public static final double STD_TOLERANCE = 0.15d;
    public static final int STILL_CONFIDENCE_THRESHOLD = 75;
    public static final int TIME_SLOT = 2;
    public static final int TIME_STEP_MINUTES = 30;
}
