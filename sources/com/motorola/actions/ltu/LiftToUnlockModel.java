package com.motorola.actions.ltu;

import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLiftToUnlock;
import com.motorola.actions.utils.MALogger;

public class LiftToUnlockModel {
    private static final MALogger LOGGER = new MALogger(LiftToUnlockModel.class);

    public static void setServiceEnabled(boolean z) {
        MotorolaSettings.setLiftToUnlockEnabled(z);
        ActionsSettingsProvider.notifyChange(ContainerProviderItemLiftToUnlock.TABLE_NAME);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setServiceEnabled = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
    }

    public static boolean isServiceEnabled() {
        boolean isLiftToUnlockEnabled = MotorolaSettings.isLiftToUnlockEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isServiceEnabled = ");
        sb.append(isLiftToUnlockEnabled);
        mALogger.mo11957d(sb.toString());
        return isLiftToUnlockEnabled;
    }
}
