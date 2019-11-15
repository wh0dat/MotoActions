package com.motorola.actions.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.utils.MALogger;

class ScreenOnOffReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(ScreenOnOffReceiver.class);
    private ScreenEventListener mListener;
    private boolean mRegistered;

    public interface ScreenEventListener {
        void onScreenOff();

        void onScreenOn();
    }

    ScreenOnOffReceiver(ScreenEventListener screenEventListener) {
        this.mListener = screenEventListener;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onReceive: ");
        sb.append(action);
        mALogger.mo11957d(sb.toString());
        if (this.mListener == null) {
            return;
        }
        if (PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON.equals(action)) {
            this.mListener.onScreenOff();
        } else if ("android.intent.action.SCREEN_ON".equals(action)) {
            this.mListener.onScreenOn();
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
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
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
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
