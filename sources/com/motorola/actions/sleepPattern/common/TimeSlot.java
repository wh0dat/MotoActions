package com.motorola.actions.sleepPattern.common;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeSlot {
    private static final long TIME_SLOT_DUR_IN_MILLIS = TimeUnit.MINUTES.toMillis(30);
    private long mId;
    private TimeZone mTimeZone;

    public TimeSlot(Calendar calendar) {
        this.mTimeZone = calendar.getTimeZone();
        this.mId = calendar.getTimeInMillis() / TIME_SLOT_DUR_IN_MILLIS;
    }

    public TimeSlot(TimeZone timeZone, long j) {
        this.mTimeZone = timeZone;
        this.mId = j;
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

    public long get() {
        return this.mId;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public Calendar getHalfTime() {
        Calendar beginTime = getBeginTime();
        beginTime.add(14, ((int) TIME_SLOT_DUR_IN_MILLIS) / 2);
        return beginTime;
    }

    public Calendar getBeginTime() {
        Calendar instance = Calendar.getInstance(this.mTimeZone);
        instance.setTimeInMillis(this.mId * TIME_SLOT_DUR_IN_MILLIS);
        return instance;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeSlot(");
        sb.append(this.mId);
        sb.append(")=");
        sb.append(getBeginTime().getTime().toString());
        return sb.toString();
    }
}
