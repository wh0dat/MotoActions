package com.motorola.actions.nightdisplay.p009pi.scheduling.scheduler;

import com.motorola.actions.nightdisplay.p009pi.scheduling.Scheduler;
import com.motorola.actions.nightdisplay.p009pi.scheduling.TimeUtil;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.scheduling.scheduler.TimeOnlyScheduler */
public class TimeOnlyScheduler implements Scheduler {
    private static final MALogger LOGGER = new MALogger(TimeOnlyScheduler.class);
    private final int mOffsetBeginMinutes;
    private final int mOffsetEndMinutes;

    public TimeOnlyScheduler(int i, int i2) {
        this.mOffsetBeginMinutes = i;
        this.mOffsetEndMinutes = i2;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Instantiating CustomOnlyScheduler with beginOffsetMinutes at:");
        sb.append(i);
        sb.append(" offsetEndMinutes:");
        sb.append(i2);
        mALogger.mo11957d(sb.toString());
    }

    public Calendar getStartTime(Calendar calendar) {
        Calendar calendar2 = TimeUtil.getCalendar(calendar, this.mOffsetBeginMinutes);
        Calendar calendar3 = TimeUtil.getCalendar(calendar, this.mOffsetEndMinutes);
        if (calendar2.before(calendar3)) {
            if (calendar3.before(calendar)) {
                calendar2.add(6, 1);
            }
        } else if (calendar.before(calendar3) || calendar.equals(calendar3)) {
            calendar2.add(6, -1);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getStartTime: ");
        sb.append(calendar2.getTime().toString());
        mALogger.mo11957d(sb.toString());
        return calendar2;
    }

    public Calendar getStopTime(Calendar calendar) {
        Calendar calendar2 = TimeUtil.getCalendar(calendar, this.mOffsetEndMinutes);
        if (calendar2.before(calendar) || calendar2.equals(calendar)) {
            calendar2.add(6, 1);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getStopTime: ");
        sb.append(calendar2.getTime().toString());
        mALogger.mo11957d(sb.toString());
        return calendar2;
    }
}
