package android.databinding.adapters;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.widget.LinearLayout;

@BindingMethods({@BindingMethod(attribute = "android:divider", method = "setDividerDrawable", type = LinearLayout.class), @BindingMethod(attribute = "android:measureWithLargestChild", method = "setMeasureWithLargestChildEnabled", type = LinearLayout.class)})
@RestrictTo({Scope.LIBRARY})
public class LinearLayoutBindingAdapter {
}
