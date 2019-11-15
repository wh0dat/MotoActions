package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.AttentiveDisplayInitService;
import com.motorola.actions.attentivedisplay.util.AttentiveDisplayHelper;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.display.ContainerProviderItemAttentiveDisplay;
import com.motorola.actions.utils.ActivityUtils;

public class AttentiveDisplaySettingsUpdater extends SettingsUpdater {
    private static final String AD_ENABLE_SOURCE = "actions_ad_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new AttentiveDisplaySettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return AD_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        if (AttentiveDisplaySettingsFragment.isDependencyResolved()) {
            toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
            return 1;
        }
        ActivityUtils.openFragmentForUserAction(FeatureKey.ATTENTIVE_DISPLAY.ordinal());
        return 0;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.ATTENTIVE_DISPLAY, z);
        setEnabledSource(z, str);
        SharedPreferenceManager.putBoolean(AttentiveDisplayHelper.STAY_ON_KEY, z);
        AttentiveDisplayInitService.onConfigUpdated();
        ActionsSettingsProvider.hideCard(ContainerProviderItemAttentiveDisplay.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemAttentiveDisplay.TABLE_NAME);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ATTENTIVE_DISPLAY);
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.ATTENTIVE_DISPLAY.getEnableDefaultState();
    }
}
