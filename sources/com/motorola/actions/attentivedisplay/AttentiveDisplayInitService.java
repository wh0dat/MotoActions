package com.motorola.actions.attentivedisplay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.notification.ADBatterySaverNotificationReceiver;
import com.motorola.actions.attentivedisplay.notification.ADDisabledModNotification;
import com.motorola.actions.attentivedisplay.notification.ADModNotificationReceiver;
import com.motorola.actions.attentivedisplay.receiver.BatterySaverReceiver;
import com.motorola.actions.attentivedisplay.util.AttentiveDisplayHelper;
import com.motorola.actions.attentivedisplay.util.ScreenTimeoutControl;
import com.motorola.actions.modaccess.ModAccessManager;
import com.motorola.actions.modaccess.ModAccessManager.ModStateListener;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;
import com.motorola.actions.utils.ServiceUtils;

public class AttentiveDisplayInitService extends Service implements ModStateListener {
    private static final MALogger LOGGER = new MALogger(AttentiveDisplayInitService.class);
    private int mLastStartId = -1;

    public static boolean shouldRun(Context context) {
        return AttentiveDisplayService.isFeatureSupported(context) && AttentiveDisplaySettingsFragment.isStayOnEnabled() && isCameraPermissionGranted(context);
    }

    public static void onConfigUpdated() {
        Context appContext = ActionsApplication.getAppContext();
        if (shouldRun(appContext)) {
            start(appContext);
        } else {
            stop(appContext);
        }
    }

    public static void stop(Context context) {
        AttentiveDisplayService.stop(context);
        PreDimReceiver.getInstance().unregister();
        StowedDetectManager.getInstance().unregister();
        ModAccessManager.getInstance().disconnect(FeatureKey.ATTENTIVE_DISPLAY);
        BatterySaverReceiver.getInstance().unregister();
        dismissADIncompatibilityNotifications();
        ScreenTimeoutControl.resetQuickOff(context);
    }

    private static void dismissADIncompatibilityNotifications() {
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_MOD_INCOMPATIBILITY.ordinal());
        ADModNotificationReceiver.getInstance().unregister();
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_BATTERY_SAVER_INCOMPATIBILITY.ordinal());
        ADBatterySaverNotificationReceiver.getInstance().unregister();
    }

    private static void start(Context context) {
        ServiceUtils.startServiceSafe(new Intent(context, AttentiveDisplayInitService.class));
    }

    public void onCreate() {
        LOGGER.mo11957d("onCreate()");
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        LOGGER.mo11957d("onBind()");
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStartCommand()");
        if (!AttentiveDisplayService.isFeatureSupported(this)) {
            LOGGER.mo11957d("Attentive display not supported");
        } else if (!AttentiveDisplaySettingsFragment.isStayOnEnabled()) {
            LOGGER.mo11957d("Attentive display is disabled");
        } else if (intent == null) {
            LOGGER.mo11957d("Intent received was null");
        } else if (this.mLastStartId != -1) {
            LOGGER.mo11957d("Face detect init already in progress");
        } else {
            startAfterVerified();
        }
        if (this.mLastStartId == -1) {
            stopSelf(i2);
        } else {
            this.mLastStartId = i2;
        }
        return 3;
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy()");
        super.onDestroy();
    }

    private void startAfterVerified() {
        if (shouldRun(this)) {
            PreDimReceiver.getInstance().register();
            StowedDetectManager.getInstance().register();
            if (!SharedPreferenceManager.getBoolean(ADModNotificationReceiver.SHOULD_NOT_SHOW_AGAIN_AD_MOD_PREFERENCE_KEY, false)) {
                ModAccessManager.getInstance().connect(FeatureKey.ATTENTIVE_DISPLAY, this);
            }
            BatterySaverReceiver.getInstance().register();
            if (AttentiveDisplaySettingsFragment.isGoToSleepEnabled()) {
                ScreenTimeoutControl.setQuickOff(this);
            } else {
                ScreenTimeoutControl.resetQuickOff(this);
            }
        }
    }

    public static boolean isCameraPermissionGranted(Context context) {
        if (context.checkSelfPermission("android.permission.CAMERA") == 0) {
            return true;
        }
        LOGGER.mo11957d("AD Permission denied");
        return false;
    }

    public static void updateAttentiveDisplaySettingsFlag(boolean z) {
        SharedPreferenceManager.putBoolean(AttentiveDisplayHelper.STAY_ON_KEY, z);
    }

    public void onModStateChanged(int i, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Mod connected, product family = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (i == 24576 || i == 24832) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Mod is using camera: ");
            sb2.append(z);
            mALogger2.mo11957d(sb2.toString());
            if (!z) {
                NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_MOD_INCOMPATIBILITY.ordinal());
                ADModNotificationReceiver.getInstance().unregister();
            } else if (AttentiveDisplayHelper.isEnabled()) {
                ADModNotificationReceiver.getInstance().register();
                new ADDisabledModNotification().showNotification();
            }
        }
    }
}
