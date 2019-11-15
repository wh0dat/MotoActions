package com.motorola.actions.p013ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.enhancedscreenshot.EnhancedScreenshotModel;
import com.motorola.actions.p013ui.tutorial.enhancedscreenshot.EnhancedScreenshotTutorialActivity;
import com.motorola.actions.settings.updater.EnhancedScreenshotSettingsUpdater;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.EnhancedScreenshotSettingsFragment */
public class EnhancedScreenshotSettingsFragment extends SettingsDetailFragment {
    private static final MALogger LOGGER = new MALogger(EnhancedScreenshotSettingsFragment.class);

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.enhanced_screenshot_info;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.enhanced_screenshot_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.screenshot_settings;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        ImageView imageView = (ImageView) onCreateView.findViewById(C0504R.C0506id.image);
        imageView.setImageResource(C0504R.C0505drawable.screenshot_editor_tutorial_start);
        imageView.setVisibility(0);
        this.mTextureView.setVisibility(8);
        return onCreateView;
    }

    public Class tutorialClass() {
        return EnhancedScreenshotTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.ENHANCED_SCREENSHOT;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (EnhancedScreenshotModel.isServiceEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        EnhancedScreenshotSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
