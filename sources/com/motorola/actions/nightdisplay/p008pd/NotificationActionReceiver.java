package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.NotificationActionReceiver */
class NotificationActionReceiver extends BroadcastReceiver {
    public static final String ACTION_NIGHT_DISPLAY_PAUSE = "com.motorola.actions.nightDisplay.pd.ACTION_NIGHT_DISPLAY_PAUSE";
    public static final String ACTION_NIGHT_DISPLAY_RESUME = "com.motorola.actions.nightDisplay.pd.ACTION_NIGHT_DISPLAY_RESUME";
    private static final MALogger LOGGER = new MALogger(NotificationActionReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    NotificationActionReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (ACTION_NIGHT_DISPLAY_PAUSE.equals(intent.getAction())) {
            this.mPdHandlerThread.event(new PIEvent(Event.SERVICE_PAUSED));
        } else if (ACTION_NIGHT_DISPLAY_RESUME.equals(intent.getAction())) {
            this.mPdHandlerThread.event(new PIEvent(Event.SERVICE_RESUMED));
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_NIGHT_DISPLAY_PAUSE);
            intentFilter.addAction(ACTION_NIGHT_DISPLAY_RESUME);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister notification action receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
