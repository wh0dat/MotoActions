package com.motorola.actions.nightdisplay;

import com.motorola.actions.nightdisplay.p008pd.TwilightAccess;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.Optional;

class NDSecureSettingsUpdater {
    private static final MALogger LOGGER = new MALogger(NDSecureSettingsUpdater.class);
    public static final String NIGHT_DISPLAY_AUTO_MODE = "night_display_auto_mode";
    public static final String QS_TILES = "sysui_qs_tiles";

    NDSecureSettingsUpdater() {
    }

    public static void fixNightDisplayActivation() {
        Optional secureInt = SettingsWrapper.getSecureInt(TwilightAccess.SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED);
        int nightDisplayIntensity = TwilightAccess.getNightDisplayIntensity();
        int minColorTemperature = TwilightAccess.getMinColorTemperature();
        if (!((Integer) secureInt.orElse(Integer.valueOf(0))).equals(Integer.valueOf(1))) {
            return;
        }
        if (nightDisplayIntensity < minColorTemperature || nightDisplayIntensity > TwilightAccess.getMaxColorTemperature()) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Night light active, setting intensity ");
            sb.append(minColorTemperature);
            mALogger.mo11957d(sb.toString());
            TwilightAccess.enable(minColorTemperature);
        }
    }
}
