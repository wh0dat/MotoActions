package com.motorola.actions.p013ui.settings;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo14495d2 = {"<anonymous>", "", "invoke"}, mo14496k = 3, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$changeStatus$1 */
/* compiled from: NightDisplaySettingsFragment.kt */
final class NightDisplaySettingsFragment$changeStatus$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ NightDisplaySettingsFragment this$0;

    NightDisplaySettingsFragment$changeStatus$1(NightDisplaySettingsFragment nightDisplaySettingsFragment) {
        this.this$0 = nightDisplaySettingsFragment;
        super(0);
    }

    public final void invoke() {
        this.this$0.setupNightDisplayUi();
    }
}
