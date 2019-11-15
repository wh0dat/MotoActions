package com.motorola.actions.settings.updater;

import android.content.Context;
import android.content.Intent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLTS;
import com.motorola.actions.utils.ServiceUtils;

public class LTSSettingsUpdater extends SettingsUpdater {
    private static final String LTS_ENABLE_SOURCE = "actions_lts_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final LTSSettingsUpdater INSTANCE = new LTSSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return LTS_ENABLE_SOURCE;
    }

    public static LTSSettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.PICKUP_TO_STOP_RINGING, z);
        setEnabledSource(z, str);
        SharedPreferenceManager.putBoolean(LiftToSilenceService.KEY_ENABLED, z);
        ActionsSettingsProvider.hideCard(ContainerProviderItemLTS.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemLTS.TABLE_NAME);
        Context appContext = ActionsApplication.getAppContext();
        Intent createIntent = LiftToSilenceService.createIntent(appContext);
        if (!z) {
            appContext.stopService(createIntent);
            return;
        }
        ServiceUtils.startServiceSafe(createIntent);
        DiscoveryManager.getInstance().cancelFDN(FeatureKey.PICKUP_TO_STOP_RINGING);
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.PICKUP_TO_STOP_RINGING.getEnableDefaultState();
    }

    public void updateFromTutorial() {
        toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
    }
}
