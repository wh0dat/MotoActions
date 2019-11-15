package com.motorola.actions.p013ui.settings;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.mediacontrol.MediaControlModel;
import com.motorola.actions.p013ui.tutorial.mediacontrol.MediaControlTutorialActivity;
import com.motorola.actions.settings.updater.MediaControlSettingsUpdater;

/* renamed from: com.motorola.actions.ui.settings.MediaControlSettingsFragment */
public class MediaControlSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.media_control_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.media_control_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.media_control_settings;
    }

    public Class tutorialClass() {
        return MediaControlTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.MEDIA_CONTROL;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (MediaControlModel.isServiceEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        MediaControlSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }
}
