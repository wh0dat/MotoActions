package com.motorola.actions.p013ui.settings;

import android.os.Bundle;
import android.view.View;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.p013ui.tutorial.foc.FlashOnChopTutorialActivity;
import com.motorola.actions.settings.updater.FOCSettingsUpdater;
import com.motorola.actions.utils.ActivityUtils;

/* renamed from: com.motorola.actions.ui.settings.FlashOnChopSettingsFragment */
public class FlashOnChopSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.foc_checkbox_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.foc_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.chopchop;
    }

    public Class tutorialClass() {
        return FlashOnChopTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.FLASH_ON_CHOP;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ActivityUtils.stretchVideo(this.mTextureView);
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (SharedPreferenceManager.getBoolean(FlashOnChopService.KEY_ENABLED, FeatureKey.FLASH_ON_CHOP.getEnableDefaultState())) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        FOCSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
