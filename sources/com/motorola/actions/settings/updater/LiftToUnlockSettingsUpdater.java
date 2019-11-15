package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.ltu.LiftToUnlockHelper;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLiftToUnlock;
import com.motorola.actions.utils.ActivityUtils;

public class LiftToUnlockSettingsUpdater extends SettingsUpdater {
    private static final String ENABLE_SOURCE = "actions_ltu_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new LiftToUnlockSettingsUpdater();

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
        if (!z || LiftToUnlockHelper.isEnrolled()) {
            toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
            return 1;
        }
        ActivityUtils.openFragmentForUserAction(FeatureKey.LIFT_TO_UNLOCK.ordinal());
        return 0;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.LIFT_TO_UNLOCK, z);
        setEnabledSource(z, str);
        LiftToUnlockHelper.setServiceEnabled(z);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.LIFT_TO_UNLOCK);
        }
        ActionsSettingsProvider.hideCard(ContainerProviderItemLiftToUnlock.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemLiftToUnlock.TABLE_NAME);
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.LIFT_TO_UNLOCK.getEnableDefaultState();
    }
}
