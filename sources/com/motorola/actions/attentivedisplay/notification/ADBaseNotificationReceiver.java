package com.motorola.actions.attentivedisplay.notification;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import java.util.List;

public abstract class ADBaseNotificationReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(ADBaseNotificationReceiver.class);
    private boolean mRegistered;

    /* access modifiers changed from: protected */
    public abstract List<String> getNotificationActions();

    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            List<String> notificationActions = getNotificationActions();
            if (notificationActions != null) {
                for (String addAction : notificationActions) {
                    intentFilter.addAction(addAction);
                }
            }
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister ADBaseNotificationReceiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
