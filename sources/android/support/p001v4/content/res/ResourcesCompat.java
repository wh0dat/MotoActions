package android.support.p001v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.FontResourcesParserCompat.FamilyResourceEntry;
import android.support.p001v4.graphics.TypefaceCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v4.content.res.ResourcesCompat */
public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    @Nullable
    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int i, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawable(i, theme);
        }
        return resources.getDrawable(i);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources resources, @DrawableRes int i, int i2, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(i, i2, theme);
        }
        if (VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(i, i2);
        }
        return resources.getDrawable(i);
    }

    @ColorInt
    public static int getColor(@NonNull Resources resources, @ColorRes int i, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return resources.getColor(i, theme);
        }
        return resources.getColor(i);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources resources, @ColorRes int i, @Nullable Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(i, theme);
        }
        return resources.getColorStateList(i);
    }

    @Nullable
    public static Typeface getFont(@NonNull Context context, @FontRes int i) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, new TypedValue(), 0, null);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static Typeface getFont(@NonNull Context context, @FontRes int i, TypedValue typedValue, int i2, @Nullable TextView textView) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, typedValue, i2, textView);
    }

    private static Typeface loadFont(@NonNull Context context, int i, TypedValue typedValue, int i2, @Nullable TextView textView) {
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        Typeface loadFont = loadFont(context, resources, typedValue, i, i2, textView);
        if (loadFont != null) {
            return loadFont;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Font resource ID #0x");
        sb.append(Integer.toHexString(i));
        throw new NotFoundException(sb.toString());
    }

    private static Typeface loadFont(@NonNull Context context, Resources resources, TypedValue typedValue, int i, int i2, @Nullable TextView textView) {
        if (typedValue.string == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Resource \"");
            sb.append(resources.getResourceName(i));
            sb.append("\" (");
            sb.append(Integer.toHexString(i));
            sb.append(") is not a Font: ");
            sb.append(typedValue);
            throw new NotFoundException(sb.toString());
        }
        String charSequence = typedValue.string.toString();
        if (!charSequence.startsWith("res/")) {
            return null;
        }
        Typeface findFromCache = TypefaceCompat.findFromCache(resources, i, i2);
        if (findFromCache != null) {
            return findFromCache;
        }
        try {
            if (!charSequence.toLowerCase().endsWith(".xml")) {
                return TypefaceCompat.createFromResourcesFontFile(context, resources, i, charSequence, i2);
            }
            FamilyResourceEntry parse = FontResourcesParserCompat.parse(resources.getXml(i), resources);
            if (parse != null) {
                return TypefaceCompat.createFromResourcesFamilyXml(context, parse, resources, i, i2, textView);
            }
            Log.e(TAG, "Failed to find font-family tag");
            return null;
        } catch (XmlPullParserException e) {
            String str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to parse xml resource ");
            sb2.append(charSequence);
            Log.e(str, sb2.toString(), e);
            return null;
        } catch (IOException e2) {
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to read xml resource ");
            sb3.append(charSequence);
            Log.e(str2, sb3.toString(), e2);
            return null;
        }
    }

    private ResourcesCompat() {
    }
}
