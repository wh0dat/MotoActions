package com.motorola.actions.attentivedisplay.util;

import android.content.Context;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;
import com.motorola.actions.utils.MALogger;

public class ScreenTimeoutControl {
    private static final int DEFAULT_QUICK_OFF_PERCENT = 1;
    private static final MALogger LOGGER = new MALogger(ScreenTimeoutControl.class);
    public static final int NO_SCREEN_OFF_TIMEOUT = -1;

    public static void setQuickOff(Context context) {
        new PowerManagerPrivateProxy(context).setQuickOff(0.8f);
    }

    public static void resetQuickOff(Context context) {
        LOGGER.mo11957d("Re-setting quick off percent.");
        new PowerManagerPrivateProxy(context).setQuickOff(1.0f);
    }

    public static int getScreenTimeout(Context context) {
        try {
            return System.getInt(context.getContentResolver(), "screen_off_timeout");
        } catch (SettingNotFoundException e) {
            LOGGER.mo11964w("Error getting screen timeout", e);
            return -1;
        }
    }

    public static int getReducedScreenTimeout(Context context) {
        int screenTimeout = getScreenTimeout(context);
        return (screenTimeout == -1 || !AttentiveDisplaySettingsFragment.isGoToSleepEnabled()) ? screenTimeout : (int) (((float) screenTimeout) * 0.8f);
    }
}
