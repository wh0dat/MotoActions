package com.motorola.actions.p013ui.settings;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p010qc.QuickDrawHelper;
import com.motorola.actions.p013ui.tutorial.p015qc.QuickDrawTutorialActivity;
import com.motorola.actions.settings.updater.QCSettingsUpdater;
import com.motorola.actions.utils.DisplayUtils;

/* renamed from: com.motorola.actions.ui.settings.QuickDrawSettingsFragment */
public class QuickDrawSettingsFragment extends SettingsDetailFragment {
    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.camera_qd_enabled_checkbox_summary;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.camera_qd_enabled;
    }

    public int getVideoId() {
        return DisplayUtils.getScreenWidth(getActivity()) <= 540 ? C0504R.raw.twist_lowres : C0504R.raw.twist;
    }

    public Class tutorialClass() {
        return QuickDrawTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.QUICK_CAPTURE;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        SettingStatus settingStatus = SettingStatus.DISABLED;
        return (!QuickDrawHelper.isQuickCaptureSupported(this.mContext) || !QuickDrawHelper.isEnabled()) ? settingStatus : SettingStatus.ENABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        if (QuickDrawHelper.isQuickCaptureSupported(this.mContext)) {
            updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
            QCSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
            return;
        }
        updateStatus(SettingStatus.UNAVAILABLE);
    }
}
