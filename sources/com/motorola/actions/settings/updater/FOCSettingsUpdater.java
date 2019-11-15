package com.motorola.actions.settings.updater;

import android.content.Intent;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.foc.instrumentation.FlashOnChopInstrumentation;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemFOC;
import com.motorola.actions.utils.ServiceUtils;

public class FOCSettingsUpdater extends SettingsUpdater {
    private static final String FOC_ENABLE_SOURCE = "actions_foc_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new FOCSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return FOC_ENABLE_SOURCE;
    }

    public static SettingsUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int updateFromMoto(boolean z) {
        toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        return 1;
    }

    public void toggleStatus(boolean z, boolean z2, String str) {
        setEnabledSource(z, str);
        SharedPreferenceManager.putBoolean(FlashOnChopService.KEY_ENABLED, z);
        ActionsSettingsProvider.hideCard(ContainerProviderItemFOC.PRIORITY_KEY);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemFOC.TABLE_NAME);
        if (z2) {
            FlashOnChopInstrumentation.recordDailyToggleEvent(z);
        }
        restartFDNDelay(z);
        Intent createIntent = FlashOnChopService.createIntent(ActionsApplication.getAppContext());
        if (z) {
            ServiceUtils.startServiceSafe(createIntent);
        }
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.FLASH_ON_CHOP.getEnableDefaultState();
    }
}
