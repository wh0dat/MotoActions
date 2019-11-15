package android.support.p004v7.text;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.text.method.TransformationMethod;
import android.view.View;
import java.util.Locale;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.text.AllCapsTransformationMethod */
public class AllCapsTransformationMethod implements TransformationMethod {
    private Locale mLocale;

    public void onFocusChanged(View view, CharSequence charSequence, boolean z, int i, Rect rect) {
    }

    public AllCapsTransformationMethod(Context context) {
        this.mLocale = context.getResources().getConfiguration().locale;
    }

    public CharSequence getTransformation(CharSequence charSequence, View view) {
        if (charSequence != null) {
            return charSequence.toString().toUpperCase(this.mLocale);
        }
        return null;
    }
}
