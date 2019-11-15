package com.motorola.actions.p013ui.settings;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.fpsslide.FPSSlideHelper;
import com.motorola.actions.settings.updater.FPSSlideHomeSettingsUpdater;

/* renamed from: com.motorola.actions.ui.settings.FPSSlideHomeSettingsFragment */
public class FPSSlideHomeSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.fps_slide_home_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.fps_slide_home_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.fps_slide_home_settings;
    }

    public Class tutorialClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.FPS_SLIDE_HOME;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (FPSSlideHelper.isScrollOnHomeEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        FPSSlideHomeSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
