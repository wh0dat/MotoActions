package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.ScreenOnOffReceiver */
class ScreenOnOffReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(ScreenOnOffReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    ScreenOnOffReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON.equals(action)) {
            this.mPdHandlerThread.event(new PIEvent(Event.SCREEN_OFF));
        } else if ("android.intent.action.SCREEN_ON".equals(action)) {
            this.mPdHandlerThread.event(new PIEvent(Event.SCREEN_ON));
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON);
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
                LOGGER.mo11960e("Unable to unregister screen-on-off-receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
