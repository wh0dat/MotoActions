package android.databinding.adapters;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.widget.ActionMenuView;

@BindingMethods({@BindingMethod(attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener", type = ActionMenuView.class)})
@RestrictTo({Scope.LIBRARY})
public class ActionMenuViewBindingAdapter {
}
