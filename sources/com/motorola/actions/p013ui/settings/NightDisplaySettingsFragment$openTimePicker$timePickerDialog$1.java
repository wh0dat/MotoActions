package com.motorola.actions.p013ui.settings;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.TimePicker;
import kotlin.Metadata;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\n¢\u0006\u0002\b\b"}, mo14495d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/widget/TimePicker;", "kotlin.jvm.PlatformType", "selectedHour", "", "selectedMinute", "onTimeSet"}, mo14496k = 3, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$openTimePicker$timePickerDialog$1 */
/* compiled from: NightDisplaySettingsFragment.kt */
final class NightDisplaySettingsFragment$openTimePicker$timePickerDialog$1 implements OnTimeSetListener {
    final /* synthetic */ boolean $isStartTime;
    final /* synthetic */ NightDisplaySettingsFragment this$0;

    NightDisplaySettingsFragment$openTimePicker$timePickerDialog$1(NightDisplaySettingsFragment nightDisplaySettingsFragment, boolean z) {
        this.this$0 = nightDisplaySettingsFragment;
        this.$isStartTime = z;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.this$0.onTimeSet(this.$isStartTime, i, i2);
    }
}
