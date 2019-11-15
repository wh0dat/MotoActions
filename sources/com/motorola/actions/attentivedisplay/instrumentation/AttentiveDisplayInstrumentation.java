package com.motorola.actions.attentivedisplay.instrumentation;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.attentivedisplay.util.ScreenTimeoutControl;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;

public class AttentiveDisplayInstrumentation {
    private static final long FALSE_NEGATIVE_TIME_THRESHOLD_MILLIS = 2000;
    private long mLastScreenDimTime;
    private int mLastScreenOffReason;
    private long mLastScreenOffTime;
    private boolean mLastSessionNotExtended;
    private boolean mObjectStateInLastResult;

    private AttentiveDisplayAnalytics getAttentiveDisplayAnalytics() {
        return (AttentiveDisplayAnalytics) CheckinAlarm.getInstance().getAnalytics(AttentiveDisplayAnalytics.class);
    }

    private synchronized void recordDailyScreenOnEvent() {
        getAttentiveDisplayAnalytics().recordDailyScreenOnEvent();
    }

    private synchronized void recordDailyScreenOffEvent() {
        getAttentiveDisplayAnalytics().recordDailyScreenOffEvent();
    }

    private synchronized void recordDailyScreenBrightEvent() {
        getAttentiveDisplayAnalytics().recordDailyScreenBrightEvent();
    }

    private synchronized void recordDailyScreenDimEvent() {
        getAttentiveDisplayAnalytics().recordDailyScreenDimEvent();
    }

    private synchronized void recordDailyScreenPreDimEvent() {
        getAttentiveDisplayAnalytics().recordDailyScreenPreDimEvent();
    }

    private synchronized void recordDailySessionsShortenedEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsShortenedEvent();
    }

    private synchronized void recordDailySessionsExtendedEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsExtendedEvent();
    }

    private synchronized void recordDailySessionsNotExtendedEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsNotExtendedEvent();
    }

    private synchronized void recordDailySessionsAbortedEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsAbortedEvent();
    }

    private synchronized void recordDailyFalseScreenDimNoFaceEvent() {
        getAttentiveDisplayAnalytics().recordDailyFalseScreenDimNoFaceEvent();
    }

    private synchronized void recordDailyFalseScreenDimNoObjectEvent() {
        getAttentiveDisplayAnalytics().recordDailyFalseScreenDimNoObjectEvent();
    }

    private synchronized void recordDailyFalseScreenOffNoFaceEvent() {
        getAttentiveDisplayAnalytics().recordDailyFalseScreenOffNoFaceEvent();
    }

    private synchronized void recordDailyFalseScreenOffNoObjectEvent() {
        getAttentiveDisplayAnalytics().recordDailyFalseScreenOffNoObjectEvent();
    }

    private synchronized void recordDailyScreenBrightTimeSavedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyScreenBrightTimeSavedMillis(i);
    }

    private synchronized void recordDailyScreenDimTimeSavedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyScreenDimTimeSavedMillis(i);
    }

    private synchronized void recordDailyCameraOnTimeExtendedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyCameraOnTimeExtendedMillis(i);
    }

    private synchronized void recordDailyCameraOnTimeNotExtendedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyCameraOnTimeNotExtendedMillis(i);
    }

    private synchronized void recordDailyCameraOnTimeAbortedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyCameraOnTimeAbortedMillis(i);
    }

    private synchronized void recordDailyCameraStartedNotExtendedEvent() {
        getAttentiveDisplayAnalytics().recordDailyCameraStartedNotExtendedEvent();
    }

    private synchronized void recordDailyCameraStartedAbortedEvent() {
        getAttentiveDisplayAnalytics().recordDailyCameraStartedAbortedEvent();
    }

    private synchronized void recordDailyObjectDetectionTimeExtendedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyObjectDetectionTimeExtendedMillis(i);
    }

    private synchronized void recordDailyObjectDetectionTimeNotExtendedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyObjectDetectionTimeNotExtendedMillis(i);
    }

    private synchronized void recordDailyObjectDetectionTimeAbortedMillis(int i) {
        getAttentiveDisplayAnalytics().recordDailyObjectDetectionTimeAbortedMillis(i);
    }

    private synchronized void recordDailyObjectDetectionStartedNotExtendedEvent() {
        getAttentiveDisplayAnalytics().recordDailyObjectDetectionStartedNotExtendedEvent();
    }

    private synchronized void recordDailyObjectDetectionStartedAbortedEvent() {
        getAttentiveDisplayAnalytics().recordDailyObjectDetectionStartedAbortedEvent();
    }

    private synchronized void resetLastCounters() {
        this.mObjectStateInLastResult = false;
        this.mLastScreenDimTime = 0;
        this.mLastScreenOffTime = 0;
        this.mLastSessionNotExtended = false;
        this.mLastScreenOffReason = -1;
    }

    private void calculateAndRecordScreenTimeSaved() {
        if (AttentiveDisplaySettingsFragment.isGoToSleepEnabled()) {
            int screenTimeout = ScreenTimeoutControl.getScreenTimeout(ActionsApplication.getAppContext());
            if (screenTimeout != -1) {
                double d = (double) screenTimeout;
                double d2 = 0.2d * d;
                if (d2 > 7000.0d) {
                    d2 = 7000.0d;
                }
                double d3 = d - d2;
                double d4 = (double) (((float) screenTimeout) * 0.8f);
                double min = Math.min(d2, 5000.0d);
                recordDailyScreenBrightTimeSavedMillis((int) Math.max(0.0d, d3 - (d4 - min)));
                recordDailyScreenDimTimeSavedMillis((int) Math.max(0.0d, d2 - min));
            }
        }
    }

    public synchronized void recordDailySessionsAbortedErrorEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsAbortedErrorEvent();
    }

    public synchronized void recordDailySessionsAbortedUserActivityEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsAbortedUserActivityEvent();
    }

    public synchronized void recordDailySessionsAbortedTimoutEvent() {
        getAttentiveDisplayAnalytics().recordDailySessionsAbortedTimoutEvent();
    }

    public synchronized void onSessionAbortedCommon(int i, int i2) {
        recordDailySessionsAbortedEvent();
        if (i != -1) {
            recordDailyObjectDetectionStartedAbortedEvent();
            recordDailyObjectDetectionTimeAbortedMillis(i);
        }
        if (i2 != -1) {
            recordDailyCameraStartedAbortedEvent();
            recordDailyCameraOnTimeAbortedMillis(i2);
        }
    }

    public synchronized void onAttentiveDisplayResult(boolean z) {
        this.mObjectStateInLastResult = z;
    }

    public synchronized void onScreenPreDim() {
        recordDailyScreenPreDimEvent();
    }

    public synchronized void onScreenDim() {
        recordDailyScreenDimEvent();
        this.mLastScreenDimTime = System.currentTimeMillis();
    }

    public synchronized void onScreenBright(boolean z) {
        recordDailyScreenBrightEvent();
        long currentTimeMillis = System.currentTimeMillis();
        if (z && currentTimeMillis - this.mLastScreenDimTime < FALSE_NEGATIVE_TIME_THRESHOLD_MILLIS) {
            if (this.mObjectStateInLastResult) {
                recordDailyFalseScreenDimNoFaceEvent();
            } else {
                recordDailyFalseScreenDimNoObjectEvent();
            }
        }
        resetLastCounters();
    }

    public synchronized void onScreenOff(int i) {
        recordDailyScreenOffEvent();
        this.mLastScreenOffTime = System.currentTimeMillis();
        this.mLastScreenOffReason = i;
    }

    public synchronized void onScreenOn() {
        recordDailyScreenOnEvent();
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mLastScreenOffReason == PowerManagerPrivateProxy.GO_TO_SLEEP_REASON_TIMEOUT && this.mLastSessionNotExtended) {
            if (currentTimeMillis - this.mLastScreenOffTime >= FALSE_NEGATIVE_TIME_THRESHOLD_MILLIS) {
                calculateAndRecordScreenTimeSaved();
            } else if (this.mObjectStateInLastResult) {
                recordDailyFalseScreenOffNoFaceEvent();
            } else {
                recordDailyFalseScreenOffNoObjectEvent();
            }
        }
        resetLastCounters();
    }

    public void onSessionExtended(int i, int i2) {
        recordDailySessionsExtendedEvent();
        if (i2 != -1) {
            recordDailyCameraOnTimeExtendedMillis(i2);
        }
        if (i != -1) {
            recordDailyObjectDetectionTimeExtendedMillis(i);
        }
    }

    public synchronized void onSessionNotExtended(int i, int i2) {
        this.mLastSessionNotExtended = true;
        recordDailySessionsNotExtendedEvent();
        if (i != -1) {
            recordDailyObjectDetectionStartedNotExtendedEvent();
            recordDailyObjectDetectionTimeNotExtendedMillis(i);
        }
        if (i2 != -1) {
            recordDailyCameraStartedNotExtendedEvent();
            recordDailyCameraOnTimeNotExtendedMillis(i2);
        }
        if (AttentiveDisplaySettingsFragment.isGoToSleepEnabled()) {
            recordDailySessionsShortenedEvent();
        }
    }
}
