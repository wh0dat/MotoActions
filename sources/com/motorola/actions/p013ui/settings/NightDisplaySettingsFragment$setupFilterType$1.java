package com.motorola.actions.p013ui.settings;

import android.support.design.widget.Snackbar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.motorola.actions.nightdisplay.p008pd.TwilightAccess;
import com.motorola.actions.utils.MALogger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\f"}, mo14495d2 = {"com/motorola/actions/ui/settings/NightDisplaySettingsFragment$setupFilterType$1", "Landroid/widget/SeekBar$OnSeekBarChangeListener;", "onProgressChanged", "", "seekBar", "Landroid/widget/SeekBar;", "progress", "", "b", "", "onStartTrackingTouch", "onStopTrackingTouch", "MotoActions_release"}, mo14496k = 1, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$setupFilterType$1 */
/* compiled from: NightDisplaySettingsFragment.kt */
public final class NightDisplaySettingsFragment$setupFilterType$1 implements OnSeekBarChangeListener {
    final /* synthetic */ NightDisplaySettingsFragment this$0;

    NightDisplaySettingsFragment$setupFilterType$1(NightDisplaySettingsFragment nightDisplaySettingsFragment) {
        this.this$0 = nightDisplaySettingsFragment;
    }

    public void onProgressChanged(@NotNull SeekBar seekBar, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
        MALogger access$getLOGGER$cp = NightDisplaySettingsFragment.LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onProgressChanged: User interacting. Progress = ");
        sb.append(i);
        access$getLOGGER$cp.mo11957d(sb.toString());
        this.this$0.showNightDisplayPreview(i);
    }

    public void onStartTrackingTouch(@NotNull SeekBar seekBar) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
        NightDisplaySettingsFragment.LOGGER.mo11957d("onStartTrackingTouch");
        Snackbar access$getMPreviewSnackbar$p = this.this$0.mPreviewSnackbar;
        if (access$getMPreviewSnackbar$p != null) {
            access$getMPreviewSnackbar$p.show();
        }
        this.this$0.mHandler.removeCallbacks(this.this$0.mEndPreviewRunnable);
        this.this$0.mIsPreviewMode = true;
        this.this$0.showNightDisplayPreview(seekBar.getProgress());
    }

    public void onStopTrackingTouch(@NotNull SeekBar seekBar) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
        NightDisplaySettingsFragment.LOGGER.mo11957d("onStopTrackingTouch");
        TwilightAccess.setNightDisplayIntensity(this.this$0.getIntensity(seekBar.getProgress()));
        this.this$0.mHandler.postDelayed(this.this$0.mEndPreviewRunnable, 3000);
    }
}
