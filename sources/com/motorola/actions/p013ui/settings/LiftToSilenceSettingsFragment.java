package com.motorola.actions.p013ui.settings;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.lts.LiftToSilenceService;
import com.motorola.actions.p013ui.tutorial.lts.LiftToSilenceTutorialActivity;
import com.motorola.actions.settings.updater.LTSSettingsUpdater;

/* renamed from: com.motorola.actions.ui.settings.LiftToSilenceSettingsFragment */
public class LiftToSilenceSettingsFragment extends SettingsDetailFragment {
    private static final int LIFT_TO_SILENCE_VIDEO = 2131558406;
    private static final boolean LTS_DEFAULT_SETTING_VALUE = false;

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.lts_enabled_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.lts_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.lift_to_silence;
    }

    public Class tutorialClass() {
        return LiftToSilenceTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.PICKUP_TO_STOP_RINGING;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (SharedPreferenceManager.getBoolean(LiftToSilenceService.KEY_ENABLED, false)) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        LTSSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
