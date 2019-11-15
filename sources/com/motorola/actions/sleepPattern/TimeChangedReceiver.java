package com.motorola.actions.sleepPattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public final class TimeChangedReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(TimeChangedReceiver.class);
    private boolean mRegistered;

    private static class SingletonHolder {
        static final TimeChangedReceiver INSTANCE = new TimeChangedReceiver();

        private SingletonHolder() {
        }
    }

    public static TimeChangedReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private TimeChangedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onReceive ");
        sb.append(intent.getAction());
        mALogger.mo11957d(sb.toString());
        FeatureManager.stop(context);
        FeatureManager.start(context);
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        LOGGER.mo11957d("register");
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_SET");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        LOGGER.mo11957d("unregister");
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister TimeChangedReceiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
