package com.motorola.actions.onenav;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.p013ui.settings.ScreenLockSettings;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;

class ScreenLockNotification {
    private static final MALogger LOGGER = new MALogger(ScreenLockNotification.class);

    ScreenLockNotification() {
    }

    private PendingIntent createShowFingerprintLockScreenSettingsIntent() {
        LOGGER.mo11957d("Creating showFingerprintLockScreenSettingsIntent");
        Intent intent = new Intent();
        intent.setClass(ActionsApplication.getAppContext(), ScreenLockSettings.class);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getActivity(ActionsApplication.getAppContext(), 0, intent, ErrorDialogData.BINDER_CRASH);
    }

    private Notification createLockScreenSettingsNotification() {
        LOGGER.mo11957d("createLockScreenSettingsNotification");
        return new Builder(ActionsApplication.getAppContext(), ActionsNotificationChannel.GENERAL.name()).setSmallIcon(C0504R.C0505drawable.ic_moto_actions_notification).setColor(ActionsApplication.getAppContext().getColor(C0504R.color.notification_accent)).setContentTitle(ActionsApplication.getAppContext().getString(C0504R.string.onenav_lock_screen_notification_title)).setContentText(ActionsApplication.getAppContext().getString(C0504R.string.onenav_lock_screen_notification_content)).setContentIntent(createShowFingerprintLockScreenSettingsIntent()).setAutoCancel(true).setLocalOnly(true).build();
    }

    private void showNotification() {
        LOGGER.mo11957d("showNotification");
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.notify(NotificationId.ACTIONS_ONENAV_LOCKSCREEN_SETTINGS.ordinal(), createLockScreenSettingsNotification());
        } else {
            LOGGER.mo11963w("showNotification - Ignoring notification. notificationManager is NULL");
        }
    }

    private boolean hasEnrolledFingerprints() {
        FingerprintManager fingerprintManager = (FingerprintManager) ActionsApplication.getAppContext().getSystemService("fingerprint");
        if (fingerprintManager != null) {
            try {
                return fingerprintManager.hasEnrolledFingerprints();
            } catch (SecurityException e) {
                LOGGER.mo11960e("Application does not have USE_FINGERPRINT permission", e);
            }
        } else {
            LOGGER.mo11959e("Could not retrieve access to fingerprint manager");
            return false;
        }
    }

    private boolean deviceHasLock() {
        KeyguardManager keyguardManager = (KeyguardManager) ActionsApplication.getAppContext().getSystemService("keyguard");
        if (keyguardManager != null) {
            return keyguardManager.isDeviceSecure();
        }
        LOGGER.mo11959e("Unable to access KeyguardManager");
        return false;
    }

    private boolean shouldShow() {
        return hasEnrolledFingerprints() && deviceHasLock() && !OneNavHelper.isOneNavPresent();
    }

    /* access modifiers changed from: 0000 */
    public void triggerNotification() {
        if (!SharedPreferenceManager.getBoolean(Constants.FPS_LOCK_SCREEN_SHOWN, false) && shouldShow()) {
            showNotification();
        }
        SharedPreferenceManager.putBoolean(Constants.FPS_LOCK_SCREEN_SHOWN, true);
    }
}
