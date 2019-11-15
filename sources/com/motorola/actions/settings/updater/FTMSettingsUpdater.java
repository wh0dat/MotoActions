package com.motorola.actions.settings.updater;

import android.content.Context;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.ftm.FlipToMuteConstants;
import com.motorola.actions.ftm.FlipToMuteService;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFTM;
import com.motorola.actions.utils.ServiceUtils;

public class FTMSettingsUpdater extends SettingsUpdater {
    private static final String FTM_ENABLE_SOURCE = "actions_ftm_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final FTMSettingsUpdater INSTANCE = new FTMSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return FTM_ENABLE_SOURCE;
    }

    public static FTMSettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        ignoreDiscoveryStatus(FeatureKey.FLIP_TO_DND, z);
        setEnabledSource(z, str);
        SharedPreferenceManager.putBoolean(FlipToMuteConstants.KEY_ENABLED, z);
        if (z) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.FLIP_TO_DND);
        }
        ActionsSettingsProvider.hideCard(ContainerProviderItemFTM.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemFTM.TABLE_NAME);
        Context appContext = ActionsApplication.getAppContext();
        if (z) {
            ServiceUtils.startServiceSafe(FlipToMuteService.createIntent(appContext));
        } else {
            appContext.stopService(FlipToMuteService.createIntent(appContext));
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.FLIP_TO_DND.getEnableDefaultState();
    }
}
