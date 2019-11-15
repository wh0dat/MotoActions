package android.databinding.adapters;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p004v7.widget.SwitchCompat;

@BindingMethods({@BindingMethod(attribute = "android:thumb", method = "setThumbDrawable", type = SwitchCompat.class), @BindingMethod(attribute = "android:track", method = "setTrackDrawable", type = SwitchCompat.class)})
@RestrictTo({Scope.LIBRARY})
public class SwitchCompatBindingAdapter {
    @BindingAdapter({"android:switchTextAppearance"})
    public static void setSwitchTextAppearance(SwitchCompat switchCompat, int i) {
        switchCompat.setSwitchTextAppearance(null, i);
    }
}
