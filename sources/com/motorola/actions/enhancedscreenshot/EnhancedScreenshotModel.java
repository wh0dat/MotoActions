package com.motorola.actions.enhancedscreenshot;

import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemEnhancedScreenshot;
import com.motorola.actions.utils.MALogger;

public class EnhancedScreenshotModel {
    private static final MALogger LOGGER = new MALogger(EnhancedScreenshotModel.class);

    public static void setServiceEnabled(boolean z) {
        MotorolaSettings.setEnhancedScreenshot(z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemEnhancedScreenshot.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isServiceEnabled() {
        boolean isEnhancedScreenshot = MotorolaSettings.isEnhancedScreenshot();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled = ");
        sb.append(isEnhancedScreenshot);
        mALogger.mo11957d(sb.toString());
        return isEnhancedScreenshot;
    }
}
