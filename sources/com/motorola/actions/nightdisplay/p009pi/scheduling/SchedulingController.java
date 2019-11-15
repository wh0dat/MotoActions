package com.motorola.actions.nightdisplay.p009pi.scheduling;

import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.nightdisplay.common.SleepPatternAccess;
import com.motorola.actions.nightdisplay.p009pi.scheduling.scheduler.TimeOnlyScheduler;
import com.motorola.actions.sleepPattern.preprocessing.Utils;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.scheduling.SchedulingController */
public class SchedulingController {
    private static final MALogger LOGGER = new MALogger(SchedulingController.class);
    private Scheduler mScheduler;

    public boolean isWithinActiveTime(Calendar calendar) {
        Calendar startServiceTime = getStartServiceTime(calendar);
        Calendar stopServiceTime = getStopServiceTime(calendar);
        boolean z = (calendar.after(startServiceTime) || calendar.equals(startServiceTime)) && (calendar.before(stopServiceTime) || calendar.equals(stopServiceTime));
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isWithinActiveTime: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public Calendar getStopServiceTime(Calendar calendar) {
        return this.mScheduler.getStopTime(calendar);
    }

    private Calendar getStartServiceTime(Calendar calendar) {
        return this.mScheduler.getStartTime(calendar);
    }

    public void updateConfiguration(Calendar calendar, int i) {
        int mode = Persistence.getMode(i);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("updateConfiguration, mode: ");
        sb.append(mode);
        mALogger.mo11957d(sb.toString());
        this.mScheduler = new TimeOnlyScheduler(getInitialTime(calendar, mode), getTerminationTime(calendar, mode));
    }

    private int getInitialTime(Calendar calendar, int i) {
        int i2;
        if (i != 4) {
            i2 = Persistence.getInitialTimeInMinutes();
        } else {
            i2 = SleepPatternAccess.getSleepPatternTimeInMinutes(calendar, Utils.RETIRE);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getInitialTime: ");
        sb.append(i2);
        mALogger.mo11957d(sb.toString());
        return i2;
    }

    private int getTerminationTime(Calendar calendar, int i) {
        int i2;
        if (i != 4) {
            i2 = Persistence.getTerminationTimeInMinutes();
        } else {
            i2 = SleepPatternAccess.getSleepPatternTimeInMinutes(calendar, Utils.WAKEUP);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getTerminationTime: ");
        sb.append(i2);
        mALogger.mo11957d(sb.toString());
        return i2;
    }
}
