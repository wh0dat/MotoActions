package com.motorola.actions.quickscreenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

public final class PowerOffReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(PowerOffReceiver.class);
    private boolean mRegistered;

    public void onReceive(Context context, Intent intent) {
        if (intent != null && "android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
            LOGGER.mo11957d("Received android.intent.action.ACTION_SHUTDOWN");
            MotorolaSettings.setQuickScreenshotMode(0);
        }
    }

    public void register() {
        if (!this.mRegistered) {
            LOGGER.mo11957d("Receiver registered");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        if (this.mRegistered) {
            try {
                LOGGER.mo11957d("Receiver unregistered");
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
