package com.motorola.actions.quickscreenshot;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemQuickScreenshot;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SecurityUtils;

public class QuickScreenshotModel {
    private static final String KEY_ENABLED = "actions_quickscreenshot_enabled";
    private static final MALogger LOGGER = new MALogger(QuickScreenshotModel.class);

    public static void setServiceEnabled(boolean z) {
        MotorolaSettings.setQuickScreenshotMode(z ? 1 : 0);
        SharedPreferenceManager.putBoolean(KEY_ENABLED, z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemQuickScreenshot.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isServiceEnabled() {
        boolean z = SharedPreferenceManager.getBoolean(KEY_ENABLED, FeatureKey.QUICK_SCREENSHOT.getEnableDefaultState());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static void setInitialValue() {
        if (!SecurityUtils.isUserUnlocked()) {
            LOGGER.mo11957d("setInitialValue(): Disable Quick Screenshot");
            MotorolaSettings.setQuickScreenshotMode(0);
            return;
        }
        setServiceEnabled(isServiceEnabled());
    }
}
