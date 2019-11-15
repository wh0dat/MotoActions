package com.motorola.actions.onenav;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import java.util.HashSet;

public class ScreenLockNotificationManager implements ActivityLifecycleCallbacks {
    private static final MALogger LOGGER = new MALogger(ScreenLockNotificationManager.class);
    private static final long TIMEOUT_DELAY = 1000;
    private HashSet<Activity> mActiveActivities = new HashSet<>();
    private Handler mHandler = new Handler();
    private ScreenLockNotification mScreenLockNotification = new ScreenLockNotification();
    private Runnable mTimeout = new ScreenLockNotificationManager$$Lambda$0(this);

    private static class SingletonHolder {
        static final ScreenLockNotificationManager INSTANCE = new ScreenLockNotificationManager();

        private SingletonHolder() {
        }
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onActivityResumed:");
        sb.append(activity);
        mALogger.mo11957d(sb.toString());
        this.mActiveActivities.add(activity);
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onActivityResumed:#activities now=");
        sb2.append(this.mActiveActivities.size());
        mALogger2.mo11957d(sb2.toString());
        this.mHandler.removeCallbacks(this.mTimeout);
    }

    public void onActivityStopped(Activity activity) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onActivityStopped:");
        sb.append(activity);
        mALogger.mo11957d(sb.toString());
        this.mActiveActivities.remove(activity);
        PowerManager powerManager = (PowerManager) ActionsApplication.getAppContext().getSystemService("power");
        KeyguardManager keyguardManager = (KeyguardManager) ActionsApplication.getAppContext().getSystemService("keyguard");
        if (powerManager == null || keyguardManager == null) {
            LOGGER.mo11959e("Could not retrieve access to power manager or keyguard manager");
        } else if (this.mActiveActivities.isEmpty() && powerManager.isInteractive() && !keyguardManager.isDeviceLocked()) {
            this.mHandler.postDelayed(this.mTimeout, TIMEOUT_DELAY);
        }
    }

    /* access modifiers changed from: 0000 */
    public void triggerIfMotoActionsInBackground() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("triggerIfMotoActionsInBackground. #activities=");
        sb.append(this.mActiveActivities.size());
        mALogger.mo11957d(sb.toString());
        if (this.mActiveActivities.isEmpty()) {
            this.mScreenLockNotification.triggerNotification();
        }
    }

    public static ScreenLockNotificationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
