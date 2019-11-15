package com.motorola.actions.settings.updater;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p010qc.QuickDrawHelper;

public class QCSettingsUpdater extends SettingsUpdater {
    private static final String QC_ENABLE_SOURCE = "actions_qc_enable_source";

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SettingsUpdater INSTANCE = new QCSettingsUpdater();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public String getEnableSourceKey() {
        return QC_ENABLE_SOURCE;
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
        QuickDrawHelper.setEnabled(z);
        restartFDNDelay(z);
    }

    public boolean getDefaultSettingsValue() {
        return FeatureKey.QUICK_CAPTURE.getEnableDefaultState();
    }
}
