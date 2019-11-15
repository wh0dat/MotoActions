package com.motorola.actions.nightdisplay.instrumentation;

import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.MALogger;

public class NightDisplayInstrumentation {
    private static final MALogger LOGGER = new MALogger(NightDisplayInstrumentation.class);

    private static NightDisplayAnalytics getNightDisplayAnalytics() {
        return (NightDisplayAnalytics) CheckinAlarm.getInstance().getAnalytics(NightDisplayAnalytics.class);
    }

    public static synchronized void recordDailyFilterActivated() {
        synchronized (NightDisplayInstrumentation.class) {
            LOGGER.mo11957d("record blue light filter activated");
            getNightDisplayAnalytics().recordDailyFilterActivated();
        }
    }
}
