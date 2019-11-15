package com.motorola.actions.nightdisplay.common;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.NightDisplayService;
import com.motorola.actions.utils.MALogger;

public class Persistence {
    private static final String KEY_ACTIVITY_RECOGNITION_ENABLED = "key_activity_recognition_enabled";
    public static final String KEY_NIGHT_DISPLAY_INITIATION_CUSTOM_HOUR = "night_display_initation_custom_hour";
    public static final String KEY_NIGHT_DISPLAY_INITIATION_TYPE = "night_display_initation_type";
    public static final String KEY_NIGHT_DISPLAY_MODE = "night_display_mode";
    public static final String KEY_NIGHT_DISPLAY_TERMINATION_CUSTOM_HOUR = "night_display_termination_custom_hour";
    public static final String KEY_NIGHT_DISPLAY_TERMINATION_TYPE = "night_display_termination_type";
    private static final String KEY_SLEEP_PATTERN_DAYS_RUNNING = "key_sleep_pattern_days_running";
    private static final String KEY_SLEEP_PATTERN_IS_READY = "key_sleep_pattern_is_ready";
    private static final MALogger LOGGER = new MALogger(Persistence.class);

    public static void saveFeatureEnable(boolean z) {
        SharedPreferenceManager.putBoolean(NightDisplayService.KEY_ENABLED, z);
    }

    public static boolean isFeatureEnabled() {
        return SharedPreferenceManager.getBoolean(NightDisplayService.KEY_ENABLED, FeatureKey.NIGHT_DISPLAY.getEnableDefaultState());
    }

    public static boolean isSleepPatternReady() {
        boolean z = SharedPreferenceManager.getBoolean(KEY_SLEEP_PATTERN_IS_READY, false);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isSleepPatternReady: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setIsSleepPatternReady(boolean z) {
        SharedPreferenceManager.putBoolean(KEY_SLEEP_PATTERN_IS_READY, z);
    }

    public static int getDaysRunningSleepPattern() {
        return SharedPreferenceManager.getInt(KEY_SLEEP_PATTERN_DAYS_RUNNING, 0);
    }

    public static void setDaysRunningSleepPattern(int i) {
        SharedPreferenceManager.putInt(KEY_SLEEP_PATTERN_DAYS_RUNNING, i);
    }

    public static void saveMode(int i) {
        SharedPreferenceManager.putInt(KEY_NIGHT_DISPLAY_MODE, i);
    }

    public static int getMode(int i) {
        if (!SharedPreferenceManager.contains(KEY_NIGHT_DISPLAY_MODE) && isOldModeCustomTime()) {
            LOGGER.mo11957d("User is using custom time, carry the option to manual mode.");
            SharedPreferenceManager.putInt(KEY_NIGHT_DISPLAY_MODE, 1);
        }
        return SharedPreferenceManager.getInt(KEY_NIGHT_DISPLAY_MODE, i);
    }

    public static void saveInitialTimeInMinutes(int i) {
        SharedPreferenceManager.putInt(KEY_NIGHT_DISPLAY_INITIATION_CUSTOM_HOUR, i);
    }

    public static int getInitialTimeInMinutes() {
        return SharedPreferenceManager.getInt(KEY_NIGHT_DISPLAY_INITIATION_CUSTOM_HOUR, (int) NightDisplayConstants.AUTOMATIC_TIME_MINUTE_BEGIN);
    }

    public static void saveTerminationTimeInMinutes(int i) {
        SharedPreferenceManager.putInt(KEY_NIGHT_DISPLAY_TERMINATION_CUSTOM_HOUR, i);
    }

    public static int getTerminationTimeInMinutes() {
        return SharedPreferenceManager.getInt(KEY_NIGHT_DISPLAY_TERMINATION_CUSTOM_HOUR, (int) NightDisplayConstants.AUTOMATIC_TIME_MINUTE_END);
    }

    private static boolean isOldModeCustomTime() {
        if (SharedPreferenceManager.getInt(KEY_NIGHT_DISPLAY_INITIATION_TYPE, 4) == 1 && SharedPreferenceManager.getInt(KEY_NIGHT_DISPLAY_TERMINATION_TYPE, 4) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isActivityRecognitionEnabled() {
        return SharedPreferenceManager.getBoolean(KEY_ACTIVITY_RECOGNITION_ENABLED, false);
    }

    public static void setActivityRecognitionEnabled(boolean z) {
        SharedPreferenceManager.putBoolean(KEY_ACTIVITY_RECOGNITION_ENABLED, z);
    }
}
