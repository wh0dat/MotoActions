package com.motorola.actions.nightdisplay.common;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimeSlot {
    private static final long NUMBER_TIME_SLOTS_PER_DAY = (TimeUnit.DAYS.toMinutes(1) / 30);
    private final long mId;

    public TimeSlot(Calendar calendar) {
        this.mId = getMinutesSinceBeginOfYear(calendar) / 30;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !TimeSlot.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (((TimeSlot) obj).mId == this.mId) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (int) this.mId;
    }

    public long getOrdinalWithinDay() {
        return this.mId % NUMBER_TIME_SLOTS_PER_DAY;
    }

    private long getMinutesSinceBeginOfYear(Calendar calendar) {
        return TimeUnit.DAYS.toMinutes((long) calendar.get(6)) + TimeUnit.HOURS.toMinutes((long) calendar.get(11)) + ((long) calendar.get(12));
    }
}
