package com.motorola.actions.nightdisplay.p009pi.scheduling;

import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.scheduling.Scheduler */
public interface Scheduler {
    Calendar getStartTime(Calendar calendar);

    Calendar getStopTime(Calendar calendar);
}
