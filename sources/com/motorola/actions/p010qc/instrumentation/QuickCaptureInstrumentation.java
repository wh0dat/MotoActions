package com.motorola.actions.p010qc.instrumentation;

import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.qc.instrumentation.QuickCaptureInstrumentation */
public class QuickCaptureInstrumentation {
    private static final MALogger LOGGER = new MALogger(QuickCaptureInstrumentation.class);

    private static QuickCaptureAnalytics getQuickCaptureAnalytics() {
        return (QuickCaptureAnalytics) CheckinAlarm.getInstance().getAnalytics(QuickCaptureAnalytics.class);
    }

    public static synchronized void recordQuickCaptureEvent() {
        synchronized (QuickCaptureInstrumentation.class) {
            if (DisplayUtils.isLidClosed()) {
                getQuickCaptureAnalytics().recordQuickCaptureEventClosedLid();
            } else {
                getQuickCaptureAnalytics().recordQuickCaptureEvent();
            }
        }
    }

    public static synchronized void recordDailyToggleEvent(boolean z) {
        synchronized (QuickCaptureInstrumentation.class) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Record QC - Daily Toggle Event: ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            getQuickCaptureAnalytics().recordDailyToggleEvent();
        }
    }
}
