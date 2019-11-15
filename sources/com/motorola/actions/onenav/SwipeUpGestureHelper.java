package com.motorola.actions.onenav;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.Optional;

public class SwipeUpGestureHelper {
    private static final String ACTION_QUICKSTEP = "android.intent.action.QUICKSTEP_SERVICE";
    private static final MALogger LOGGER = new MALogger(SwipeUpGestureHelper.class);
    private static final String PLATFORM_CONFIG_RECENTS_COMPONENT_NAME = "config_recentsComponentName";
    private static final String PLATFORM_SWIPE_UP_GESTURE_AVAILABLE = "config_swipe_up_gesture_setting_available";
    private static final int PLATFORM_SWIPE_UP_GESTURE_AVAILABLE_DISABLE = 0;
    private static final int PLATFORM_SWIPE_UP_GESTURE_AVAILABLE_ENABLE = 1;
    public static final String SWIPE_UP_TO_SWITCH_APPS_ENABLED = "swipe_up_to_switch_apps_enabled";

    public static boolean isGestureAvailable() {
        Context appContext = ActionsApplication.getAppContext();
        Optional boolResource = AndroidResourceAccess.getBoolResource(PLATFORM_SWIPE_UP_GESTURE_AVAILABLE);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isGestureAvailable.isPresent(): ");
        sb.append(boolResource.isPresent());
        mALogger.mo11957d(sb.toString());
        if (boolResource.isPresent() && !((Boolean) boolResource.get()).booleanValue()) {
            Optional stringResource = AndroidResourceAccess.getStringResource(PLATFORM_CONFIG_RECENTS_COMPONENT_NAME);
            if (stringResource.isPresent()) {
                boolResource = appContext.getPackageManager().resolveService(new Intent(ACTION_QUICKSTEP).setPackage(ComponentName.unflattenFromString((String) stringResource.get()).getPackageName()), 1048576) == null ? Optional.of(Boolean.valueOf(false)) : Optional.of(Boolean.valueOf(true));
            }
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isGestureAvailable: ");
        sb2.append(boolResource);
        mALogger2.mo11957d(sb2.toString());
        return ((Boolean) boolResource.orElse(Boolean.valueOf(false))).booleanValue();
    }

    public static void setEnabled(boolean z) {
        if (isGestureAvailable()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("setEnabled: ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            SettingsWrapper.putSecureInt(SWIPE_UP_TO_SWITCH_APPS_ENABLED, z ? 1 : 0);
        }
    }

    public static boolean isEnabled() {
        boolean z = false;
        if (isGestureAvailable() && ((Integer) SettingsWrapper.getSecureInt(SWIPE_UP_TO_SWITCH_APPS_ENABLED).orElse(Integer.valueOf(0))).intValue() == 1) {
            z = true;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isEnabled: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
