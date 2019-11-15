package com.motorola.actions.foc.instrumentation;

import android.os.SystemClock;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;

public class FlashOnChopInstrumentation {
    private static final MALogger LOGGER = new MALogger(FlashOnChopInstrumentation.class);
    private static boolean sIsChopChopLastFlashlightSource = false;
    private static long sLastFlashlightOnTime = -1;
    private static long sUsageTimeTrackingStartMs = -1;

    private static FlashOnChopAnalytics getFlashOnChopAnalytics() {
        return (FlashOnChopAnalytics) CheckinAlarm.getInstance().getAnalytics(FlashOnChopAnalytics.class);
    }

    public static synchronized void recordChopEvent() {
        synchronized (FlashOnChopInstrumentation.class) {
            if (DisplayUtils.isLidClosed()) {
                getFlashOnChopAnalytics().recordChopEventClosedLid();
            } else {
                getFlashOnChopAnalytics().recordChopEvent();
            }
        }
    }

    public static synchronized void recordFlashlightOnEvents(boolean z) {
        synchronized (FlashOnChopInstrumentation.class) {
            FlashOnChopAnalytics flashOnChopAnalytics = getFlashOnChopAnalytics();
            if (sLastFlashlightOnTime >= 0) {
                LOGGER.mo11959e("recordFlashlightOnEvents was done without a recordFlashlightOffEvents");
            }
            sLastFlashlightOnTime = SystemClock.elapsedRealtime();
            sIsChopChopLastFlashlightSource = z;
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("recordFlashlightOnEvents - isChopChop = ");
            sb.append(z);
            sb.append(" - eventTime = ");
            sb.append(sLastFlashlightOnTime);
            mALogger.mo11957d(sb.toString());
            if (DisplayUtils.isLidClosed()) {
                flashOnChopAnalytics.recordFlashlightOnEventClosedLid();
            } else {
                flashOnChopAnalytics.recordFlashlightOnEvent(z);
            }
        }
    }

    public static synchronized void recordFlashlightOffEvents() {
        synchronized (FlashOnChopInstrumentation.class) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("recordFlashlightOffEvents (isChopChop = ");
            sb.append(sIsChopChopLastFlashlightSource);
            sb.append(") - eventTime = ");
            sb.append(elapsedRealtime);
            mALogger.mo11957d(sb.toString());
            recordChopChopFlashlightOnTime(elapsedRealtime);
            sLastFlashlightOnTime = -1;
        }
    }

    public static synchronized void recordGyroThresholdTriggeredEvents(int i) {
        synchronized (FlashOnChopInstrumentation.class) {
            if (DisplayUtils.isLidClosed()) {
                getFlashOnChopAnalytics().recordGyroThresholdTriggeredEventsClosedLid(i);
            } else {
                getFlashOnChopAnalytics().recordGyroThresholdTriggeredEvents(i);
            }
        }
    }

    public static synchronized void recordFlashlightTimedOutEvent() {
        synchronized (FlashOnChopInstrumentation.class) {
            if (DisplayUtils.isLidClosed()) {
                getFlashOnChopAnalytics().recordFlashlightTimedOutEventClosedLid();
            } else {
                getFlashOnChopAnalytics().recordFlashlightTimedOutEvent();
            }
        }
    }

    public static synchronized void startRecordTimeEnabledSecs() {
        synchronized (FlashOnChopInstrumentation.class) {
            if (sUsageTimeTrackingStartMs > 0) {
                LOGGER.mo11959e("startTimeEnabledSecs was done two time without stopTimeEnabledSecs");
            }
            sUsageTimeTrackingStartMs = SystemClock.elapsedRealtime();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecordTimeEnabledSecs: Start time = ");
            sb.append(sUsageTimeTrackingStartMs);
            mALogger.mo11957d(sb.toString());
        }
    }

    public static synchronized void stopRecordTimeEnabledSecs() {
        synchronized (FlashOnChopInstrumentation.class) {
            if (sUsageTimeTrackingStartMs <= 0) {
                LOGGER.mo11957d("Invalid time for ON");
                return;
            }
            int elapsedRealtime = ((int) (SystemClock.elapsedRealtime() - sUsageTimeTrackingStartMs)) / 1000;
            getFlashOnChopAnalytics().recordTimeEnabledSecs(elapsedRealtime);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("stopRecordTimeEnabledSecs: Stop time = ");
            sb.append(sUsageTimeTrackingStartMs);
            mALogger.mo11957d(sb.toString());
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("stopRecordTimeEnabledSecs: numberSecs = ");
            sb2.append(elapsedRealtime);
            mALogger2.mo11957d(sb2.toString());
            sUsageTimeTrackingStartMs = -1;
        }
    }

    public static synchronized boolean isRecordingTimeEnabledSecs() {
        boolean z;
        synchronized (FlashOnChopInstrumentation.class) {
            z = sUsageTimeTrackingStartMs > 0;
        }
        return z;
    }

    public static synchronized void recordDailyToggleEvent(boolean z) {
        synchronized (FlashOnChopInstrumentation.class) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Record FOC - Daily Toggle Event: ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            if (DisplayUtils.isLidClosed()) {
                getFlashOnChopAnalytics().recordDailyToggleEventClosedLid();
            } else {
                getFlashOnChopAnalytics().recordDailyToggleEvent();
            }
        }
    }

    public static synchronized void recordCameraManagerEvent() {
        synchronized (FlashOnChopInstrumentation.class) {
            FlashOnChopAnalytics flashOnChopAnalytics = getFlashOnChopAnalytics();
            if (DisplayUtils.isLidClosed()) {
                flashOnChopAnalytics.recordCameraManagerEventClosedLid();
            } else {
                flashOnChopAnalytics.recordCameraManagerEvent();
            }
        }
    }

    public static synchronized void recordFlashlightControllerEvent() {
        synchronized (FlashOnChopInstrumentation.class) {
            FlashOnChopAnalytics flashOnChopAnalytics = getFlashOnChopAnalytics();
            if (DisplayUtils.isLidClosed()) {
                flashOnChopAnalytics.recordFlashlightControllerEventClosedLid();
            } else {
                flashOnChopAnalytics.recordFlashlightControllerEvent();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b1, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void recordChopChopFlashlightOnTime(long r11) {
        /*
            java.lang.Class<com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation> r0 = com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation.class
            monitor-enter(r0)
            long r1 = sLastFlashlightOnTime     // Catch:{ all -> 0x00b2 }
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 > 0) goto L_0x0014
            com.motorola.actions.utils.MALogger r11 = LOGGER     // Catch:{ all -> 0x00b2 }
            java.lang.String r12 = "Invalid time for ON"
            r11.mo11957d(r12)     // Catch:{ all -> 0x00b2 }
            monitor-exit(r0)
            return
        L_0x0014:
            com.motorola.actions.foc.instrumentation.FlashOnChopAnalytics r1 = getFlashOnChopAnalytics()     // Catch:{ all -> 0x00b2 }
            long r2 = sLastFlashlightOnTime     // Catch:{ all -> 0x00b2 }
            long r11 = r11 - r2
            r2 = 1000(0x3e8, double:4.94E-321)
            long r11 = r11 / r2
            boolean r2 = com.motorola.actions.utils.DisplayUtils.isLidClosed()     // Catch:{ all -> 0x00b2 }
            com.motorola.actions.utils.MALogger r3 = LOGGER     // Catch:{ all -> 0x00b2 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b2 }
            r4.<init>()     // Catch:{ all -> 0x00b2 }
            java.lang.String r5 = "isChopChop = "
            r4.append(r5)     // Catch:{ all -> 0x00b2 }
            boolean r5 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r4.append(r5)     // Catch:{ all -> 0x00b2 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x00b2 }
            r3.mo11957d(r4)     // Catch:{ all -> 0x00b2 }
            com.motorola.actions.utils.MALogger r3 = LOGGER     // Catch:{ all -> 0x00b2 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b2 }
            r4.<init>()     // Catch:{ all -> 0x00b2 }
            java.lang.String r5 = "onTimeSecs = "
            r4.append(r5)     // Catch:{ all -> 0x00b2 }
            r4.append(r11)     // Catch:{ all -> 0x00b2 }
            java.lang.String r5 = "s"
            r4.append(r5)     // Catch:{ all -> 0x00b2 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x00b2 }
            r3.mo11957d(r4)     // Catch:{ all -> 0x00b2 }
            r3 = 120(0x78, double:5.93E-322)
            r5 = 60
            r7 = 30
            r9 = 15
            if (r2 == 0) goto L_0x0083
            int r2 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r2 >= 0) goto L_0x0067
            r1.recordFlashlightOn0To15SecsClosedLid()     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x0067:
            int r2 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1))
            if (r2 >= 0) goto L_0x006f
            r1.recordFlashlightOn15To30SecsClosedLid()     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x006f:
            int r2 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r2 >= 0) goto L_0x0077
            r1.recordFlashlightOn30To60SecsClosedLid()     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x0077:
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 >= 0) goto L_0x007f
            r1.recordFlashlightOn60To120SecsClosedLid()     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x007f:
            r1.recordFlashlightOnOver120SecsClosedLid()     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x0083:
            int r2 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r2 >= 0) goto L_0x008d
            boolean r11 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r1.recordFlashlightOn0To15Secs(r11)     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x008d:
            int r2 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1))
            if (r2 >= 0) goto L_0x0097
            boolean r11 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r1.recordFlashlightOn15To30Secs(r11)     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x0097:
            int r2 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r2 >= 0) goto L_0x00a1
            boolean r11 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r1.recordFlashlightOn30To60Secs(r11)     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x00a1:
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 >= 0) goto L_0x00ab
            boolean r11 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r1.recordFlashlightOn60To120Secs(r11)     // Catch:{ all -> 0x00b2 }
            goto L_0x00b0
        L_0x00ab:
            boolean r11 = sIsChopChopLastFlashlightSource     // Catch:{ all -> 0x00b2 }
            r1.recordFlashlightOnOver120Secs(r11)     // Catch:{ all -> 0x00b2 }
        L_0x00b0:
            monitor-exit(r0)
            return
        L_0x00b2:
            r11 = move-exception
            monitor-exit(r0)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation.recordChopChopFlashlightOnTime(long):void");
    }
}
