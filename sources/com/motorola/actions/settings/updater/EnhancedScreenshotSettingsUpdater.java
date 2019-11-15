package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotModel;
import com.motorola.actions.enhancedscreenshot.FeatureManager;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemEnhancedScreenshot;

public class EnhancedScreenshotSettingsUpdater extends SettingsUpdater {
    private static final String ENABLE_SOURCE = "actions_enhanced_screenshot_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new EnhancedScreenshotSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.ENHANCED_SCREENSHOT, z);
        setEnabledSource(z, str);
        EnhancedScreenshotModel.setServiceEnabled(z);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ENHANCED_SCREENSHOT);
        }
        ActionsSettingsProvider.hideCard(ContainerProviderItemEnhancedScreenshot.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemEnhancedScreenshot.TABLE_NAME);
        if (!z) {
            FeatureManager.stop();
        } else {
            FeatureManager.start();
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.ENHANCED_SCREENSHOT.getEnableDefaultState();
    }
}
