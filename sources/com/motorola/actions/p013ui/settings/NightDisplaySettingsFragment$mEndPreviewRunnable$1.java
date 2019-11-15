package com.motorola.actions.p013ui.settings;

import kotlin.Metadata;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo14495d2 = {"<anonymous>", "", "run"}, mo14496k = 3, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$mEndPreviewRunnable$1 */
/* compiled from: NightDisplaySettingsFragment.kt */
final class NightDisplaySettingsFragment$mEndPreviewRunnable$1 implements Runnable {
    final /* synthetic */ NightDisplaySettingsFragment this$0;

    NightDisplaySettingsFragment$mEndPreviewRunnable$1(NightDisplaySettingsFragment nightDisplaySettingsFragment) {
        this.this$0 = nightDisplaySettingsFragment;
    }

    public final void run() {
        this.this$0.stopPreviewMode();
    }
}
