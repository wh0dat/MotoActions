package com.motorola.actions.p013ui.settings;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.ftm.FlipToMuteConstants;
import com.motorola.actions.ftm.FlipToMuteHelper;
import com.motorola.actions.settings.updater.FTMSettingsUpdater;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.FlipToMuteSettingsFragment */
public class FlipToMuteSettingsFragment extends SettingsDetailFragment {
    private static final String ACTION_ZEN_MODE_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";
    private static final MALogger LOGGER = new MALogger(FlipToMuteSettingsFragment.class);
    private NotificationManager mNotificationManager;
    private TextView mSettingsButton;
    private ViewGroup mVibrationGroup;
    private Switch mVibrationSwitch;

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.ftm_info;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_settings_ftm;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.ftm_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.flip_to_dnd;
    }

    public Class tutorialClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.FLIP_TO_DND;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mNotificationManager = (NotificationManager) getActivity().getSystemService("notification");
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mSettingsButton = (TextView) view.findViewById(C0504R.C0506id.priority_only_button);
        setupSettingsButton(true);
        loadVibrationSetting(view);
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (!SharedPreferenceManager.getBoolean(FlipToMuteConstants.KEY_ENABLED, FeatureKey.FLIP_TO_DND.getEnableDefaultState()) || !this.mNotificationManager.isNotificationPolicyAccessGranted()) {
            return SettingStatus.DISABLED;
        }
        return SettingStatus.ENABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        handleStatusChange(z);
        setupSettingsButton(z);
    }

    private void handleStatusChange(boolean z) {
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        FTMSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        updateOptions(z);
    }

    private void updateOptions(boolean z) {
        if (this.mVibrationGroup == null) {
            return;
        }
        if (z) {
            this.mVibrationGroup.setAlpha(1.0f);
            this.mVibrationSwitch.setClickable(true);
            return;
        }
        this.mVibrationGroup.setAlpha(0.5f);
        this.mVibrationSwitch.setClickable(false);
    }

    private void loadVibrationSetting(@NonNull View view) {
        View view2 = getView();
        LOGGER.mo11957d("Loading default vibration settings...");
        if (view2 != null) {
            this.mVibrationGroup = (ViewGroup) view2.findViewById(C0504R.C0506id.vibration_setting);
            this.mVibrationSwitch = (Switch) view.findViewById(C0504R.C0506id.toggle_element);
            this.mVibrationSwitch.setOnCheckedChangeListener(new FlipToMuteSettingsFragment$$Lambda$0(this));
            this.mVibrationSwitch.setChecked(FlipToMuteHelper.isVibrationEnabled());
            setVibrationTitle();
            setVibrationDescription();
            updateOptions(getEnabledStatus() == SettingStatus.ENABLED);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$loadVibrationSetting$0$FlipToMuteSettingsFragment(CompoundButton compoundButton, boolean z) {
        onVibrationChange(z);
    }

    private void setVibrationTitle() {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(C0504R.C0506id.toggle_text)).setText(C0504R.string.ftm_vibration_setting_title);
        }
    }

    private void setVibrationDescription() {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(C0504R.C0506id.toggle_description)).setText(C0504R.string.ftm_vibration_setting_on);
        }
    }

    private void onVibrationChange(boolean z) {
        SharedPreferenceManager.putBoolean(FlipToMuteConstants.KEY_FTM_VIBRATION, z);
    }

    private void setupSettingsButton(boolean z) {
        if (this.mSettingsButton != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("setupSettingsButton: ");
            sb.append(z);
            mALogger.mo11957d(sb.toString());
            if (!z || getEnabledStatus() != SettingStatus.ENABLED) {
                this.mSettingsButton.setEnabled(false);
                this.mSettingsButton.setAlpha(0.5f);
                this.mSettingsButton.setOnClickListener(null);
                return;
            }
            this.mSettingsButton.setEnabled(true);
            this.mSettingsButton.setAlpha(1.0f);
            this.mSettingsButton.setOnClickListener(new FlipToMuteSettingsFragment$$Lambda$1(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupSettingsButton$1$FlipToMuteSettingsFragment(View view) {
        startActivity(new Intent(ACTION_ZEN_MODE_SETTINGS));
    }
}
