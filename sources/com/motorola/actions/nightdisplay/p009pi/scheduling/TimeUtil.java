package com.motorola.actions.nightdisplay.p009pi.scheduling;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/* renamed from: com.motorola.actions.nightdisplay.pi.scheduling.TimeUtil */
public class TimeUtil {
    public static int getMinutesOfDay(Calendar calendar) {
        return ((int) TimeUnit.HOURS.toMinutes((long) calendar.get(11))) + calendar.get(12);
    }

    public static Calendar getCalendar(Calendar calendar, int i) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(11, i / 60);
        calendar2.set(12, i % 60);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        return calendar2;
    }
}
