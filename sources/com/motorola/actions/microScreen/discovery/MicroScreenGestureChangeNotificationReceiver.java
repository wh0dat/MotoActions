package com.motorola.actions.microScreen.discovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.utils.MALogger;

public class MicroScreenGestureChangeNotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_NO_THANKS = "com.motorola.actions.microScreen.discovery.ACTION_NO_THANKS";
    public static final String ACTION_OPEN_MICROSCREEN_SETTINGS = "com.motorola.actions.microScreen.discovery.ACTION_OPEN_MICROSCREEN_SETTINGS";
    private static final MALogger LOGGER = new MALogger(MicroScreenGestureChangeNotificationReceiver.class);
    private final MicroScreenGestureChangeNotification mMicroScreenGestureChangeNotification;
    private boolean mRegistered;

    public MicroScreenGestureChangeNotificationReceiver(MicroScreenGestureChangeNotification microScreenGestureChangeNotification) {
        this.mMicroScreenGestureChangeNotification = microScreenGestureChangeNotification;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive: ");
            sb.append(action);
            mALogger.mo11957d(sb.toString());
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != -2010654723) {
                if (hashCode == 2095466301 && action.equals(ACTION_OPEN_MICROSCREEN_SETTINGS)) {
                    c = 1;
                }
            } else if (action.equals(ACTION_NO_THANKS)) {
                c = 0;
            }
            switch (c) {
                case 0:
                    this.mMicroScreenGestureChangeNotification.dismissMicroScreenGestureChangeNotification();
                    break;
                case 1:
                    this.mMicroScreenGestureChangeNotification.dismissMicroScreenGestureChangeNotification();
                    context.startActivity(SettingsActivity.getFeatureLaunchIntent(FeatureKey.MICROSCREEN.ordinal()));
                    break;
                default:
                    MALogger mALogger2 = LOGGER;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onReceive - Not valid action: ");
                    sb2.append(intent.getAction());
                    mALogger2.mo11957d(sb2.toString());
                    break;
            }
            unregister();
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
            intentFilter.addAction(ACTION_NO_THANKS);
            intentFilter.addAction(ACTION_OPEN_MICROSCREEN_SETTINGS);
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
                LOGGER.mo11960e("Unable to unregister Microscreen notification receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
