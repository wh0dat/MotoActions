package com.motorola.actions.discovery.highlight.onenav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SetupObserver;

class UserLockReceiver extends BroadcastReceiver {
    public static final String KEY_SOFT_ONE_NAV_UNLOCK = "softonenav_unlock";
    private static final MALogger LOGGER = new MALogger(UserLockReceiver.class);
    private static final int MAX_UNLOCK = 10;
    private PowerManager mPowerManager;
    private boolean mRegistered;
    private boolean mScreenOff;

    UserLockReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Received ");
            sb.append(action);
            mALogger.mo11957d(sb.toString());
            if ("android.intent.action.SCREEN_OFF".equals(action) && SetupObserver.isSetupFinished() && !this.mPowerManager.isInteractive()) {
                this.mScreenOff = true;
            } else if ("android.intent.action.USER_PRESENT".equals(action) && this.mScreenOff) {
                this.mScreenOff = false;
                int i = SharedPreferenceManager.getInt(KEY_SOFT_ONE_NAV_UNLOCK, 0) + 1;
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("UnlockCount=");
                sb2.append(i);
                mALogger2.mo11957d(sb2.toString());
                if (i >= 10 && MotorolaSettings.getSoftOneNavDiscovery() == 0) {
                    DiscoveryManager.getInstance().onHighlightEvent(FeatureHighlightKey.SOFTONENAV);
                }
                SharedPreferenceManager.putInt(KEY_SOFT_ONE_NAV_UNLOCK, i);
            }
        }
    }

    public void register() {
        if (!this.mRegistered) {
            this.mScreenOff = false;
            LOGGER.mo11957d("UserLockedReceiver registered");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mPowerManager = (PowerManager) ActionsApplication.getAppContext().getSystemService(PowerManager.class);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        if (this.mRegistered) {
            try {
                LOGGER.mo11957d("UserLockUnlockReceiver unregistered");
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister notification action receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                LOGGER.mo11957d("unregister()");
                throw th;
            }
            this.mRegistered = false;
            LOGGER.mo11957d("unregister()");
        }
    }
}
