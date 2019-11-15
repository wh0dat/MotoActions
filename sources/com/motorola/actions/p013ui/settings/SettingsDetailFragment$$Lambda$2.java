package com.motorola.actions.p013ui.settings;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/* renamed from: com.motorola.actions.ui.settings.SettingsDetailFragment$$Lambda$2 */
final /* synthetic */ class SettingsDetailFragment$$Lambda$2 implements OnCheckedChangeListener {
    private final SettingsDetailFragment arg$1;

    SettingsDetailFragment$$Lambda$2(SettingsDetailFragment settingsDetailFragment) {
        this.arg$1 = settingsDetailFragment;
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.arg$1.lambda$onResume$2$SettingsDetailFragment(compoundButton, z);
    }
}
