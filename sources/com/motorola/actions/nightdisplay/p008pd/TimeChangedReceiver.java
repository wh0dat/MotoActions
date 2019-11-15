package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.TimeChangedReceiver */
class TimeChangedReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(TimeChangedReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    TimeChangedReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        Event event = Event.TIME_CHANGED;
        if ("android.intent.action.DATE_CHANGED".equals(intent.getAction()) || "android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction()) || "android.intent.action.TIME_SET".equals(intent.getAction())) {
            event = Event.TIME_CONFIG_CHANGED;
        }
        this.mPdHandlerThread.event(new PIEvent(event));
        if (!"android.intent.action.TIME_TICK".equals(intent.getAction())) {
            DailyAlarmManager.createAlarm();
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_TICK");
            intentFilter.addAction("android.intent.action.DATE_CHANGED");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_SET");
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
                LOGGER.mo11960e("Unable to unregister timechange-receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
