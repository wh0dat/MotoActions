package com.motorola.actions.p013ui.settings;

import android.view.View;
import com.motorola.actions.nightdisplay.common.Persistence;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo14495d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/view/View;", "invoke"}, mo14496k = 3, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$endClickListener$1 */
/* compiled from: NightDisplaySettingsFragment.kt */
final class NightDisplaySettingsFragment$endClickListener$1 extends Lambda implements Function1<View, Unit> {
    final /* synthetic */ NightDisplaySettingsFragment this$0;

    NightDisplaySettingsFragment$endClickListener$1(NightDisplaySettingsFragment nightDisplaySettingsFragment) {
        this.this$0 = nightDisplaySettingsFragment;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((View) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull View view) {
        Intrinsics.checkParameterIsNotNull(view, "<anonymous parameter 0>");
        this.this$0.openTimePicker(false, Persistence.getTerminationTimeInMinutes());
    }
}
