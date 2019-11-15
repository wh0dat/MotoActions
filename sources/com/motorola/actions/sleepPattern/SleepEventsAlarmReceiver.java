package com.motorola.actions.sleepPattern;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import javax.inject.Inject;

public class SleepEventsAlarmReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(SleepEventsAlarmReceiver.class);
    private static final String SLEEP_ALARM_EVENT = "sleep_alarm_event";
    private ProcessSleepEventThread mProcessSleepEventThread;
    private boolean mRegistered;

    @Inject
    SleepEventsAlarmReceiver(ProcessSleepEventThread processSleepEventThread) {
        this.mProcessSleepEventThread = processSleepEventThread;
    }

    public void onReceive(Context context, Intent intent) {
        LOGGER.mo11957d("onReceive");
        this.mProcessSleepEventThread.processReadAccelEvent();
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SLEEP_ALARM_EVENT);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }

    public static PendingIntent getSleepPatternPendingIntent(Context context) {
        LOGGER.mo11957d("getSleepPatternPendingIntent");
        Intent intent = new Intent();
        intent.setAction(SLEEP_ALARM_EVENT);
        intent.setPackage(context.getPackageName());
        return PendingIntent.getBroadcast(context, 0, intent, 134217728);
    }
}
