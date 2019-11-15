package com.motorola.actions.sleepPattern;

import java.util.Calendar;

public interface SleepPatternSystemAccess {
    Calendar getTimestamp();

    void reschedule();
}
