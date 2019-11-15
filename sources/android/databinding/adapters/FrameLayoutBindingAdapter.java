package android.databinding.adapters;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.widget.FrameLayout;

@BindingMethods({@BindingMethod(attribute = "android:foregroundTint", method = "setForegroundTintList", type = FrameLayout.class)})
@RestrictTo({Scope.LIBRARY})
public class FrameLayoutBindingAdapter {
}
