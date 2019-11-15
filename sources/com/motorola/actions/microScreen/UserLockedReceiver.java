package com.motorola.actions.microScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.view.Display;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.microScreen.instrumentation.MicroScreenInstrumentation;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.utils.MALogger;

public final class UserLockedReceiver extends BroadcastReceiver {
    private static final String INTENT_ACTION_USER_LOCKED = "com.motorola.internal.intent.action.USER_LOCKED";
    private static final MALogger LOGGER = new MALogger(UserLockedReceiver.class);
    private static UserLockedReceiver sInstance;
    private boolean mRegistered;

    private UserLockedReceiver() {
    }

    static synchronized UserLockedReceiver getInstance() {
        UserLockedReceiver userLockedReceiver;
        synchronized (UserLockedReceiver.class) {
            if (sInstance == null) {
                LOGGER.mo11957d("getInstance() : Creating a new UserLockedReceiver");
                sInstance = new UserLockedReceiver();
            }
            userLockedReceiver = sInstance;
        }
        return userLockedReceiver;
    }

    static synchronized void clean() {
        synchronized (UserLockedReceiver.class) {
            if (sInstance != null) {
                LOGGER.mo11957d("clean() : Unregister instance and cleanning it");
                sInstance.unregister();
                sInstance = null;
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Received ");
            sb.append(action);
            mALogger.mo11957d(sb.toString());
            if (INTENT_ACTION_USER_LOCKED.equals(action) && MicroScreenModel.isMicroScreenModeOn()) {
                LOGGER.mo11957d("Exiting microscreen due to screen being locked");
                MicroScreenService.stopMicroScreenService();
                MicroScreenNotificationManager.dismissNotification();
                saveInstrumentation();
                unregister();
            } else if (MicroScreenReceiver.MOTO_ACTIONS_MICROSCREEN_RECEIVER_ACTION.equals(action)) {
                unregister();
            }
        }
    }

    private void saveInstrumentation() {
        DisplayManager displayManager = (DisplayManager) ActionsApplication.getAppContext().getSystemService("display");
        if (displayManager != null) {
            for (Display state : displayManager.getDisplays()) {
                if (state.getState() == 2) {
                    LOGGER.mo11957d("Recording instrumentation due to screen being locked. Screen is ON.");
                    MicroScreenInstrumentation.registerMicroScreenOff("O");
                    return;
                }
            }
            return;
        }
        LOGGER.mo11959e("Unable to retrieve access to display manager");
    }

    /* access modifiers changed from: 0000 */
    public synchronized void register() {
        if (!this.mRegistered) {
            LOGGER.mo11957d("UserLockedReceiver registered");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(INTENT_ACTION_USER_LOCKED);
            intentFilter.addAction(MicroScreenReceiver.MOTO_ACTIONS_MICROSCREEN_RECEIVER_ACTION);
            ActionsApplication.getAppContext().registerReceiver(getInstance(), intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void unregister() {
        if (this.mRegistered) {
            try {
                LOGGER.mo11957d("UserLockedReceiver unregistered");
                ActionsApplication.getAppContext().unregisterReceiver(getInstance());
                this.mRegistered = false;
                LOGGER.mo11957d("unregister() : sInstance = null");
            } catch (IllegalArgumentException e) {
                try {
                    LOGGER.mo11960e("Unable to unregister notification action receiver", e);
                    this.mRegistered = false;
                    LOGGER.mo11957d("unregister() : sInstance = null");
                } catch (Throwable th) {
                    this.mRegistered = false;
                    LOGGER.mo11957d("unregister() : sInstance = null");
                    sInstance = null;
                    throw th;
                }
            }
            sInstance = null;
        }
    }
}
