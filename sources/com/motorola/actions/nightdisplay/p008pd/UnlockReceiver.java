package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;

/* renamed from: com.motorola.actions.nightdisplay.pd.UnlockReceiver */
public class UnlockReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(UnlockReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    UnlockReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        LOGGER.mo11957d("onReceive");
        if (intent != null && SecurityUtils.isUserUnlocked()) {
            if ("android.intent.action.USER_PRESENT".equals(intent.getAction()) || "android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                this.mPdHandlerThread.event(new PIEvent(Event.UNLOCKED));
            }
        }
    }

    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

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
                LOGGER.mo11960e("Unable to unregister UnlockReceiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
