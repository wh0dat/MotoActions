package com.motorola.actions.lts.instrumentation;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.utils.DisplayUtils;
import com.motorola.actions.utils.MALogger;

public class LiftToSilenceInstrumentation {
    public static final String KEY_LIFT_SILENCED = "lift";
    private static final String KEY_OTHER_SILENCED = "other";
    private static final String KEY_POWER_SILENCED = "power button";
    private static final String KEY_SCREEN_STATE_BEFORE_CALL = "SCREEN_STATE_BEFORE_CALL";
    private static final String KEY_SILENCED_CAUSE = "SILENCED_CAUSE";
    private static final MALogger LOGGER = new MALogger(LiftToSilenceInstrumentation.class);
    private static final boolean SCREEN_STATE_NOT_SET_DEFAULT = false;
    private static HandlerThread sHandlerThread;
    /* access modifiers changed from: private */
    public static long sStartTimeCounter;
    private static long sTimeVolumeDecreased;
    private static Handler sWorker;

    private static synchronized Handler getHandlerThread() {
        Handler handler;
        synchronized (LiftToSilenceInstrumentation.class) {
            if (sHandlerThread == null) {
                sHandlerThread = new HandlerThread("lts instrumentation worker thread");
                sHandlerThread.start();
                sWorker = new Handler(sHandlerThread.getLooper());
            }
            handler = sWorker;
        }
        return handler;
    }

    public static synchronized void setSilencedCause(String str) {
        synchronized (LiftToSilenceInstrumentation.class) {
            getHandlerThread().post(new LiftToSilenceInstrumentation$$Lambda$0(str));
        }
    }

    static final /* synthetic */ void lambda$setSilencedCause$0$LiftToSilenceInstrumentation(String str) {
        if (!str.equals(KEY_OTHER_SILENCED) || SharedPreferenceManager.getString(KEY_SILENCED_CAUSE, KEY_OTHER_SILENCED).equals(KEY_OTHER_SILENCED)) {
            SharedPreferenceManager.putString(KEY_SILENCED_CAUSE, str);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setSilencedCause ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
    }

    public static void setTimeVolumeDecreased() {
        getHandlerThread().post(LiftToSilenceInstrumentation$$Lambda$1.$instance);
    }

    static final /* synthetic */ void lambda$setTimeVolumeDecreased$1$LiftToSilenceInstrumentation() {
        sTimeVolumeDecreased = SystemClock.elapsedRealtime();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setTimeVolumeDecreased ");
        sb.append(sTimeVolumeDecreased);
        mALogger.mo11957d(sb.toString());
    }

    public static synchronized void triggerStartTimeCounter() {
        synchronized (LiftToSilenceInstrumentation.class) {
            getHandlerThread().post(LiftToSilenceInstrumentation$$Lambda$2.$instance);
        }
    }

    public static synchronized void setRingingEnded() {
        synchronized (LiftToSilenceInstrumentation.class) {
            getHandlerThread().post(LiftToSilenceInstrumentation$$Lambda$3.$instance);
        }
    }

    static final /* synthetic */ void lambda$setRingingEnded$3$LiftToSilenceInstrumentation() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Set ringing ended ");
        sb.append(sStartTimeCounter);
        mALogger.mo11957d(sb.toString());
        if (sStartTimeCounter != 0) {
            String str = null;
            int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - sStartTimeCounter);
            int i = 0;
            boolean z = SharedPreferenceManager.getBoolean(KEY_SCREEN_STATE_BEFORE_CALL, false);
            SharedPreferenceManager.remove(KEY_SCREEN_STATE_BEFORE_CALL);
            if (sTimeVolumeDecreased > 0) {
                str = SharedPreferenceManager.getString(KEY_SILENCED_CAUSE, KEY_OTHER_SILENCED);
                if (!str.equals(KEY_OTHER_SILENCED)) {
                    int i2 = (int) (sTimeVolumeDecreased - sStartTimeCounter);
                    if (i2 > 0) {
                        i = i2;
                    }
                }
                SharedPreferenceManager.remove(KEY_SILENCED_CAUSE);
                sTimeVolumeDecreased = 0;
            }
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Silenced cause: ");
            sb2.append(str);
            mALogger2.mo11957d(sb2.toString());
            MALogger mALogger3 = LOGGER;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Delay to silence: ");
            sb3.append(i);
            mALogger3.mo11957d(sb3.toString());
            MALogger mALogger4 = LOGGER;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Time to answer: ");
            sb4.append(elapsedRealtime);
            mALogger4.mo11957d(sb4.toString());
            if (DisplayUtils.isLidClosed()) {
                getLiftToSilenceAnalytics().recordRingingEndedAfterSilencedClosedLid(str, z, elapsedRealtime, i);
            } else {
                getLiftToSilenceAnalytics().recordRingingEndedAfterSilenced(str, z, elapsedRealtime, i);
            }
            sStartTimeCounter = 0;
        }
    }

    public static synchronized void setScreenStateBeforeCall() {
        synchronized (LiftToSilenceInstrumentation.class) {
            getHandlerThread().post(LiftToSilenceInstrumentation$$Lambda$4.$instance);
        }
    }

    public static synchronized void powerButtonPressed() {
        synchronized (LiftToSilenceInstrumentation.class) {
            setTimeVolumeDecreased();
            setSilencedCause(KEY_POWER_SILENCED);
        }
    }

    private static LiftToSilenceAnalytics getLiftToSilenceAnalytics() {
        return (LiftToSilenceAnalytics) CheckinAlarm.getInstance().getAnalytics(LiftToSilenceAnalytics.class);
    }

    public static void recordLiftToSilenceDailyEvents() {
        if (DisplayUtils.isLidClosed()) {
            getLiftToSilenceAnalytics().recordLiftToSilenceDailyEventsClosedLid();
        } else {
            getLiftToSilenceAnalytics().recordLiftToSilenceDailyEvents();
        }
    }
}
