package com.motorola.actions.notificationchannel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

class LocaleChangedReceiver extends BroadcastReceiver {
    private final MALogger mLogger = new MALogger(LocaleChangedReceiver.class);
    private boolean mRegistered;

    LocaleChangedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        this.mLogger.mo11957d("onReceive");
        if (intent != null && "android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
            NotificationChannelManager.getInstance().createChannels();
        }
    }

    public void register() {
        if (!this.mRegistered) {
            this.mLogger.mo11957d("register");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        if (this.mRegistered) {
            try {
                this.mLogger.mo11957d("unregister");
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.mLogger.mo11958d("Could not unregister receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
