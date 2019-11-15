package com.motorola.actions.p013ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.fpsslide.FPSSlideHelper;
import com.motorola.actions.settings.updater.FPSSlideAppSettingsUpdater;

/* renamed from: com.motorola.actions.ui.settings.FPSSlideAppSettingsFragment */
public class FPSSlideAppSettingsFragment extends SettingsDetailFragment {
    private static final int FPS_DIRECTION_REVERSE = 1;
    private Spinner mDirectionSpinner;

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.fps_slide_app_summary;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_settings_fps_slide_app;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.fps_slide_app_enabled;
    }

    public int getVideoId() {
        return C0504R.raw.fps_slide_app_settings;
    }

    public Class tutorialClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public FeatureKey getFeatureKey() {
        return FeatureKey.FPS_SLIDE_APP;
    }

    /* access modifiers changed from: protected */
    public SettingStatus getEnabledStatus() {
        if (FPSSlideHelper.isScrollOnAppEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        updateOptions(z);
        updateStatus(z ? SettingStatus.ENABLED : SettingStatus.DISABLED);
        FPSSlideAppSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        loadSettingsDropdown(view, getEnabledStatus() == SettingStatus.ENABLED);
    }

    public void onResume() {
        super.onResume();
        updateOptions(getEnabledStatus() == SettingStatus.ENABLED);
    }

    /* access modifiers changed from: 0000 */
    public final void loadSettingsDropdown(View view, boolean z) {
        int i;
        this.mDirectionSpinner = (Spinner) view.findViewById(C0504R.C0506id.spinner_modes);
        loadOptionsAdapter(this.mDirectionSpinner, C0504R.array.fps_slide_app_directions);
        Spinner spinner = this.mDirectionSpinner;
        if (FPSSlideHelper.isReversedScroll()) {
            i = 1;
        } else {
            i = this.mDirectionSpinner.getSelectedItemPosition();
        }
        spinner.setSelection(i);
        this.mDirectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                boolean z = true;
                if (i != 1) {
                    z = false;
                }
                FPSSlideHelper.setReversedScroll(z);
            }
        });
        updateOptions(z);
    }

    private void loadOptionsAdapter(Spinner spinner, int i) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), 17367048, getActivity().getResources().getStringArray(i));
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
    }

    private void updateOptions(boolean z) {
        if (this.mDirectionSpinner != null) {
            this.mDirectionSpinner.setEnabled(z);
        }
    }
}
