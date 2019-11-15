package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.UserChangedReceiver */
class UserChangedReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(UserChangedReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    UserChangedReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Received ");
        sb.append(intent.getAction());
        mALogger.mo11957d(sb.toString());
        this.mPdHandlerThread.event(new PIEvent(Event.USER_CHANGED));
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_FOREGROUND");
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
                LOGGER.mo11960e("Unable to unregister userchange-receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
