package com.motorola.actions.p013ui.settings;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.settings.updater.ApproachSettingsUpdater;

/* renamed from: com.motorola.actions.ui.settings.ApproachSettingsFragment */
public class ApproachSettingsFragment extends SettingsDetailFragment {
    public static final String KEY_ENABLED = "ir_aod_approach_enabled";

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.ir_aod_approach_enabled_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.ir_aod_approach_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.approach;
    }

    public Class tutorialClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.APPROACH;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (SharedPreferenceManager.getBoolean("ir_aod_approach_enabled", FeatureKey.APPROACH.getEnableDefaultState())) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        ApproachSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
