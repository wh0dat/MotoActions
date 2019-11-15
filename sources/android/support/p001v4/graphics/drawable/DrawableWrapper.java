package android.support.p001v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.drawable.DrawableWrapper */
public interface DrawableWrapper {
    Drawable getWrappedDrawable();

    void setWrappedDrawable(Drawable drawable);
}
