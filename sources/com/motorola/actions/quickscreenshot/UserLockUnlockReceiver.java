package com.motorola.actions.quickscreenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.quickscreenshot.service.EventRegistration;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;

public final class UserLockUnlockReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(UserLockUnlockReceiver.class);
    private final EventRegistration mRegisterInterface;
    private boolean mRegistered;

    public UserLockUnlockReceiver(EventRegistration eventRegistration) {
        this.mRegisterInterface = eventRegistration;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Received ");
            sb.append(action);
            mALogger.mo11957d(sb.toString());
            if (!PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON.equals(action) && !"android.intent.action.USER_PRESENT".equals(action) && !"android.intent.action.SCREEN_ON".equals(action)) {
                return;
            }
            if (SecurityUtils.isUserUnlocked()) {
                LOGGER.mo11957d("Restore quick screenshot due to screen being unlocked");
                this.mRegisterInterface.onUserUnlocked();
                return;
            }
            LOGGER.mo11959e("Disable quick screenshot due to screen being locked/off");
            this.mRegisterInterface.onUserLocked();
            MotorolaSettings.setQuickScreenshotMode(0);
        }
    }

    public void register() {
        if (!this.mRegistered) {
            LOGGER.mo11957d("UserLockedReceiver registered");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
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
