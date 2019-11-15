package com.motorola.actions.microScreen.instrumentation;

import android.os.SystemClock;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.MALogger;
import java.util.concurrent.TimeUnit;

public class MicroScreenInstrumentation {
    private static final MALogger LOGGER = new MALogger(MicroScreenInstrumentation.class);
    private static String sActivationMethod;
    private static String sActiveAppDuringGesture;
    private static long sStartTimeCounter;

    private static MicroScreenAnalytics getMicroScreenAnalytics() {
        return (MicroScreenAnalytics) CheckinAlarm.getInstance().getAnalytics(MicroScreenAnalytics.class);
    }

    public static void registerMicroScreenOn(String str, String str2) {
        sStartTimeCounter = SystemClock.elapsedRealtime();
        sActiveAppDuringGesture = str;
        sActivationMethod = screenPositionToActivationMethod(str2);
    }

    public static void registerMicroScreenOff(String str) {
        MicroScreenAnalytics microScreenAnalytics = getMicroScreenAnalytics();
        if (microScreenAnalytics != null) {
            int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(SystemClock.elapsedRealtime() - sStartTimeCounter);
            microScreenAnalytics.recordMicroScreenOff(seconds, sActiveAppDuringGesture, sActivationMethod, str);
            microScreenAnalytics.recordTotalDuration(seconds);
        }
    }

    public static synchronized void recordActivationWithOneNavDailyEvent() {
        synchronized (MicroScreenInstrumentation.class) {
            getMicroScreenAnalytics().recordActivationWithOneNavDailyEvent();
        }
    }

    public static synchronized void recordActivationDailyEvent() {
        synchronized (MicroScreenInstrumentation.class) {
            getMicroScreenAnalytics().recordActivationDailyEvent();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0045  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String screenPositionToActivationMethod(java.lang.String r2) {
        /*
            int r0 = r2.hashCode()
            r1 = -1364013995(0xffffffffaeb2cc55, float:-8.1307995E-11)
            if (r0 == r1) goto L_0x0028
            r1 = 3317767(0x32a007, float:4.649182E-39)
            if (r0 == r1) goto L_0x001e
            r1 = 108511772(0x677c21c, float:4.6598146E-35)
            if (r0 == r1) goto L_0x0014
            goto L_0x0032
        L_0x0014:
            java.lang.String r0 = "right"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0032
            r2 = 1
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "left"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0032
            r2 = 0
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "center"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0032
            r2 = 2
            goto L_0x0033
        L_0x0032:
            r2 = -1
        L_0x0033:
            switch(r2) {
                case 0: goto L_0x0045;
                case 1: goto L_0x0042;
                case 2: goto L_0x003f;
                default: goto L_0x0036;
            }
        L_0x0036:
            com.motorola.actions.utils.MALogger r2 = LOGGER
            java.lang.String r0 = "Unknown screenPosition"
            r2.mo11959e(r0)
            r2 = 0
            goto L_0x0047
        L_0x003f:
            java.lang.String r2 = "C"
            goto L_0x0047
        L_0x0042:
            java.lang.String r2 = "R"
            goto L_0x0047
        L_0x0045:
            java.lang.String r2 = "L"
        L_0x0047:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.microScreen.instrumentation.MicroScreenInstrumentation.screenPositionToActivationMethod(java.lang.String):java.lang.String");
    }
}
