package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.microScreen.MicroScreenReceiver;
import com.motorola.actions.microScreen.MicroScreenService;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMicroscreen;

public class MicroScreenSettingsUpdater extends SettingsUpdater {
    private static final String MS_ENABLE_SOURCE = "actions_ms_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new MicroScreenSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return MS_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        if (z) {
            MicroScreenReceiver.setCheckingPermissionFromMoto(true);
            MicroScreenService.sendCheckOverlayPermission();
        }
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.MICROSCREEN, z);
        setEnabledSource(z, str);
        ActionsSettingsProvider.hideCard(ContainerProviderItemMicroscreen.PRIORITY_KEY);
        MicroScreenModel.saveMicroScreenEnabled(z);
        if (!z) {
            MicroScreenService.stopMicroScreenService();
        } else {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.MICROSCREEN);
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.MICROSCREEN.getEnableDefaultState();
    }
}
