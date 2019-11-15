package com.motorola.actions.microScreen.model;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.microScreen.MicroScreenConstants;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMicroscreen;
import com.motorola.actions.utils.MALogger;

public class MicroScreenModel {
    private static final MALogger LOGGER = new MALogger(MicroScreenModel.class);

    public static void saveMicroScreenTutorialIsActive(boolean z) {
        SharedPreferenceManager.putBoolean(MicroScreenConstants.MICROSCREEN_TUTORIAL_ACTIVE, z);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("saveMicroScreenTutorialIsActive = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isMicroScreenTutorialIsActive() {
        boolean z = SharedPreferenceManager.getBoolean(MicroScreenConstants.MICROSCREEN_TUTORIAL_ACTIVE, false);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMicroScreenTutorialIsActive = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void saveMicroScreenEnabled(boolean z) {
        if (!z) {
            MicroScreenService.sendExitByInteractionIntent();
        }
        MotorolaSettings.setGlobalTouchListenerValue(z ? 1 : 0);
        SharedPreferenceManager.putBoolean(MicroScreenConstants.KEY_ENABLED, z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemMicroscreen.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("saveMicroScreenEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isMicroScreenEnabled() {
        boolean z = SharedPreferenceManager.getBoolean(MicroScreenConstants.KEY_ENABLED, FeatureKey.MICROSCREEN.getEnableDefaultState());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMicroScreenEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean isMicroScreenModeOn() {
        boolean z = true;
        if (MotorolaSettings.getSingleHandOnValue() != 1) {
            z = false;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isMicroScreenModeOn = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void saveMicroScreenHintCounter(int i) {
        SharedPreferenceManager.putInt(MicroScreenConstants.KEY_SHOW_EXIT_HINT_COUNT, i);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("saveMicroScreenHintCounter = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
    }

    public static int getMicroScreenHintCount() {
        int i = SharedPreferenceManager.getInt(MicroScreenConstants.KEY_SHOW_EXIT_HINT_COUNT, 0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getMicroScreenHintCount = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i;
    }

    public static void saveDoNotShowGestureChangedNotification() {
        LOGGER.mo11957d("saveDoNotShowGestureChangedNotification");
        SharedPreferenceManager.putBoolean(MicroScreenConstants.KEY_MICROSCREEN_DO_NOT_SHOW_GESTURE_CHANGED_NOTIF, true);
    }

    public static boolean getDoNotShowGestureChangedNotification() {
        boolean contains = SharedPreferenceManager.contains(MicroScreenConstants.KEY_MICROSCREEN_DO_NOT_SHOW_GESTURE_CHANGED_NOTIF);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getDoNotShowGestureChangedNotification = ");
        sb.append(contains);
        mALogger.mo11957d(sb.toString());
        return contains;
    }
}
