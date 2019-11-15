package com.motorola.actions.attentivedisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public class PreDimReceiver extends BroadcastReceiver {
    private static final String ACTION_SCREEN_PRE_DIM = "com.motorola.server.power.ACTION_SCREEN_PRE_DIM";
    private static final MALogger LOGGER = new MALogger(PreDimReceiver.class);
    private boolean mRegistered;

    private static class SingletonHolder {
        static final PreDimReceiver INSTANCE = new PreDimReceiver();

        private SingletonHolder() {
        }
    }

    public static PreDimReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onReceive(Context context, Intent intent) {
        LOGGER.mo11957d("onReceive");
        if (AttentiveDisplayInitService.shouldRun(context)) {
            AttentiveDisplayService.start(context);
            return;
        }
        AttentiveDisplayInitService.updateAttentiveDisplaySettingsFlag(AttentiveDisplayInitService.shouldRun(context));
        unregister();
    }

    public void unregister() {
        LOGGER.mo11957d("unregister");
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister pre dim receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }

    public void register() {
        LOGGER.mo11957d("register");
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SCREEN_PRE_DIM);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }
}
