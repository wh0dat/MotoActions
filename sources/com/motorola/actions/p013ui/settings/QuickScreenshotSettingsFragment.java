package com.motorola.actions.p013ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p013ui.tutorial.quickScreenshot.QuickScreenshotTutorialActivity;
import com.motorola.actions.quickscreenshot.QuickScreenshotModel;
import com.motorola.actions.settings.updater.QuickScreenshotSettingsUpdater;
import com.motorola.actions.utils.Constants;

/* renamed from: com.motorola.actions.ui.settings.QuickScreenshotSettingsFragment */
public class QuickScreenshotSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.quick_screenshot_info;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.quick_screenshot_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.screenshot_settings;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        SharedPreferenceManager.putBoolean(Constants.QUICK_SCREENSHOT_SCREEN_ALREADY_SHOWN, true);
    }

    public Class tutorialClass() {
        return QuickScreenshotTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.QUICK_SCREENSHOT;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (QuickScreenshotModel.isServiceEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        QuickScreenshotSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
