package com.motorola.actions.p013ui.settings;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/* renamed from: com.motorola.actions.ui.settings.FlipToMuteSettingsFragment$$Lambda$0 */
final /* synthetic */ class FlipToMuteSettingsFragment$$Lambda$0 implements OnCheckedChangeListener {
    private final FlipToMuteSettingsFragment arg$1;

    FlipToMuteSettingsFragment$$Lambda$0(FlipToMuteSettingsFragment flipToMuteSettingsFragment) {
        this.arg$1 = flipToMuteSettingsFragment;
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.arg$1.lambda$loadVibrationSetting$0$FlipToMuteSettingsFragment(compoundButton, z);
    }
}
