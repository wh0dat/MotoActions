package com.motorola.actions.settings.updater;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.approach.p006us.USService;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemApproach;

public class ApproachSettingsUpdater extends SettingsUpdater {
    private static final String APPROACH_ENABLE_SOURCE = "actions_approach_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new ApproachSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return APPROACH_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.APPROACH, z);
        setEnabledSource(z, str);
        SharedPreferenceManager.putBoolean("ir_aod_approach_enabled", z);
        USService.toggleService(ActionsApplication.getAppContext(), z);
        ActionsSettingsProvider.hideCard(ContainerProviderItemApproach.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemApproach.TABLE_NAME);
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.APPROACH.getEnableDefaultState();
    }
}
