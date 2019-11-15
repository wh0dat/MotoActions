package com.motorola.actions.attentivedisplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.attentivedisplay.PreDimReceiver;
import com.motorola.actions.attentivedisplay.notification.ADBatterySaverNotificationReceiver;
import com.motorola.actions.attentivedisplay.notification.ADDisabledBatteryNotification;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;

public class BatterySaverReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(BatterySaverReceiver.class);
    private boolean mRegistered;

    private static class SingletonHolder {
        static final BatterySaverReceiver INSTANCE = new BatterySaverReceiver();

        private SingletonHolder() {
        }
    }

    public static BatterySaverReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onReceive(Context context, Intent intent) {
        LOGGER.mo11957d("onReceive");
        if (intent == null || !"android.os.action.POWER_SAVE_MODE_CHANGED".equals(intent.getAction())) {
            LOGGER.mo11959e("onReceive: missing action");
            return;
        }
        LOGGER.mo11957d("onReceive: action = android.os.action.POWER_SAVE_MODE_CHANGED");
        handlePowerSaveModeChanged();
    }

    private void handlePowerSaveModeChanged() {
        boolean isPowerSaveMode = ((PowerManager) ActionsApplication.getAppContext().getSystemService("power")).isPowerSaveMode();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isPowerSaveMode ");
        sb.append(isPowerSaveMode);
        mALogger.mo11957d(sb.toString());
        if (isPowerSaveMode) {
            ADDisabledBatteryNotification aDDisabledBatteryNotification = new ADDisabledBatteryNotification();
            ADBatterySaverNotificationReceiver.getInstance().register();
            aDDisabledBatteryNotification.showNotification();
            PreDimReceiver.getInstance().unregister();
            AttentiveDisplayService.stop(ActionsApplication.getAppContext());
            return;
        }
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_BATTERY_SAVER_INCOMPATIBILITY.ordinal());
        ADBatterySaverNotificationReceiver.getInstance().unregister();
        PreDimReceiver.getInstance().register();
    }

    public void register() {
        boolean z = SharedPreferenceManager.getBoolean(ADBatterySaverNotificationReceiver.SHOULD_NOT_SHOW_AGAIN_AD_BATTERY_PREFERENCE_KEY, false);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered ");
        sb.append(this.mRegistered);
        sb.append(", shouldNotShowAgain ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered && !z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            handlePowerSaveModeChanged();
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister BatterySaverReceiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
