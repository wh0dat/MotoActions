package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.fpsslide.FPSSlideHelper;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFPSSlideHome;

public class FPSSlideHomeSettingsUpdater extends SettingsUpdater {
    private static final String ENABLE_SOURCE = "actions_fps_slide_home_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new FPSSlideHomeSettingsUpdater();

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
        ignoreDiscoveryStatus(FeatureKey.FPS_SLIDE_HOME, z);
        setEnabledSource(z, str);
        FPSSlideHelper.setScrollOnHome(z);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.FPS_SLIDE_HOME);
        }
        ActionsSettingsProvider.hideCard(ContainerProviderItemFPSSlideHome.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemFPSSlideHome.TABLE_NAME);
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.FPS_SLIDE_HOME.getEnableDefaultState();
    }
}
