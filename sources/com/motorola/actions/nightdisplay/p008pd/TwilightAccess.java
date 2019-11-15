package com.motorola.actions.nightdisplay.p008pd;

import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.Optional;

/* renamed from: com.motorola.actions.nightdisplay.pd.TwilightAccess */
public class TwilightAccess {
    private static final MALogger LOGGER = new MALogger(TwilightAccess.class);
    private static final String NIGHT_DISPLAY_COLOR_TEMPERATURE = "night_display_color_temperature";
    private static final String NIGHT_DISPLAY_COLOR_TEMPERATURE_MAX = "config_nightDisplayColorTemperatureMax";
    private static final String NIGHT_DISPLAY_COLOR_TEMPERATURE_MIN = "config_nightDisplayColorTemperatureMin";
    public static final String SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED = "night_display_activated";
    public static final int SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED_FALSE = 0;
    public static final int SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED_TRUE = 1;

    public static void enable(int i) {
        enableSettingsSecure(i);
    }

    private static void enableSettingsSecure(int i) {
        int i2 = i > 0 ? 1 : 0;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Value received = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Value to set on Settings Secure = ");
        sb2.append(i2);
        mALogger2.mo11957d(sb2.toString());
        boolean putSecureInt = SettingsWrapper.putSecureInt(SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED, i2);
        if (i != 0) {
            setNightDisplayIntensity(i);
        }
        if (!putSecureInt) {
            LOGGER.mo11959e("Error to set the NightDisplay on SettingsSecure");
        }
    }

    public static void setNightDisplayIntensity(int i) {
        SettingsWrapper.putSecureInt(NIGHT_DISPLAY_COLOR_TEMPERATURE, i);
    }

    public static int getNightDisplayIntensity() {
        return ((Integer) SettingsWrapper.getSecureInt(NIGHT_DISPLAY_COLOR_TEMPERATURE).orElse(Integer.valueOf(loadColorTemperature(NIGHT_DISPLAY_COLOR_TEMPERATURE_MIN)))).intValue();
    }

    private static int loadColorTemperature(String str) {
        Optional intResource = AndroidResourceAccess.getIntResource(str);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append(intResource);
        mALogger.mo11957d(sb.toString());
        return ((Integer) intResource.orElse(Integer.valueOf(0))).intValue();
    }

    public static int getMaxColorTemperature() {
        return loadColorTemperature(NIGHT_DISPLAY_COLOR_TEMPERATURE_MAX);
    }

    public static int getMinColorTemperature() {
        return loadColorTemperature(NIGHT_DISPLAY_COLOR_TEMPERATURE_MIN);
    }
}
