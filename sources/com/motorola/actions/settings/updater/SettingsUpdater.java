package com.motorola.actions.settings.updater;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.highlight.FeatureHighlightKey;
import com.motorola.actions.utils.MALogger;

public abstract class SettingsUpdater {
    private static final MALogger LOGGER = new MALogger(SettingsUpdater.class);
    static final int UPDATE_FAILED = 0;
    static final int UPDATE_SUCCESS = 1;

    public abstract boolean getDefaultSettingsValue();

    /* access modifiers changed from: protected */
    public abstract String getEnableSourceKey();

    public abstract void toggleStatus(boolean z, boolean z2, String str);

    public abstract int updateFromMoto(boolean z);

    public void resetToDefault() {
        toggleStatus(getDefaultSettingsValue(), false, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_DEFAULT);
    }

    public void setEnabledSource(boolean z, String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("setEnabledSource: getEnableSourceKey = ");
        sb.append(getEnableSourceKey());
        sb.append(" - status = ");
        sb.append(z);
        sb.append(" - sourceEnabled = ");
        sb.append(str);
        mALogger.mo11957d(sb.toString());
        if (z) {
            SharedPreferenceManager.putString(getEnableSourceKey(), str);
        }
    }

    @Nullable
    public String getEnabledSource(@NonNull boolean z) {
        return SharedPreferenceManager.getString(getEnableSourceKey(), z ? CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_DEFAULT : null);
    }

    /* access modifiers changed from: 0000 */
    public void ignoreDiscoveryStatus(FeatureKey featureKey, boolean z) {
        if (z) {
            DiscoveryManager.getInstance().setDiscoveryStatus(featureKey, 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateHighlight(FeatureHighlightKey featureHighlightKey, boolean z) {
        if (z) {
            DiscoveryManager.getInstance().cancelHighlight(featureHighlightKey);
        }
    }

    /* access modifiers changed from: protected */
    public final void restartFDNDelay(boolean z) {
        if (!z) {
            DiscoveryManager.getInstance().restartFDNDelay();
        }
    }
}
